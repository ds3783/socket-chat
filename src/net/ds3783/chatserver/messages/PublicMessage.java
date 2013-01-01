package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: ÏÂÎç6:16
 * To change this template use File | Settings | File Templates.
 */
public class PublicMessage implements Message {
    private Long channelId;
    private String channelName;
    private String content;

    public String getType() {
        return MessageType.CHAT_MESSAGE;
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
}
