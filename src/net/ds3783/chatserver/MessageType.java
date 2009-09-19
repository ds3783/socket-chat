package net.ds3783.chatserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-18
 * Time: 11:44:25
 */
public class MessageType implements Serializable{
    public static MessageType AUTH_MESSAGE=new MessageType("AUTHMSG","授权消息");
    public static MessageType LOGIN_MESSAGE=new MessageType("LOGINMSG","登录消息");
    public static MessageType CHAT_MESSAGE=new MessageType("CHATMSG","聊天消息");
    public static MessageType COMMAND_MESSAGE=new MessageType("COMMANDMSG","命令消息");

    private MessageType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
