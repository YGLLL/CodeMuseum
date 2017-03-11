package cn.atd3.ygl.codemuseum.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by YGL on 2017/3/9.
 */

public class PrivateSession {
    private Bitmap userLogo;
    private String userName;
    private String messageQuantity;

    public Bitmap getUserLogo() {
        return userLogo;
    }
    public String getUserName() {
        return userName;
    }
    public String getMessageQuantity() {
        return messageQuantity;
    }

    public void setUserLogo(Bitmap userLogo) {
        this.userLogo = userLogo;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setMessageQuantity(String messageQuantity) {
        this.messageQuantity = messageQuantity;
    }
}
