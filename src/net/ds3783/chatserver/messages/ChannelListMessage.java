package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.messages.model.ChannelModel;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: ÏÂÎç1:56
 * To change this template use File | Settings | File Templates.
 */
public class ChannelListMessage implements Message {
    private Map<String, ChannelModel> channels;

    public Map<String, ChannelModel> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, ChannelModel> channels) {
        this.channels = channels;
    }

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public String getContent() {
        if (channels != null) {
            return StringUtils.join(channels.keySet(), ",");
        } else {
            return "";
        }
    }

    public void setContent(String s) {

    }
}
