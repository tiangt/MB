package com.whzl.mengbi.eventbus;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void sendEvent(EventBusBean event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(EventBusBean event) {
        EventBus.getDefault().postSticky(event);
    }


}
