package net.ds3783.chatserver;

import net.ds3783.chatserver.tools.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 20:47:03
 */
public class Message implements Serializable {
    private String id = Utils.newUuid();
    private String userUuid;
    private HashMap<String, String> destUserUids = new HashMap<String, String>();
    private String type;
    private String channel;
    private String destUid;
    private String content;
    private String authCode;
    private HashMap<String, String> otherProperty = new HashMap<String, String>();

    private boolean dropClientAfterReply;

    public String getId() {
        return id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public HashMap<String, String> getDestUserUids() {
        return destUserUids;
    }

    public void setDestUserUids(HashMap<String, String> destUserUids) {
        this.destUserUids = destUserUids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDestUid() {
        return destUid;
    }

    public void setDestUid(String destUid) {
        this.destUid = destUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Map<String, String> getOtherProperty() {
        return otherProperty;
    }

    public boolean isDropClientAfterReply() {
        return dropClientAfterReply;
    }

    public void setDropClientAfterReply(boolean dropClientAfterReply) {
        this.dropClientAfterReply = dropClientAfterReply;
    }

    public Message simpleClone() {
        Message result = new Message();
        result.id = id;
        result.userUuid = userUuid;
        result.destUserUids = new HashMap<String, String>();
        result.type = this.getType();
        result.channel = channel;
        result.destUid = destUid;
        result.content = content;
        result.authCode = authCode;
        result.otherProperty = otherProperty;
        result.dropClientAfterReply = dropClientAfterReply;
        return result;
    }
}
