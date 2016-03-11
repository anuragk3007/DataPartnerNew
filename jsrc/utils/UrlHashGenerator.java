package utils;

import exception.DPSystemException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: prashant.prakash
 * Date: Aug 14, 2012
 */
public class UrlHashGenerator {

    private static final String HASHING_ALGORITHM = "MD5";

    private static final ThreadLocal<MessageDigest> messageDigest_ = new ThreadLocal() {
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance(HASHING_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static SitePageUrlHash getUrlHash(String url) {
        if (messageDigest_.get() == null) {
            try {
                messageDigest_.set(MessageDigest.getInstance(HASHING_ALGORITHM));
            } catch (NoSuchAlgorithmException e) {
                throw new DPSystemException(e.getLocalizedMessage(), e);
            }
        }
        messageDigest_.get().reset();
        messageDigest_.get().update(url.getBytes());
        return new SitePageUrlHash(messageDigest_.get().digest());
    }
}
