package net.ds3783.chatserver.extension.chat;

import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: 下午11:00
 * To change this template use File | Settings | File Templates.
 */
public class DefaultChatListener {
    protected MessageDispatcher chatDispatcher;
    protected OutputerSwitcher outputerSwitcher;


    public void setChatDispatcher(MessageDispatcher chatDispatcher) {
        this.chatDispatcher = chatDispatcher;
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
