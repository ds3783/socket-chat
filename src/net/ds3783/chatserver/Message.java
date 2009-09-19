package net.ds3783.chatserver;

import net.ds3783.chatserver.tools.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 20:47:03
 */
public class Message implements Serializable {
    private String id = Utils.newUuid();
    private String userUuid;
    private List<String> destUserName;
    private MessageType type;
    private String subType;
    private String destName;
    private String content;

    public String getId() {
        return id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }


    public List<String> getDestUserName() {
        return destUserName;
    }

    public void setDestUserName(List<String> destUserName) {
        this.destUserName = destUserName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
