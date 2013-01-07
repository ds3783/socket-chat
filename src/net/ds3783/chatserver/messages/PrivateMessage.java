package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.EventConstant;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
public class PrivateMessage implements Message {
    private Date timestamp;
    private String senderName;
    private String senderId;
    private String reveiverId;
    private String reveiverName;
    private String content;

    public String getType() {
        return EventConstant.CHAT_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String s) {
        content = s;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public String getReveiverId() {
        return reveiverId;
    }

    public void setReveiverId(String reveiverId) {
        this.reveiverId = reveiverId;
    }

    public String getReveiverName() {
        return reveiverName;
    }

    public void setReveiverName(String reveiverName) {
        this.reveiverName = reveiverName;
    }
}
