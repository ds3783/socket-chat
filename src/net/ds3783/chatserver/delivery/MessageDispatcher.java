package net.ds3783.chatserver.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: ÉÏÎç12:48
 * To change this template use File | Settings | File Templates.
 */
public class MessageDispatcher {
    private Map<String, List<EventListener>> listeners = new HashMap<String, List<EventListener>>();

    public void dispatchEvent(Event evt) {
        List<EventListener> list = listeners.get(evt.getName());
        if (list != null) {
            for (EventListener listener : list) {
                listener.onEvent(evt);
            }
        }

    }

    public void addListener(String eventName, EventListener listener) {
        List<EventListener> list = listeners.get(eventName);
        if (list == null) {
            list = new ArrayList<EventListener>();
            listeners.put(eventName, list);
        }
        list.add(listener);
    }
}
