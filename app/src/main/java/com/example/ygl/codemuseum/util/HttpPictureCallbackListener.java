package com.example.ygl.codemuseum.util;

import android.graphics.Bitmap;

/**
 * Created by YGL on 2017/2/25.
 */

public interface HttpPictureCallbackListener {
    void onFinish(Bitmap bitmap);
    void onError(Exception e);
}
