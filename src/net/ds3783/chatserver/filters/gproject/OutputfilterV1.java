package net.ds3783.chatserver.filters.gproject;

import net.ds3783.chatserver.core.OutputFilter;
import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-19
 * Time: 9:54:57
 */
public class OutputfilterV1 extends OutputFilter {

    @Override
    public String marshal(Client client, Message message, String content) {
        return (message.getContent()==null)?"":message.getContent();
    }

    @Override
    public String filte(Client client, String content) {
        return super.filte(client, content);
    }
}
