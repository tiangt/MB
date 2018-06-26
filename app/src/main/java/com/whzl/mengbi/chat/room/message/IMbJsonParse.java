package com.whzl.mengbi.chat.room.message;


public interface IMbJsonParse {
    <T> T fromJson(String json, Class<T> classOf);
}
