package cn.atd3.ygl.codemuseum.util;

import org.litepal.crud.DataSupport;

import cn.atd3.ygl.codemuseum.model.User;

/**
 * Created by YGL on 2017/4/23.
 */

public static class SQLUtil {
    public static String MYCOOKIE;
    public static Boolean IsHaveUser(){
        User user= DataSupport.findFirst(User.class);
        return user!=null;
    }
}
