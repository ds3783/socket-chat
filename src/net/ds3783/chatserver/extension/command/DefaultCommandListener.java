package net.ds3783.chatserver.extension.command;

import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class DefaultCommandListener {
    protected MessageDispatcher commandDispatcher;
    protected OutputerSwitcher outputerSwitcher;


    public void setCommandDispatcher(MessageDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
