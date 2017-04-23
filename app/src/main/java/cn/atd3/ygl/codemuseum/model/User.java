package cn.atd3.ygl.codemuseum.model;

import org.litepal.crud.DataSupport;

/**
 * Created by YGL on 2017/3/26.
 */

public class User extends DataSupport{
    private String uid;
    private String name;
    private String email;
    private String pwd;
    private String cookie;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void coverSave(){
        DataSupport.deleteAll(User.class);
        super.save();
    }
}
