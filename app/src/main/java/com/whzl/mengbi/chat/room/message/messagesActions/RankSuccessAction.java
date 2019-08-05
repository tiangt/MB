package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.events.RankSuccessEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.message.messageJson.RankSuccessJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2019-08-01
 */
public class RankSuccessAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RankSuccessAction  " + msgStr);
        RankSuccessJson rankSuccessJson = GsonUtils.GsonToBean(msgStr, RankSuccessJson.class);
        if (rankSuccessJson == null || rankSuccessJson.context == null) {
            return;
        }

        if ("SVGA".equals(rankSuccessJson.context.animation.animationType)) {
            AnimJson animJson = new AnimJson();
            List<AnimJson.ResourcesEntity> resourcesEntities = new ArrayList<>();
            AnimJson.ResourcesEntity resourcesEntity = new AnimJson.ResourcesEntity();
            resourcesEntity.setResType("PARAMS");
            resourcesEntity.setResValue("{'loopCount':" + 1 + "}");
            for (int i = 0; i < rankSuccessJson.context.animation.params.size(); i++) {
                RankSuccessJson.ContextBean.AnimationBean.ParamsBean paramsBean = rankSuccessJson.context.animation.params.get(i);
                if (paramsBean.paramsKey != null && "loopcount".equals(paramsBean.paramsKey)) {
                    resourcesEntity.setResValue("{'loopCount':" + paramsBean.paramsValue + "}");
                }
            }

            resourcesEntities.add(resourcesEntity);
            animJson.setResources(resourcesEntities);
            animJson.setAnimType("MOBILE_GIFT_SVGA");
            for (int i = 0; i < rankSuccessJson.context.animation.res.size(); i++) {
                RankSuccessJson.ContextBean.AnimationBean.ResBean resBean = rankSuccessJson.context.animation.res.get(i);
                if (resBean.resType != null && "SVGA".equals(resBean.resType)) {
                    AnimEvent animEvent = new AnimEvent(animJson, resBean.resUrl);
                    EventBus.getDefault().post(animEvent);
                    EventBus.getDefault().post(new RankSuccessEvent(context, rankSuccessJson));
                }
            }


        }
    }
}
