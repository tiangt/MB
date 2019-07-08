package com.whzl.mengbi.greendao;

import android.os.Parcel;
import android.os.Parcelable;

import com.whzl.mengbi.gen.DaoSession;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * @author nobody
 * @date 2019/2/13
 */
@Entity
public class PrivateChatUser implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private Long privateUserId;

    String name;
    String avatar;
    private Long timestamp;
    private Long uncheckTime = 0L;
    String lastMessage;
    Boolean isAnchor = false;
    Integer anchorLevel;
    Integer userLevel;

    @ToMany(referencedJoinProperty = "privateUserId")
    private List<PrivateChatContent> privateChatContents;

    private Long userId;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1072743205)
    private transient PrivateChatUserDao myDao;

    @Generated(hash = 489578847)
    public PrivateChatUser(Long id, Long privateUserId, String name, String avatar, Long timestamp,
            Long uncheckTime, String lastMessage, Boolean isAnchor, Integer anchorLevel,
            Integer userLevel, Long userId) {
        this.id = id;
        this.privateUserId = privateUserId;
        this.name = name;
        this.avatar = avatar;
        this.timestamp = timestamp;
        this.uncheckTime = uncheckTime;
        this.lastMessage = lastMessage;
        this.isAnchor = isAnchor;
        this.anchorLevel = anchorLevel;
        this.userLevel = userLevel;
        this.userId = userId;
    }

    @Generated(hash = 747997055)
    public PrivateChatUser() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrivateUserId() {
        return this.privateUserId;
    }

    public void setPrivateUserId(Long privateUserId) {
        this.privateUserId = privateUserId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUncheckTime() {
        return this.uncheckTime;
    }

    public void setUncheckTime(Long uncheckTime) {
        this.uncheckTime = uncheckTime;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1713562867)
    public List<PrivateChatContent> getPrivateChatContents() {
        if (privateChatContents == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PrivateChatContentDao targetDao = daoSession.getPrivateChatContentDao();
            List<PrivateChatContent> privateChatContentsNew = targetDao
                    ._queryPrivateChatUser_PrivateChatContents(id);
            synchronized (this) {
                if (privateChatContents == null) {
                    privateChatContents = privateChatContentsNew;
                }
            }
        }
        return privateChatContents;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 875079116)
    public synchronized void resetPrivateChatContents() {
        privateChatContents = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 181671466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPrivateChatUserDao() : null;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Boolean getIsAnchor() {
        return this.isAnchor;
    }

    public void setIsAnchor(Boolean isAnchor) {
        this.isAnchor = isAnchor;
    }

    public Integer getAnchorLevel() {
        return this.anchorLevel;
    }

    public void setAnchorLevel(Integer anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public Integer getUserLevel() {
        return this.userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.privateUserId);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeValue(this.timestamp);
        dest.writeValue(this.uncheckTime);
        dest.writeString(this.lastMessage);
        dest.writeValue(this.isAnchor);
        dest.writeValue(this.anchorLevel);
        dest.writeValue(this.userLevel);
        dest.writeValue(this.userId);
    }

    protected PrivateChatUser(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.privateUserId = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.avatar = in.readString();
        this.timestamp = (Long) in.readValue(Long.class.getClassLoader());
        this.uncheckTime = (Long) in.readValue(Long.class.getClassLoader());
        this.lastMessage = in.readString();
        this.isAnchor = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.anchorLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<PrivateChatUser> CREATOR = new Parcelable.Creator<PrivateChatUser>() {
        @Override
        public PrivateChatUser createFromParcel(Parcel source) {
            return new PrivateChatUser(source);
        }

        @Override
        public PrivateChatUser[] newArray(int size) {
            return new PrivateChatUser[size];
        }
    };
}
