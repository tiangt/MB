package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messageJson.LuckGiftJson;

public class LuckGiftEvent {
    private LuckGiftJson luckGiftJson;
    public LuckGiftEvent(LuckGiftJson luckGiftJson) {
        this.luckGiftJson = luckGiftJson;
    }
    public LuckGiftJson getLuckGiftJson() {
        return luckGiftJson;
    }

    //获取中奖的萌币数
    public int getTotalLuckMengBi() {
        int totalMengBi = 0;
        for(LuckGiftJson.ContextEntity.PrizesEntity prizesEntity:luckGiftJson.getContext().getPrizes()) {
            totalMengBi += prizesEntity.getGiftPrice() * prizesEntity.getRewardRatio() * prizesEntity.getTimes();
        }
        return totalMengBi;
    }

    //获取中奖的昵称
    public String getNickname() {
        return luckGiftJson.getContext().getNickname();
    }
}
