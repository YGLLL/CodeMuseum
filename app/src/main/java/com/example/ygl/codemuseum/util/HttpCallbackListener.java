package com.example.ygl.codemuseum.util;

/**
 * Created by YGL on 2017/2/13.
 */

public interface HttpCallbackListener {
    void onFinish(Object response);
    void onError(Exception e);
}
