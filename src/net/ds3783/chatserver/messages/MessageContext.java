package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.dao.Client;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-17
 * Time: ÏÂÎç10:55
 * To change this template use File | Settings | File Templates.
 */
public class MessageContext {
    private Client sender;
    private boolean emergency;

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }
}
