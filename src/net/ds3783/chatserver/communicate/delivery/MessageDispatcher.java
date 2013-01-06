package net.ds3783.chatserver.communicate.delivery;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:48
 * To change this template use File | Settings | File Templates.
 */
public class MessageDispatcher {
    private Map<String, List<List<EventListener>>> listeners = new HashMap<String, List<List<EventListener>>>();
    private static final int MAXPRIORITY = 5;

    public static final int PRIORITY_LOWEST = 0;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_HIGHEST = 4;

    public void dispatchEvent(Event evt) {
        List<List<EventListener>> list1 = listeners.get(evt.getName());
        if (list1 != null) {
            for (int i = MAXPRIORITY - 1; i >= 0; i--) {
                List<EventListener> list = list1.get(i);
                for (EventListener listener : list) {
                    boolean result = listener.onEvent(evt);
                    if (!result) {
                        return;
                    }
                }
            }

        }

    }

    public void addListener(String eventName, EventListener listener) {
        this.addListener(eventName, PRIORITY_NORMAL, listener);

    }

    public void addListener(String eventName, int priority, EventListener listener) {
        List<List<EventListener>> list = listeners.get(eventName);
        if (list == null) {
            list = new ArrayList<List<EventListener>>();
            for (int i = 0; i < MAXPRIORITY; i++) {
                list.add(new ArrayList<EventListener>());
            }
            listeners.put(eventName, list);
        }
        list.get(priority).add(listener);
    }

    public void removeListener(String eventName, EventListener listener) {
        List<List<EventListener>> list = listeners.get(eventName);
        if (list != null) {
            list = new ArrayList<List<EventListener>>();
            for (int i = 0; i < MAXPRIORITY; i++) {
                List<EventListener> inner = list.get(i);
                for (Iterator<EventListener> iterator = inner.iterator(); iterator.hasNext(); ) {
                    EventListener eventListener = iterator.next();
                    if (eventListener == listener) {
                        //这里的确是用==判断，判断两个对象是否是同一个实例
                        iterator.remove();
                    }
                }
            }
            listeners.put(eventName, list);
        }
    }
}
