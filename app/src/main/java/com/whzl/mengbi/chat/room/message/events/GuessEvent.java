package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.GuessJson;

/**
 * @author nobody
 * @date 2019-06-13
 */
public class GuessEvent {
    public GuessJson guessJson;

    public GuessEvent(Context context, GuessJson guessJson) {
        this.guessJson = guessJson;
    }
}
