package net.ds3783.chatserver.communicate.filters;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.communicate.core.InputFilter;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-19
 * Time: 0:39:36
 *
 * @deprecated 已经不再采用过滤器实现协议
 */
@Deprecated
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
            message.setType(EventConstant.AUTH_MESSAGE);
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
            message.setType(EventConstant.LOGIN_MESSAGE);
            //角色名字
            message.setChannel(cMsg.getData());
            message.setAuthCode(cMsg.getToken());

        }

        if ("say".equals(cMsg.getCommand())) {
            message.setType(EventConstant.CHAT_MESSAGE);
            message.setChannel("BROADCAST");
            message.setContent(cMsg.getData());
            message.setAuthCode(cMsg.getToken());
        }

        if ("changeName".equals(cMsg.getCommand())) {
            message.setType(EventConstant.COMMAND_MESSAGE);
            message.setContent(cMsg.getData());
            message.setChannel("CHANGENAME");
        }


        return message;
    }

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
