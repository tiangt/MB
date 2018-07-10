package com.whzl.mengbi.chat.room.message;

import com.google.gson.Gson;


public class MbJsonParserImpl implements IMbJsonParse{
    private Gson gson;

    public MbJsonParserImpl(){
        gson = new Gson();
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOf) {
        try {
            return gson.fromJson(json,classOf);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
