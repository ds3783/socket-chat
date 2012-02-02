package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.dao.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-17
 * Time: ÏÂÎç10:55
 * To change this template use File | Settings | File Templates.
 */
public class MessageContext {
    private Client sender;
    private List<Client> receivers;
    private Message message;

    private boolean emergency;

    private boolean dropClientAfterReply;

    private long createTime;


    public MessageContext() {
        receivers = new ArrayList<Client>();
        createTime = System.currentTimeMillis();
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public List<Client> getReceivers() {
        return receivers;
    }

    public List<Client> getReceiversClone() {
        return new ArrayList<Client>(receivers);
    }

    public void setReceivers(List<Client> receivers) {
        this.receivers = receivers;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isDropClientAfterReply() {
        return dropClientAfterReply;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setDropClientAfterReply(boolean dropClientAfterReply) {
        this.dropClientAfterReply = dropClientAfterReply;
    }
}
