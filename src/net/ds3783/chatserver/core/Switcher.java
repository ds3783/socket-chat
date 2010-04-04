package net.ds3783.chatserver.core;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-2
 * Time: 23:27:11
 * To change this template use File | Settings | File Templates.
 */
public interface Switcher<T> {
    public void route(T data);

    public void route(List<T> datas);

}
