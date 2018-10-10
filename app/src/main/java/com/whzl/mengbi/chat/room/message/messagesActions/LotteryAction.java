package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.GiftJson;
import com.whzl.mengbi.chat.room.message.messageJson.LotteryJson;
import com.whzl.mengbi.chat.room.message.messageJson.SystemMsgJson;
import com.whzl.mengbi.chat.room.message.messages.GiftMsg;
import com.whzl.mengbi.chat.room.message.messages.LotteryMessage;
import com.whzl.mengbi.chat.room.message.messages.SystemMessage;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * @author nobody
 */
public class LotteryAction implements Actions {

    private Disposable disposable;

    @Override
    public void performAction(String msgStr,  final Context context) {
        LotteryJson lotteryJson = GsonUtils.GsonToBean(msgStr, LotteryJson.class);
        if (null == lotteryJson || lotteryJson.context == null) {
            return;
        }
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(aLong -> {
                     if (aLong == 10) {
                         disposable.dispose();
                         EventBus.getDefault().post(new UpdatePubChatEvent(new LotteryMessage(lotteryJson, context)));
                     }
                 });
    }
}
