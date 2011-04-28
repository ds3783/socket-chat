package net.ds3783.chatserver;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-18
 * Time: 11:44:25
 */
public class MessageType implements Serializable {
    public static String AUTH_MESSAGE = "AUTHMSG";
    public static String LOGIN_MESSAGE = "LOGINMSG";
    public static String CHAT_MESSAGE = "CHATMSG";
    public static String COMMAND_MESSAGE = "COMMANDMSG";

}
