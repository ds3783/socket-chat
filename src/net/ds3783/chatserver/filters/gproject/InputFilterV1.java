package net.ds3783.chatserver.filters.gproject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.core.InputFilter;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-19
 * Time: 0:39:36
 */
public class InputFilterV1 extends InputFilter {
    private Log logger = LogFactory.getLog(InputFilterV1.class);
    private Gson gson;
    private ClientDao clientDao;

    public Message unmarshal(Client client, String content, Message message) {
        /*if (content.endsWith("\0")){
            content =content.substring(0,content.length()-1);
        }*/
        //content = Utils.unescape(content);
        message.setUserUuid(client.getUid());
        if (content.startsWith("<") && content.endsWith(">")) {
            message.setContent(content);
            message.setType(MessageType.AUTH_MESSAGE);
            return message;
        }
        ClientMessage cMsg;
        try {
            cMsg = gson.fromJson(content, ClientMessage.class);
        } catch (JsonParseException e) {
            logger.warn("无效的消息:" + content);
            return message;
        }
        if ("login".equals(cMsg.getCommand())) {
            message.setType(MessageType.LOGIN_MESSAGE);
            //角色名字
            message.setChannel(cMsg.getData());
            message.setAuthCode(cMsg.getToken());

        }

        if ("say".equals(cMsg.getCommand())) {
            message.setType(MessageType.CHAT_MESSAGE);
            message.setChannel("BROADCAST");
            message.setContent(cMsg.getData());
            message.setAuthCode(cMsg.getToken());
        }

        if ("changeName".equals(cMsg.getCommand())) {
            message.setType(MessageType.COMMAND_MESSAGE);
            message.setContent(cMsg.getData());
            message.setChannel("CHANGENAME");
        }


        return message;
    }

    @Override
    public void filte(Client client, Message message) {
        return;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
