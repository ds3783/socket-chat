package net.ds3783.chatserver.extension;

/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-12-3
 * Time: обнГ3:45
 * To change this template use File | Settings | File Templates.
 */
public class ClientException extends RuntimeException {
    public ClientException() {
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
