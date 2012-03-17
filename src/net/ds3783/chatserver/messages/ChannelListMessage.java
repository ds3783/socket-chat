package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.messages.model.ChannelModel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: ÏÂÎç1:56
 * To change this template use File | Settings | File Templates.
 */
public class ChannelListMessage extends SystemReplyMessage implements Message {
    private List<ChannelModel> channels;

    public List<ChannelModel> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelModel> channels) {
        this.channels = channels;
    }

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public String getContent() {
        return "";
    }

    public void setContent(String s) {

    }
}
