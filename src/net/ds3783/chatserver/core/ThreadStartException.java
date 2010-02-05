package net.ds3783.chatserver.core;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-23
 * Time: 15:58:35
 */
public class ThreadStartException extends Exception {

    public ThreadStartException() {
    }

    public ThreadStartException(String message) {
        super(message);
    }

    public ThreadStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadStartException(Throwable cause) {
        super(cause);
    }
}
