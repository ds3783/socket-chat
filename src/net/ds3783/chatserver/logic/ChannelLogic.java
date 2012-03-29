package net.ds3783.chatserver.logic;

import net.ds3783.chatserver.dao.Channel;
import net.ds3783.chatserver.dao.ChannelDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-29
 * Time: ÏÂÎç2:32
 * To change this template use File | Settings | File Templates.
 */
public class ChannelLogic {
    private List<Channel> configedChannels;
    private ChannelDao channelDao;

    public void setupDefaultChannels() {
        if (configedChannels != null) {
            List<Channel> channels = channelDao.getChannels();
            for (Channel configedChannel : configedChannels) {
                boolean found = false;
                for (Channel channel : channels) {
                    if (channel.isInternal() && channel.getName().equals(configedChannel.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    channelDao.registerChannel(configedChannel);
                }
            }
        }
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public void setConfigedChannels(List<Channel> configedChannels) {
        this.configedChannels = configedChannels;
    }
}
