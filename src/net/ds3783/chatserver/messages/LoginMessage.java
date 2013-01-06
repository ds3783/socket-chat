package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-12-1
 * Time: 上午12:35
 * To change this template use File | Settings | File Templates.
 */
public class LoginMessage implements Message {
    private String username;
    private String password;

    public String getType() {
        return MessageType.LOGIN_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public String getContent() {
        return username;
    }

    public void setContent(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
