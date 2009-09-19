package net.ds3783.chatserver.filters.gproject;

import net.ds3783.chatserver.core.InputFilter;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.MessageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-19
 * Time: 0:39:36
 */
public class InputFilterV1 extends InputFilter {
    private Gson gson;
    @Override
    public Message unmarshal(Client client, String content, Message message) {
        if (content.startsWith("<") && content.endsWith(">")){
            message.setContent(content);
            message.setUserUuid(client.getUid());
            message.setType(MessageType.AUTH_MESSAGE);
            return message;
        }
         message.setContent(content);
        return message;
    }

    @Override
    public Message filte(Client client, Message message) {
        return super.filte(client, message);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
