package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Message;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:30
 */
public class OutputFilter {
    public String marshal(Message message, String content) {
        return content;
    }

    public String filte(String content) {
        return content;
    }
}
