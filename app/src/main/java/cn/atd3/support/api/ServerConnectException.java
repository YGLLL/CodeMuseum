package cn.atd3.support.api;

/**
 * Created by DXkite on 2017/3/1 0001.
 * API连接异常
 */

public class ServerConnectException extends  Exception {
    public ServerConnectException(String message) {
        super(message);
    }

    public ServerConnectException() {
    }

    public ServerConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
