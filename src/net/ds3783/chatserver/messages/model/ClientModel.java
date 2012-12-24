package net.ds3783.chatserver.messages.model;

import net.ds3783.chatserver.dao.Client;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 12-12-24
 * Time: ÏÂÎç11:18
 * To change this template use File | Settings | File Templates.
 */
public class ClientModel implements Serializable {
    private String id;
    private String name;
    private boolean admin;
    private boolean mySelf;

    public ClientModel(Client client, boolean mySelf) {
        this.id = client.getUid();
        this.name = client.getName();
        this.admin = false;
        this.mySelf = mySelf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isMySelf() {
        return mySelf;
    }

    public void setMySelf(boolean mySelf) {
        this.mySelf = mySelf;
    }
}