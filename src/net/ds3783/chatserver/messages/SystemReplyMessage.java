package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-12-1
 * Time: 上午12:00
 * To change this template use File | Settings | File Templates.
 */
public class SystemReplyMessage implements Message {
    public static final int CODE_100 = 100;

    public static final int CODE_200 = 200;
    public static final int CODE_LOGIN_SUCCESS = 201;//登录成功
    public static final int CODE_CHANNEL_LIST = 202;//频道列表

    public static final int CODE_USER_ONLINE = 301;//其他用户登录

    public static final int CODE_ERROR_NOT_LOGIN = 401;//尚未登录
    public static final int CODE_ERROR_WRONG_PASSWORD = 402;//登录用户名或密码错误
    public static final int CODE_ERROR_BLACKLIST = 403;//用户被禁言
    public static final int CODE_ERROR_USER_CUSTOM = 499;//用户被禁言

    private int code;
    private String content;

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
