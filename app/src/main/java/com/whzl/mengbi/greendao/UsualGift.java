package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author nobody
 * @date 2019/3/5
 */
@Entity
public class UsualGift {
    @Id(autoincrement = true)
    private Long id;

    public Long giftId;
    public Long times;

    private Long userId;

    @Generated(hash = 999656973)
    public UsualGift(Long id, Long giftId, Long times, Long userId) {
        this.id = id;
        this.giftId = giftId;
        this.times = times;
        this.userId = userId;
    }

    @Generated(hash = 1222170215)
    public UsualGift() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGiftId() {
        return this.giftId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public Long getTimes() {
        return this.times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
