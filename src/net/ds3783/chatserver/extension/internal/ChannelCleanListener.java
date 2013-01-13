package net.ds3783.chatserver.extension.internal;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.InternalEvent;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;
import net.ds3783.chatserver.dao.*;
import net.ds3783.chatserver.logic.ChannelLogic;
import net.ds3783.chatserver.messages.ChannelLostMessage;
import net.ds3783.chatserver.messages.ClientLostMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.model.ChannelModel;
import net.ds3783.chatserver.messages.model.ClientModel;

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
    protected ContextHelper contextHelper;
    protected OutputerSwitcher outputerSwitcher;
    protected ClientDao clientDao;
    protected ChannelDao channelDao;
    protected ChannelLogic channelLogic;


    public boolean onEvent(Event evt) {
        if (evt instanceof InternalEvent) {
            InternalEvent event = (InternalEvent) evt;
            Client client = event.getClient();
            List<Long> bcChannels = new ArrayList<Long>();
            List<Channel> lostedChannels = new ArrayList<Channel>();
            if (client != null) {
                List<ClientChannel> channels = channelDao.getMyChannels(client.getUid());
                for (ClientChannel channel : channels) {
                    Channel deletedChannel = channelLogic.exitChannel(channel);
                    if (deletedChannel != null) {
                        lostedChannels.add(deletedChannel);
                    } else {
                        bcChannels.add(channel.getChannelId());
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
            if (lostedChannels.size() > 0) {
                //对所有人发送channel 删除消息
                ChannelLostMessage message = new ChannelLostMessage();
                //receivers
                MessageContext replyContext = contextHelper.registerMessage(message, null);
                replyContext.getReceivers().addAll(clientDao.getAllClients());

                ChannelModel[] chls = new ChannelModel[lostedChannels.size()];
                for (int i = 0; i < lostedChannels.size(); i++) {
                    Channel channel = lostedChannels.get(i);
                    chls[i] = new ChannelModel(channel);
                }
                message.setChannels(chls);


                outputerSwitcher.switchTo(message);

            }
            if (clients.size() > 0) {
                // 对clients 发送ClientExitMessage
                ClientLostMessage message = new ClientLostMessage();
                MessageContext replyContext = contextHelper.registerMessage(message, null);
                replyContext.getReceivers().addAll(clients);
                ClientModel c = new ClientModel(client, false);
                message.setClient(c);
                outputerSwitcher.switchTo(message);
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

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
