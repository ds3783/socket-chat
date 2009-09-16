package net.ds3783.chatserver.core;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 17:28:21
 */
public class ThreadResource {
    private Hashtable<String,CommonRunnable> uuidMap=new Hashtable<String, CommonRunnable>();
    private Hashtable<ThreadResourceType, List<CommonRunnable>> typeMap=new Hashtable<ThreadResourceType, List<CommonRunnable>>();

    public synchronized void register(String uuid, ThreadResourceType type,CommonRunnable thread){
        uuidMap.put(uuid,thread);
        if (!typeMap.containsKey(type)){
             typeMap.put(type,new Vector<CommonRunnable>());
        }
        List<CommonRunnable> result=typeMap.get(type);
        result.add(thread);
    }

    public CommonRunnable getThread(String uuid){
        return uuidMap.get(uuid);
    }

    public List<CommonRunnable> getThreads(ThreadResourceType type){
        List<CommonRunnable> result=typeMap.get(type);
        return new ArrayList<CommonRunnable>(result);
    }

}
