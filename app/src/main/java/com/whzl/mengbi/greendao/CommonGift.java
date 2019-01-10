package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author nobody
 * @date 2019/1/10
 */
@Entity
public class CommonGift {
    @Id
    Long userId;

    @Convert(columnType = String.class, converter = CommonGift_Converter.class)
    List<CommonGiftBean> hobbyList;

    @Generated(hash = 1195446905)
    public CommonGift(Long userId, List<CommonGiftBean> hobbyList) {
        this.userId = userId;
        this.hobbyList = hobbyList;
    }

    @Generated(hash = 1299480660)
    public CommonGift() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CommonGiftBean> getHobbyList() {
        return this.hobbyList;
    }

    public void setHobbyList(List<CommonGiftBean> hobbyList) {
        this.hobbyList = hobbyList;
    }

   
}
