package cn.atd3.support.api;

/**
 * Created by DXkite on 2017/3/1 0001.
 * 客户端信息未找到异常
 */

public class ClientNoFoundException extends RuntimeException {
    public ClientNoFoundException(Throwable cause) {
        super(cause);
    }

    public ClientNoFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNoFoundException(String message) {
        super(message);
    }

    public ClientNoFoundException() {
    }
}
