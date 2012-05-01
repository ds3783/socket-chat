package net.ds3783.chatserver.extension.command;

import net.ds3783.chatserver.CommandType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.Channel;
import net.ds3783.chatserver.dao.ChannelDao;
import net.ds3783.chatserver.dao.ClientChannel;
import net.ds3783.chatserver.extension.ExtensionException;
import net.ds3783.chatserver.logic.ChannelLogic;
import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.model.ChannelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 12-5-1
 * Time: ����10:45
 * To change this template use File | Settings | File Templates.
 */
public class JoinChannelListener extends DefaultCommandListener implements EventListener {
    private ContextHelper contextHelper;
    private ChannelDao channelDao;
    private ChannelLogic channelLogic;

    public boolean onEvent(Event event) {
        if (!CommandType.JOIN_CHANNEL.equals(event.getName())) {
            //��ListChannel���������Listener����
            return true;
        }
        ChannelListMessage reply = new ChannelListMessage();
        CommandMessage command = (CommandMessage) event.getMessage();
        MessageContext context = contextHelper.getContext(command);
        //receivers
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        replyContext.getReceivers().add(context.getSender());

        //�������Channel
        Long channelid = new Long(command.getContent());
        List<ClientChannel> myChannel = channelLogic.getMyChannels(context.getSender().getUid());
        for (ClientChannel clientChannel : myChannel) {
            if (clientChannel.getChannelId().equals(channelid)) {
                throw new ExtensionException("����Ƶ��ʧ�ܣ������ڴ�Ƶ����");
            }
        }
        Channel joinChannel = channelDao.getChannel(channelid);
        if (joinChannel == null) {
            throw new ExtensionException("����Ƶ��ʧ�ܣ���Ч��Ƶ����");
        }
        ClientChannel newChannel = channelLogic.joinChannel(context.getSender(), joinChannel);

        List<Channel> channels = channelDao.getChannels();
        ChannelModel[] chls = new ChannelModel[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            chls[i] = new ChannelModel(channel);
        }
        reply.setChannels(chls);
        myChannel.add(newChannel);
        //��õ�ǰ�Ѿ������channels
        List<Long> myChanList = new ArrayList<Long>();
        for (ClientChannel channel : myChannel) {
            myChanList.add(channel.getChannelId());
        }
        reply.setListeningChannels(myChanList.toArray(new Long[myChanList.size()]));


        outputerSwitcher.switchTo(reply);
        //��ֹ����Listener
        return false;
    }


    public void init() {
        commandDispatcher.addListener(CommandType.LIST_CHANNELS, this);
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
}
