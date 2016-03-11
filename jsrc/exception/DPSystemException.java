package exception;

/**
 * @author prashant.saksena
 * @since 16/8/12
 */
public class DPSystemException extends RuntimeException {
    private static final long serialVersionUID = 5886177900359549047L;

    public DPSystemException(String message) {
        super(message);
    }

    public DPSystemException(Throwable cause) {
        super(cause);
    }

    public DPSystemException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }
}
