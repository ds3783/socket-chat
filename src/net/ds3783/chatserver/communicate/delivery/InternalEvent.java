package net.ds3783.chatserver.communicate.delivery;

import net.ds3783.chatserver.dao.Client;

/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 13-1-7
 * Time: 下午6:55
 * To change this template use File | Settings | File Templates.
 */
public class InternalEvent extends Event {
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
