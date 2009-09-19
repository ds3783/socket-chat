package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.Client;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:39:49
 */
public abstract class InputFilter {
    public Message unmarshal(Client client,String content, Message message) {
        return message;
    }

    public Message filte(Client client,Message message) {
        return message;
    }
}
