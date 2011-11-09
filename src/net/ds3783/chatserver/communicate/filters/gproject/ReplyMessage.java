package net.ds3783.chatserver.communicate.filters.gproject;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-21
 * Time: 11:14:22
 */
public class ReplyMessage implements Serializable {
    private String code;
    private String data;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
