package cn.atd3.ygl.codemuseum.model;

import org.litepal.crud.DataSupport;

/**
 * Created by YGL on 2017/2/20.
 */

public class UserMessage extends DataSupport {
    private String message_time;
    private String message_sender;
    private String message_paper;
    private String message_content;

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_sender() {
        return message_sender;
    }

    public void setMessage_sender(String message_sender) {
        this.message_sender = message_sender;
    }

    public String getMessage_paper() {
        return message_paper;
    }

    public void setMessage_paper(String message_paper) {
        this.message_paper = message_paper;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }
}
