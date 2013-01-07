package net.ds3783.chatserver;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-18
 * Time: 11:44:25
 */
public class EventConstant implements Serializable {
    public static final String AUTH_MESSAGE = "MESSAGE_AUTH";
    public static final String LOGIN_MESSAGE = "MESSAGE_LOGIN";
    public static final String CHAT_MESSAGE = "MESSAGE_CHAT";
    public static final String COMMAND_MESSAGE = "MESSAGE_COMMAD";

    public static final String EVENT_CLIENT_OFFLINE = "EVENT_CLIENT_OFFLINE";

}
