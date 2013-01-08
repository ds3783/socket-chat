package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.messages.model.ChannelModel;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-8
 * Time: 下午10:57
 * To change this template use File | Settings | File Templates.
 */
public class ChannelLostMessage extends SystemReplyMessage implements Message {
    private ChannelModel[] channels;

    public ChannelModel[] getChannels() {
        return channels;
    }

    public void setChannels(ChannelModel[] channels) {
        this.channels = channels;
    }

    public String getType() {
        return EventConstant.COMMAND_MESSAGE;
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