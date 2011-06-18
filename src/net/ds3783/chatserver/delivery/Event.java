package net.ds3783.chatserver.delivery;

import net.ds3783.chatserver.Message;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç12:49
 * To change this template use File | Settings | File Templates.
 */
public class Event {
    private String name;
    private Message message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
