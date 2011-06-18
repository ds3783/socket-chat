package net.ds3783.chatserver.filters.gproject;

import com.google.gson.Gson;
import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.core.OutputFilter;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.tools.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-19
 * Time: 9:54:57
 */
public class OutputfilterV1 extends OutputFilter {
    private Gson gson;
    private ClientDao clientDao;

    public String marshal(Message message, String content) {
        if (MessageType.AUTH_MESSAGE.equals(message.getType())) {
            return (message.getContent() == null) ? "" : message.getContent();
        }
        if (MessageType.LOGIN_MESSAGE.equals(message.getType())) {
            ReplyMessage rm = new ReplyMessage();
            rm.setCode("login");
            rm.setData(message.getContent());
            rm.setName(message.getChannel());
            return Utils.escape(gson.toJson(rm));
        }
        if (MessageType.CHAT_MESSAGE.equals(message.getType())) {
            ReplyMessage rm = new ReplyMessage();

            rm.setData(message.getContent());
            Client sender = clientDao.getClient(message.getUserUuid());
            if (sender != null) {
                rm.setName(sender.getName());
            } else {
                rm.setName("");
            }

            if ("PRIVATE".equals(message.getChannel())) {
                rm.setCode("private");
            } else if ("BROADCAST".equals(message.getChannel())) {
                rm.setCode("world");
                Client sender1 = clientDao.getClient(message.getOtherProperty().get("SENDERID"));
                rm.setName(sender1.getName() == null ? "" : sender1.getName());
            } else if ("WORLDSHOW".equals(message.getChannel())) {
                rm.setCode("worldshow");
                Client sender1 = clientDao.getClient(message.getOtherProperty().get("SENDERID"));
                rm.setName(sender1.getName() == null ? "" : sender1.getName());
            } else if ("PARTY".equals(message.getChannel())) {
                rm.setCode("team");
            } else if ("NATION".equals(message.getChannel())) {
                rm.setCode("nation");
            } else if ("TEAM".equals(message.getChannel())) {
                rm.setCode("group");
                Client sender1 = clientDao.getClient(message.getOtherProperty().get("SENDERID"));
                rm.setName(sender1.getName() == null ? "" : sender1.getName());
            } else if ("TEAM".equals(message.getChannel())) {
                rm.setCode("group");
            } else if ("SYSTEM".equals(message.getChannel())) {
                rm.setCode("sys");
            }

            String json = gson.toJson(rm);
            return Utils.escape(json);
        }
        if (MessageType.COMMAND_MESSAGE.equals(message.getType())) {
            if ("EMPTYMSG".equals(message.getChannel())) {
                ReplyMessage rm = new ReplyMessage();
                rm.setCode("emptymsg");
                rm.setName("");
                rm.setData(message.getContent());

                String json = gson.toJson(rm);
                return Utils.escape(json) + "\r\n";
            }
            if ("TOTALUSER".equals(message.getChannel())) {
                ReplyMessage rm = new ReplyMessage();
                rm.setCode("totaluser");
                rm.setName("");
                rm.setData(message.getContent());

                String json = gson.toJson(rm);
                return Utils.escape(json) + "\r\n";
            }
            if ("USERONLINE".equals(message.getChannel())) {
                ReplyMessage rm = new ReplyMessage();
                rm.setCode("userisonline");
                rm.setName("");
                rm.setData(message.getContent());

                String json = gson.toJson(rm);
                return Utils.escape(json) + "\r\n";
            }
            if ("ADDBLACKLIST".equals(message.getChannel())) {
                ReplyMessage rm = new ReplyMessage();
                rm.setCode("delu");
                rm.setName("");
                rm.setData(message.getContent());

                String json = gson.toJson(rm);
                return Utils.escape(json) + "\r\n";
            }
            if ("ECHO".equals(message.getChannel())) {
                ReplyMessage rm = new ReplyMessage();
                rm.setCode("echo");
                rm.setName("");
                rm.setData(message.getContent());

                String json = gson.toJson(rm);
                return Utils.escape(json) + "\r\n";
            }
        }
        return "";
    }

    public String filte(String content) {
        return content + "\0";
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }
}
