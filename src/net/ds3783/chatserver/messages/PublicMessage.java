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
public class PublicMessage implements Message {
    private Long channelId;
    private Date timestamp;
    private String channelName;
    private String senderName;
    private String senderId;
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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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
}
