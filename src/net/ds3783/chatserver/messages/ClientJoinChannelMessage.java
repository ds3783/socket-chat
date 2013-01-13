package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.messages.model.ClientModel;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-12
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public class ClientJoinChannelMessage extends SystemReplyMessage implements Message {
    private ClientModel client;

    public ClientJoinChannelMessage() {
        this.setCode(SystemReplyMessage.CODE_OTHER_USER_ONLINE);
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public String getType() {
        return EventConstant.COMMAND_MESSAGE;
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
