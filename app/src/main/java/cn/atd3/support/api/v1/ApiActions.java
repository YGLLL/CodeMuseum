package cn.atd3.support.api.v1;

import android.graphics.Bitmap;
import android.util.Log;
import cn.atd3.support.api.ServerException;

/**
 * Created by DXkite on 2017/3/2 0002.
 * Update by YGL on 2017/3/5.
 */

public abstract class ApiActions {
    final  static  String TAG="ApiActions";
    public void checkSignUpNeedCode(boolean need){ Log.i(TAG,"checkSignUpNeedCode"); }
    public void getCodePicture(Bitmap bitmap){Log.i(TAG,"getCodePicture");}
    public void checkUserId(boolean have){Log.i(TAG,"checkUserId");}
    public void checkUserEmail(boolean have){Log.i(TAG,"checkUserEmail");}
    public void checkSignInNeedCode(boolean need){ Log.i(TAG,"checkSignInNeedCode"); }
    public void userSignUp(boolean success,String message){ Log.i(TAG,"userSignIn"); }
    public void userSignIn(boolean success,String message){ Log.i(TAG,"userSignIn"); }
    public void beatHeart(String nextToken){Log.i(TAG,"beatHeart");}
    abstract public void serverException(ServerException e);
}
