package net.ds3783.chatserver.communicate.delivery;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:52
 * To change this template use File | Settings | File Templates.
 */
public interface EventListener {
    public boolean onEvent(Event event);
}
