package net.ds3783.chatserver.communicate.delivery;


import net.ds3783.chatserver.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ионГ12:04
 * To change this template use File | Settings | File Templates.
 */
public interface MessageProcessor {
    public void processMsg(Message msg, long now);
}
