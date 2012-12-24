package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.messages.model.ClientModel;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 12-12-24
 * Time: ÏÂÎç11:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientListMessage extends SystemReplyMessage implements Message {
    private ClientModel[] clients;


    public ClientListMessage() {
        this.setCode(SystemReplyMessage.CODE_CLIENT_LIST);
    }

    public ClientModel[] getClients() {
        return clients;
    }

    public void setClients(ClientModel[] clients) {
        this.clients = clients;
    }

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return true;
    }

    public String getContent() {
        return "";
    }

    public void setContent(String s) {

    }
}
