package net.ds3783.chatserver.logic;

import com.google.gson.Gson;
import net.ds3783.chatserver.dao.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
public class ChannelLogic {
    private static Log logger = LogFactory.getLog(ChannelLogic.class);
    private List<Channel> configedChannels;
    private ChannelDao channelDao;
    private ClientDao clientDao;

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

    /**
     * 查询当前所在channel
     *
     * @param uid
     * @return
     */
    public List<ClientChannel> getMyChannels(String uid) {
        List<ClientChannel> result = channelDao.getMyChannels(uid);
        List<Channel> channels = channelDao.getChannels();

        for (Iterator<ClientChannel> iterator = result.iterator(); iterator.hasNext(); ) {
            ClientChannel clientChannel = iterator.next();
            boolean found = false;
            for (Channel channel : channels) {
                if (channel.getId().equals(clientChannel.getChannelId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                channelDao.removeClientChannel(clientChannel);
                iterator.remove();
                Gson gson = new Gson();
                Client client = clientDao.getClient(clientChannel.getClientId());
                logger.warn("数据中存在ClientChannel(" + gson.toJson(clientChannel) + ")但是却不存在对应的Channel，Client(" + gson.toJson(client) + ")");
            }
        }
        return result;
    }

    /**
     * 加入频道
     *
     * @param sender
     * @param channel
     * @return
     */
    public ClientChannel joinChannel(Client sender, Channel channel) {
        ClientChannel cc = new ClientChannel();
        cc.setChannelId(channel.getId());
        cc.setClientId(sender.getUid());
        channelDao.addClientChannel(cc);
        return cc;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public void setConfigedChannels(List<Channel> configedChannels) {
        this.configedChannels = configedChannels;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
