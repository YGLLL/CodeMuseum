package cn.atd3.support.api;

import org.json.JSONObject;

/**
 * Created by 冯世昌 on 2017/3/2 0002.
 */

public interface JSONListener {
    public void onSuccess(JSONObject object);
    public void onError(ServerException e);
}
