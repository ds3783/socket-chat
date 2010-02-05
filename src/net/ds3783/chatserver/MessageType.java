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
    public static MessageType AUTH_MESSAGE = new MessageType("AUTHMSG", "��Ȩ��Ϣ");
    public static MessageType LOGIN_MESSAGE = new MessageType("LOGINMSG", "��¼��Ϣ");
    public static MessageType CHAT_MESSAGE = new MessageType("CHATMSG", "������Ϣ");
    public static MessageType COMMAND_MESSAGE = new MessageType("COMMANDMSG", "������Ϣ");

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageType that = (MessageType) o;

        return !(code != null ? !code.equals(that.code) : that.code != null);

    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
