package net.ds3783.chatserver.extension.command;

import net.ds3783.chatserver.CommandType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.communicate.delivery.MessageEvent;
import net.ds3783.chatserver.dao.*;
import net.ds3783.chatserver.extension.ClientException;
import net.ds3783.chatserver.logic.ChannelLogic;
import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.ClientJoinChannelMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.model.ChannelModel;
import net.ds3783.chatserver.messages.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 12-5-1
 * Time: 下午10:45
 * To change this template use File | Settings | File Templates.
 */
public class JoinChannelListener extends DefaultCommandListener implements EventListener {
    private ContextHelper contextHelper;
    private ClientDao clientDao;
    private ChannelDao channelDao;
    private ChannelLogic channelLogic;

    public boolean onEvent(Event messageEvent) {
        if (!CommandType.JOIN_CHANNEL.equals(messageEvent.getName())) {
            //非ListChannel命令交由其他Listener处理
            return true;
        }
        ChannelListMessage reply = new ChannelListMessage();
        MessageEvent event = (MessageEvent) messageEvent;
        CommandMessage command = (CommandMessage) event.getMessage();
        MessageContext context = contextHelper.getContext(command);
        //receivers
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        replyContext.getReceivers().add(context.getSender());

        //获得所有Channel
        Long channelid = new Long(command.getContent());
        List<ClientChannel> myChannel = channelLogic.getMyChannels(context.getSender().getUid());

        for (ClientChannel clientChannel : myChannel) {
            if (clientChannel.getChannelId().equals(channelid)) {
                throw new ClientException("Cannot join channel, U R already in it!");
            }
        }
        Channel joinChannel = channelDao.getChannel(channelid);

        if (joinChannel == null) {
            throw new ClientException("Unable to locate the channel!");
        }

        ClientJoinChannelMessage reply2 = new ClientJoinChannelMessage();
        MessageContext reply2Context = contextHelper.registerMessage(reply2, context.getSender());
        List<ClientChannel> ccs = channelDao.getClientsByChannel(channelid);
        List<Client> cs = new ArrayList<Client>(ccs.size());
        for (ClientChannel cc : ccs) {
            Client client = clientDao.getClient(cc.getClientId());
            cs.add(client);
        }
        reply2Context.getReceivers().addAll(cs);
        reply2.setClient(new ClientModel(context.getSender(), false));

        ClientChannel newChannel = channelLogic.joinChannel(context.getSender(), joinChannel);

        List<Channel> channels = channelDao.getChannels();
        ChannelModel[] chls = new ChannelModel[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            chls[i] = new ChannelModel(channel);
        }
        reply.setChannels(chls);
        myChannel.add(newChannel);
        //获得当前已经加入的channels
        List<Long> myChanList = new ArrayList<Long>();
        for (ClientChannel channel : myChannel) {
            myChanList.add(channel.getChannelId());
        }
        reply.setListeningChannels(myChanList.toArray(new Long[myChanList.size()]));


        outputerSwitcher.switchTo(reply);
        outputerSwitcher.switchTo(reply2);

        //阻止其他Listener
        return false;
    }


    public void init() {
        commandDispatcher.addListener(CommandType.JOIN_CHANNEL, this);
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public void setChannelLogic(ChannelLogic channelLogic) {
        this.channelLogic = channelLogic;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
