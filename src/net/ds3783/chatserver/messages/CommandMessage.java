package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-27
 * Time: ÏÂÎç7:23
 * To change this template use File | Settings | File Templates.
 */
public class CommandMessage implements Message {
    private String command;
    private String content;

    public String getType() {
        return MessageType.COMMAND_MESSAGE;
    }

    public boolean isSerializable() {
        return false;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
