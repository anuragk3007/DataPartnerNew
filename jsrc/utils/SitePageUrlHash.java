package utils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>
 * Stores 128 bit hash. The hash is stored in 4 integers.
 * </p>
 *
 * @author prashant saksena
 *         Date: 24/7/12
 *         Time: 3:12 PM
 */
public class SitePageUrlHash implements Comparable<SitePageUrlHash>, Serializable {

    private final static long LONG_MASK = 0xffffffffL;
    private static final long serialVersionUID = 2178353062475788559L;

    /**
     * Most significant integers
     */
    private int msb1_;
    private int msb2_;
    private int msb3_;
    private int msb4_;

    public SitePageUrlHash(BigDecimal value) {
        this(value.toBigInteger());
    }

    public SitePageUrlHash(BigInteger value) {
        this(value.toByteArray());
    }

    public SitePageUrlHash(byte[] val) {
        if (val.length == 0)
            throw new NumberFormatException("Zero length Integer");

        initialize(val);
    }

    private void initialize(byte[] val) {
        int[] mag = stripLeadingZeroBytes(val);
        int len = mag.length;
        msb4_ = (len > 0) ? mag[len - 1] : 0;
        msb3_ = (len > 1) ? mag[len - 2] : 0;
        msb2_ = (len > 2) ? mag[len - 3] : 0;
        msb1_ = (len > 3) ? mag[len - 4] : 0;
    }

    private static int[] stripLeadingZeroBytes(byte a[]) {
        int byteLength = a.length;
        int keep;

        // Find first nonzero byte
        for (keep = 0; keep < byteLength && a[keep] == 0; keep++)
            ;

        // Allocate new array and copy relevant part of input array
        int intLength = ((byteLength - keep) + 3) >>> 2;
        int[] result = new int[intLength];
        int b = byteLength - 1;
        for (int i = intLength - 1; i >= 0; i--) {
            result[i] = a[b--] & 0xff;
            int bytesRemaining = b - keep + 1;
            int bytesToTransfer = Math.min(3, bytesRemaining);
            for (int j = 8; j <= (bytesToTransfer << 3); j += 8)
                result[i] |= ((a[b--] & 0xff) << j);
        }
        return result;
    }

    static int bitLengthForInt(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    public int hashCode() {
        int hashCode = 0;
        hashCode = (int) (31 * hashCode + (msb1_ & LONG_MASK));
        hashCode = (int) (31 * hashCode + (msb2_ & LONG_MASK));
        hashCode = (int) (31 * hashCode + (msb3_ & LONG_MASK));
        hashCode = (int) (31 * hashCode + (msb4_ & LONG_MASK));

        return hashCode;
    }

    public boolean equals(Object x) {
        if (x == this)
            return true;

        if (x == null)
            return false;

        if (!(x instanceof SitePageUrlHash))
            return false;
        SitePageUrlHash xInt = (SitePageUrlHash) x;

        if (this.getMsb1() != xInt.getMsb1()
                || this.getMsb2() != xInt.getMsb2()
                || this.getMsb3() != xInt.getMsb3()
                || this.getMsb4() != xInt.getMsb4()) {
            return false;
        }

        return true;
    }

    public int compareTo(SitePageUrlHash val) {
        if (this.getMsb1() != val.getMsb1()) {
            return ((this.getMsb1() & LONG_MASK) < (val.getMsb1() & LONG_MASK)) ? -1 : 1;
        }
        if (this.getMsb2() != val.getMsb2()) {
            return ((this.getMsb2() & LONG_MASK) < (val.getMsb2() & LONG_MASK)) ? -1 : 1;
        }
        if (this.getMsb3() != val.getMsb3()) {
            return ((this.getMsb3() & LONG_MASK) < (val.getMsb3() & LONG_MASK)) ? -1 : 1;
        }
        if (this.getMsb4() != val.getMsb4()) {
            return ((this.getMsb4() & LONG_MASK) < (val.getMsb4() & LONG_MASK)) ? -1 : 1;
        }

        return 0;
    }

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("magnitude", byte[].class),
    };

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {

        ObjectInputStream.GetField fields = s.readFields();
        byte[] magnitude = (byte[]) fields.get("magnitude", null);
        initialize(magnitude);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        ObjectOutputStream.PutField fields = s.putFields();
        fields.put("magnitude", magSerializedForm());
        s.writeFields();
    }

    private int[] toMagArray() {
        int[] mag;
        if (msb1_ != 0) {
            mag = new int[]{msb1_, msb2_, msb3_, msb4_};
        } else if (msb2_ != 0) {
            mag = new int[]{msb2_, msb3_, msb4_};
        } else if (msb3_ != 0) {
            mag = new int[]{msb3_, msb4_};
        } else if (msb4_ != 0) {
            mag = new int[]{msb4_};
        } else {
            mag = new int[0];
        }
        return mag;
    }

    private byte[] magSerializedForm() {
        int[] mag = toMagArray();
        int len = mag.length;

        int bitLen = (len == 0 ? 0 : ((len - 1) << 5) + bitLengthForInt(mag[0]));
        int byteLen = (bitLen + 7) / 8;
        byte[] result = new byte[byteLen];

        for (int i = byteLen - 1, bytesCopied = 4, intIndex = len - 1, nextInt = 0;
             i >= 0; i--) {
            if (bytesCopied == 4) {
                nextInt = mag[intIndex--];
                bytesCopied = 1;
            } else {
                nextInt >>>= 8;
                bytesCopied++;
            }
            result[i] = (byte) nextInt;
        }
        return result;
    }

    public byte[] toByteArray() {
        int[] mag = toMagArray();
        int byteLen = bitLength(mag) / 8 + 1;
        byte[] byteArray = new byte[byteLen];

        for (int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 0; i >= 0; i--) {
            if (bytesCopied == 4) {
                nextInt = getInt(mag, intIndex++);
                bytesCopied = 1;
            } else {
                nextInt >>>= 8;
                bytesCopied++;
            }
            byteArray[i] = (byte) nextInt;
        }
        return byteArray;
    }

    public int bitLength(int[] mag) {
        int n = -1;
        int len = mag.length;
        if (len == 0) {
            n = 0; // offset by one to initialize
        } else {
            // Calculate the bit length of the magnitude
            int magBitLength = ((len - 1) << 5) + bitLengthForInt(mag[0]);
            n = magBitLength;
        }
        return n;
    }

    private int getInt(int[] mag, int n) {
        if (n < 0)
            return 0;
        if (n >= mag.length)
            return signInt();

        int magInt = mag[mag.length - n - 1];

        return magInt;
    }

    private int signInt() {
        return 0;
    }

    public BigDecimal getValue() {
        return new BigDecimal(new BigInteger(toByteArray()));
    }

    public int getMsb1() {
        return msb1_;
    }

    public int getMsb2() {
        return msb2_;
    }

    public int getMsb3() {
        return msb3_;
    }

    public int getMsb4() {
        return msb4_;
    }

    @Override
    public String toString() {
        return new BigInteger(toByteArray()).toString();
    }
}
