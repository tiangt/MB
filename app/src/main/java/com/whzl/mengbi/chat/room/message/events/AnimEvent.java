package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;

public class AnimEvent {
    private AnimJson animJson;
    private String animUrl;

    public AnimEvent(AnimJson animJson, String animUrl) {
        this.animJson = animJson;
        this.animUrl = animUrl;
    }

    public AnimJson getAnimJson() {
        return animJson;
    }

    public String getAnimUrl() {
        return animUrl;
    }
}