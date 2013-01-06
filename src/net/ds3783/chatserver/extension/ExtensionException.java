package net.ds3783.chatserver.extension;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 12-5-1
 * Time: 下午10:55
 * To change this template use File | Settings | File Templates.
 */
public class ExtensionException extends RuntimeException {
    public ExtensionException() {
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }
}
