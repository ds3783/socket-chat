package net.ds3783.chatserver.extension.internal;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.InternalEvent;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.dao.ChannelDao;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientChannel;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.logic.ChannelLogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 13-1-8
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class ChannelCleanListener implements EventListener {

    protected MessageDispatcher messageDispatcher;
    protected ClientDao clientDao;
    protected ChannelDao channelDao;
    protected ChannelLogic channelLogic;


    public boolean onEvent(Event evt) {
        if (evt instanceof InternalEvent) {
            InternalEvent event = (InternalEvent) evt;
            Client client = event.getClient();
            List<Long> bcChannels = new ArrayList<Long>();
            boolean hasChannelDeleted = false;
            if (client != null) {
                List<ClientChannel> channels = channelDao.getMyChannels(client.getUid());
                for (ClientChannel channel : channels) {
                    boolean isChannelDeleted = channelLogic.exitChannel(channel);
                    if (!isChannelDeleted) {
                        bcChannels.add(channel.getChannelId());
                    } else {
                        hasChannelDeleted = true;
                    }
                }
            }
            HashSet<Client> clients = new HashSet<Client>();
            for (Long channeId : bcChannels) {
                List<ClientChannel> cs = channelDao.getClientsByChannel(channeId);
                for (ClientChannel c : cs) {
                    Client cl = clientDao.getClient(c.getClientId());
                    if (cl != null) {
                        clients.add(cl);
                    }
                }
            }
            if (hasChannelDeleted) {
                //TODO: 对所有人发送update channel List
            }
            if (clients.size() > 0) {
                //TODO:: 对clients 发送ClientExitMessage
            }
        }
        return true;
    }

    public void init() {
        messageDispatcher.addListener(EventConstant.EVENT_CLIENT_OFFLINE, this);
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setChannelLogic(ChannelLogic channelLogic) {
        this.channelLogic = channelLogic;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
