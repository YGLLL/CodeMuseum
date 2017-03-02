package cn.atd3.support.api.v1;

import cn.atd3.support.api.ServerException;

/**
 * Created by DXkite on 2017/3/2 0002.
 * Api活动
 */

public  abstract class ApiActions {
    public void needSignInCode(boolean need){}
    abstract public void serverException(ServerException e);
}
