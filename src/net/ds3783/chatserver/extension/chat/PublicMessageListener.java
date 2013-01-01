package net.ds3783.chatserver.extension.chat;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.Channel;
import net.ds3783.chatserver.dao.ChannelDao;
import net.ds3783.chatserver.dao.ClientChannel;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.extension.ClientException;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.PublicMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: 下午10:28
 * To change this template use File | Settings | File Templates.
 */
public class PublicMessageListener extends DefaultChatListener implements EventListener {
    private ContextHelper contextHelper;
    private ChannelDao channelDao;
    private ClientDao clientDao;

    public boolean onEvent(Event event) {
        if (event.getName().equals(PublicMessage.class.getSimpleName())) {
            PublicMessage message = (PublicMessage) event.getMessage();
            MessageContext context = contextHelper.getContext(message);
            //验证该channel是否存在
            Channel channel = channelDao.getChannel(message.getChannelId());
            if (channel == null) {
                throw new ClientException("Invalid Channel[id=" + message.getChannelId() + "]!");
            }

            //验证用户是否在该Channel内
            boolean inChannel = channelDao.isInChannel(context.getSender().getUid(), channel.getId());
            if (!inChannel) {
                throw new ClientException("U R not in channel[id=" + message.getChannelId() + "]!");
            }
            PublicMessage reply = new PublicMessage();
            reply.setChannelId(channel.getId());
            reply.setChannelName(channel.getName());
            reply.setContent(message.getContent());
            MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
            List<ClientChannel> cList = channelDao.getClientsByChannel(channel.getId());
            for (ClientChannel clientChannel : cList) {
                replyContext.getReceivers().add(clientDao.getClient(clientChannel.getClientId()));
            }
            outputerSwitcher.switchTo(reply);
            return false;
        } else {
            return true;
        }
    }


    public void init() {
        chatDispatcher.addListener(PublicMessage.class.getSimpleName(), this);
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
