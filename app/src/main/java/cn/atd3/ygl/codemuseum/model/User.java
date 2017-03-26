package cn.atd3.ygl.codemuseum.model;

/**
 * Created by YGL on 2017/3/26.
 */

public class User {
    private int uid;
    private String name;
    private String pwd;
    private String beat_token;

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getBeat_token() {
        return beat_token;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setBeat_token(String beat_token) {
        this.beat_token = beat_token;
    }
}
