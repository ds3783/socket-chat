package net.ds3783.chatserver.extension.command;

import net.ds3783.chatserver.CommandType;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.EventListener;
import net.ds3783.chatserver.dao.ChannelDao;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientChannel;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.logic.ChannelLogic;
import net.ds3783.chatserver.messages.ClientListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.model.ClientModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 12-12-24
 * Time: 下午11:00
 * To change this template use File | Settings | File Templates.
 */
public class UpdateClientListListener extends DefaultCommandListener implements EventListener {
    private ContextHelper contextHelper;
    private ChannelDao channelDao;
    private ClientDao clientDao;
    private ChannelLogic channelLogic;

    public boolean onEvent(Event event) {
        if (!CommandType.UPDATE_CLIENT_LIST.equals(event.getName())) {
            //非ListChannel命令交由其他Listener处理
            return true;
        }
        CommandMessage command = (CommandMessage) event.getMessage();
        MessageContext context = contextHelper.getContext(command);
        ClientListMessage reply = new ClientListMessage();
        List<ClientChannel> channels = channelDao.getMyChannels(context.getSender().getUid());
        HashSet<String> clients = new HashSet<String>();
        for (ClientChannel channel : channels) {
            List<ClientChannel> ccs = channelDao.getClientsByChannel(channel.getChannelId());
            for (ClientChannel clientChannel : ccs) {
                clients.add(clientChannel.getClientId());
            }
        }
        clients.remove(context.getSender().getUid());
        ArrayList<Client> realClients = new ArrayList<Client>(clients.size());
        for (String clientid : clients) {
            realClients.add(clientDao.getClient(clientid));
        }
        Collections.sort(realClients);

        ArrayList<ClientModel> realClientModels = new ArrayList<ClientModel>(clients.size());
        realClientModels.add(new ClientModel(context.getSender(), true));
        for (Client client : realClients) {
            realClientModels.add(new ClientModel(client, false));
        }
        reply.setClients(realClientModels.toArray(new ClientModel[realClientModels.size()]));
        //receivers
        MessageContext replyContext = contextHelper.registerMessage(reply, context.getSender());
        replyContext.getReceivers().add(context.getSender());


        outputerSwitcher.switchTo(reply);
        //阻止其他Listener
        return false;
    }


    public void init() {
        commandDispatcher.addListener(CommandType.UPDATE_CLIENT_LIST, this);
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
