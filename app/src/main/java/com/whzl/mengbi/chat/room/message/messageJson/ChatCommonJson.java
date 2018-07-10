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
