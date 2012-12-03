package net.ds3783.chatserver.extension.command;

import flex.messaging.util.StringUtils;
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
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-12-3
 * Time: ����2:25
 * To change this template use File | Settings | File Templates.
 */
public class CreateChannelListener extends DefaultCommandListener implements EventListener {
    private ContextHelper contextHelper;
    private ChannelDao channelDao;
    private ChannelLogic channelLogic;

    public boolean onEvent(Event event) {
        if (!CommandType.CREATE_CHANNEL.equals(event.getName())) {
            //��ListChannel���������Listener����
            return true;
        }
        ChannelListMessage reply = new ChannelListMessage();
        CommandMessage command = (CommandMessage) event.getMessage();
        MessageContext context = contextHelper.getContext(command);

        //�������Channel
        String channelName = command.getContent();
        if (channelName != null) {
            channelName = channelName.trim();
        }
        if (StringUtils.isEmpty(channelName)) {
            throw new ExtensionException("Channel name cannot be null when creating!");
        }
        Channel newChannel = channelDao.getChannelByName(channelName);
        if (newChannel != null) {
            throw new ExtensionException("[CREATE CHANNEL]channel:" + channelName + " already exists!");
        }
        channelLogic.createChannel(channelName, true, context.getSender());

        //receivers
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        replyContext.getReceivers().add(context.getSender());
        List<Channel> channels = channelDao.getChannels();
        ChannelModel[] chls = new ChannelModel[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            chls[i] = new ChannelModel(channel);
        }
        reply.setChannels(chls);
        //��õ�ǰ�Ѿ������channels
        List<ClientChannel> myChannel = channelLogic.getMyChannels(context.getSender().getUid());
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
        commandDispatcher.addListener(CommandType.CREATE_CHANNEL, this);
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
