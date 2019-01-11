package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author nobody
 * @date 2019/1/10
 */
@Entity
public class CommonGift {
    @Id
    Long userId;

    @Convert(columnType = String.class, converter = CommonGift_Converter.class)
    List<CommonGiftBean> hobbyList = new ArrayList<>();

    String avatar;
    String nickname;
    String seesionId;
    boolean recharged;
    @Generated(hash = 1169883968)
    public CommonGift(Long userId, List<CommonGiftBean> hobbyList, String avatar,
            String nickname, String seesionId, boolean recharged) {
        this.userId = userId;
        this.hobbyList = hobbyList;
        this.avatar = avatar;
        this.nickname = nickname;
        this.seesionId = seesionId;
        this.recharged = recharged;
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
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getSeesionId() {
        return this.seesionId;
    }
    public void setSeesionId(String seesionId) {
        this.seesionId = seesionId;
    }
    public boolean getRecharged() {
        return this.recharged;
    }
    public void setRecharged(boolean recharged) {
        this.recharged = recharged;
    }

}
