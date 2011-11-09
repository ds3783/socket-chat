package net.ds3783.chatserver.communicate.protocol;

import net.ds3783.chatserver.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 22:59:19
 */
public abstract class OutputProtocal {
    protected List<Message> messages = new ArrayList<Message>();

    /**
     * 添加一条消息
     *
     * @param msg 消息
     */
    public void addMessage(Message msg) {
        messages.add(msg);
    }

    /**
     * 添加若干消息
     *
     * @param msgs 消息的集合
     */
    public void addMessages(Collection<Message> msgs) {
        if (msgs != null)
            messages.addAll(msgs);
    }

    /**
     * 编码
     *
     * @return 待输出到网络上的字节数组
     */
    public abstract byte[] marshal() throws MarshalException;

}
