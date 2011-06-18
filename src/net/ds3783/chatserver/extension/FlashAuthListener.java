package net.ds3783.chatserver.extension;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Configuration;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.core.OutputerSwitcher;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.delivery.Event;
import net.ds3783.chatserver.delivery.EventListener;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç1:38
 * To change this template use File | Settings | File Templates.
 */
public class FlashAuthListener implements EventListener {
    private OutputerSwitcher outputerSwitcher;
    private Configuration config;
    private ClientDao clientDao;

    public void onEvent(Event event) {
        Message msg = event.getMessage();
        Message reply = new Message();
        reply.setContent("<cross-domain-policy><allow-access-from domain=\"" + config.getAddress() + "\" to-ports=\"" + config.getPort() + "\" /></cross-domain-policy>");

        Client client = clientDao.getClient(msg.getUserUuid());
        client.setAuthed(true);
        HashMap<String, String> destUsers = new HashMap();
        destUsers.put(client.getUid(), client.getUid());
        reply.setDestUserUids(destUsers);
        reply.setUserUuid(msg.getUserUuid());
        reply.setChannel(msg.getChannel());
        reply.setType(msg.getType());
        outputerSwitcher.switchTo(reply);
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
