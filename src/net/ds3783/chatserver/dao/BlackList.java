package net.ds3783.chatserver.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-7-5
 * Time: ÏÂÎç8:21
 * To change this template use File | Settings | File Templates.
 */
public class BlackList {
    private Long id;
    private String uid;
    private long blackTime;
    private long expiredTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getBlackTime() {
        return blackTime;
    }

    public void setBlackTime(long blackTime) {
        this.blackTime = blackTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
