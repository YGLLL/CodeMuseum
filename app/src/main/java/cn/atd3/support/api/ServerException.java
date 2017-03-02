package cn.atd3.support.api;

/**
 * Created by DXkite on 2017/3/1 0001.
 * 服务器异常
 */

public class ServerException extends  Exception {
    public ServerException(String message) {
        super(message);
    }

    public ServerException() {
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
