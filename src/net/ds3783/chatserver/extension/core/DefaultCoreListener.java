package net.ds3783.chatserver.extension.core;

import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.communicate.delivery.MessageDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午11:17
 * 默认的监听器类，其不执行任何操作，仅用于spring注入
 */
public class DefaultCoreListener {
    protected MessageDispatcher messageDispatcher;
    protected OutputerSwitcher outputerSwitcher;

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }
}
