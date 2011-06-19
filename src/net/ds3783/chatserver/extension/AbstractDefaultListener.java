package net.ds3783.chatserver.extension;

import net.ds3783.chatserver.core.OutputerSwitcher;
import net.ds3783.chatserver.delivery.MessageDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç11:17
 * To change this template use File | Settings | File Templates.
 */
public class AbstractDefaultListener {
    protected MessageDispatcher messageDispatcher;
    protected MessageDispatcher channelDispatcher;
    protected OutputerSwitcher outputerSwitcher;

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setChannelDispatcher(MessageDispatcher channelDispatcher) {
        this.channelDispatcher = channelDispatcher;
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
