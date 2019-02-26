package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;

import com.whzl.mengbi.gen.DaoSession;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;

/**
 * @author nobody
 * @date 2019/2/13
 */
@Entity
public class PrivateChatUser {
    @Id(autoincrement = true)
    private Long id;
    private Long privateUserId;

    String name;
    String avatar;
    private Long timestamp;
    private Long uncheckTime = 0L;

    @ToMany(referencedJoinProperty = "privateUserId")
    private List<PrivateChatContent> privateChatContents;

    private Long userId;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1072743205)
    private transient PrivateChatUserDao myDao;

    @Generated(hash = 1122182842)
    public PrivateChatUser(Long id, Long privateUserId, String name, String avatar,
            Long timestamp, Long uncheckTime, Long userId) {
        this.id = id;
        this.privateUserId = privateUserId;
        this.name = name;
        this.avatar = avatar;
        this.timestamp = timestamp;
        this.uncheckTime = uncheckTime;
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 181671466)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPrivateChatUserDao() : null;
    }

}
