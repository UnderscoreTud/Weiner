package me.tud.weiner.exception;

public class WienerException extends RuntimeException {

    public WienerException() {
    }

    public WienerException(String message) {
        super(message);
    }

    public WienerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WienerException(Throwable cause) {
        super(cause);
    }

    public WienerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
