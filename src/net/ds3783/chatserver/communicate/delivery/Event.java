package net.ds3783.chatserver.communicate.delivery;

/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 13-1-7
 * Time: 下午6:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class Event {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
