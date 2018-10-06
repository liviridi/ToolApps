package com.liviridi.tools.common.exception;

public class UnableToInitializeException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3062691949926103865L;

    public UnableToInitializeException() {
        super();
    }

    public UnableToInitializeException(String message) {
        super(message);
    }

    public UnableToInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToInitializeException(Throwable cause) {
        super(cause);
    }

    protected UnableToInitializeException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
