package org.ogorodnik.shop.error;

public class ItemNotFountException extends Exception{
    public ItemNotFountException() {
        super();
    }

    public ItemNotFountException(String message) {
        super(message);
    }

    public ItemNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemNotFountException(Throwable cause) {
        super(cause);
    }

    protected ItemNotFountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
