package net.ds3783.chatserver.extension.command;

import net.ds3783.chatserver.CommandType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.Channel;
import net.ds3783.chatserver.dao.ChannelDao;
import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.model.ChannelModel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-27
 * Time: 下午7:22
 * To change this template use File | Settings | File Templates.
 */
public class ListChannelListener extends DefaultCommandListener implements EventListener {
    private ContextHelper contextHelper;
    private ChannelDao channelDao;

    public boolean onEvent(Event event) {
        if (!CommandType.LIST_CHANNELS.equals(event.getName())) {
            //非ListChannel命令交由其他Listener处理
            return true;
        }
        ChannelListMessage reply = new ChannelListMessage();
        reply.setListeningChannels(new Long[0]);//TODO::
        CommandMessage command = (CommandMessage) event.getMessage();
        MessageContext context = contextHelper.getContext(command);
        //receivers
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        replyContext.getReceivers().add(context.getSender());

        //获得所有Channel
        List<Channel> channels = channelDao.getChannels();
        ChannelModel[] chls = new ChannelModel[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            chls[i] = new ChannelModel(channel);
        }
        reply.setChannels(chls);
        outputerSwitcher.switchTo(reply);
        //阻止其他Listener
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
}
