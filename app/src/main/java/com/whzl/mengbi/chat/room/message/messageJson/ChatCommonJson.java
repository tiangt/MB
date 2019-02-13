package com.whzl.mengbi.chat.room.message.messageJson;

public class ChatCommonJson {
    String content;
    String create_time;
    String from_nickname;
    String from_uid;
    String to_nickname;
    String to_uid;
    FromJson from_json;
    FromJson to_json;

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public void setFrom_json(FromJson from_json) {
        this.from_json = from_json;
    }

    public void setTo_json(FromJson to_json) {
        this.to_json = to_json;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public FromJson getTo_json() {
        return to_json;
    }

    public FromJson getFrom_json() {
        return from_json;
    }
}
