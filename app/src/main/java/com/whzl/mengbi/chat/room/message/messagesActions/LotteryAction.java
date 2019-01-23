package com.whzl.mengbi.chat.room.message.messagesActions;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.LotteryJson;
import com.whzl.mengbi.chat.room.message.messages.LotteryMessage;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @author nobody
 */
public class LotteryAction implements Actions {

    private Disposable disposable;

    @Override
    public void performAction(String msgStr, final Context context) {
        LogUtils.e("lottery   " + msgStr);
        LotteryJson lotteryJson = GsonUtils.GsonToBean(msgStr, LotteryJson.class);
        if (null == lotteryJson || lotteryJson.context == null) {
            return;
        }
        Observable.timer(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LiveDisplayActivity) context, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        EventBus.getDefault().post(new UpdatePubChatEvent(new LotteryMessage(lotteryJson, context)));
                    }
                });
    }
}
