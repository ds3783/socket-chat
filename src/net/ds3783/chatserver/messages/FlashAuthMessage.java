package net.ds3783.chatserver.messages;

import net.ds3783.chatserver.MessageType;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-17
 * Time: 下午10:50
 */
public class FlashAuthMessage implements Message {
    private String content;

    public String getType() {
        return MessageType.AUTH_MESSAGE;
    }

    public boolean isSerializable() {
        return false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
