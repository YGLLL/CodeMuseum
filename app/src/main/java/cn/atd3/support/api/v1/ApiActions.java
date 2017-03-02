package cn.atd3.support.api.v1;

import android.util.Log;

import cn.atd3.support.api.ServerException;

/**
 * Created by DXkite on 2017/3/2 0002.
 * Api活动
 */

public  abstract class ApiActions {
    final  static  String TAG="ApiActions";
    public void checkSignInNeedCode(boolean need){ Log.i(TAG,"checkSignInNeedCode"); }
    public void checkSignUpNeedCode(boolean need){ Log.i(TAG,"checkSignUpNeedCode"); }
    public void userSignIn(boolean success,String userToken){ Log.i(TAG,"userSignIn"); }
    public void userSignUp(boolean success,int uid,String userToken){ Log.i(TAG,"userSignIn"); }
    abstract public void serverException(ServerException e);
}
