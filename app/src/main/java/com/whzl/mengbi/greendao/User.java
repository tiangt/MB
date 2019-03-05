package com.whzl.mengbi.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.whzl.mengbi.gen.DaoSession;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.gen.UsualGiftDao;

/**
 * @author nobody
 * @date 2019/2/13
 */
@Entity
public class User {
    @Id
    Long userId;

    String avatar;
    String nickname;
    String seesionId;
    Boolean recharged;

    @ToMany(referencedJoinProperty = "userId")
    private List<PrivateChatUser> privateChatUserList;

    @ToMany(referencedJoinProperty = "userId")
    private List<UsualGift> usualGiftList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 1426594685)
    public User(Long userId, String avatar, String nickname, String seesionId,
            Boolean recharged) {
        this.userId = userId;
        this.avatar = avatar;
        this.nickname = nickname;
        this.seesionId = seesionId;
        this.recharged = recharged;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Boolean getRecharged() {
        return this.recharged;
    }

    public void setRecharged(Boolean recharged) {
        this.recharged = recharged;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1741710909)
    public List<PrivateChatUser> getPrivateChatUserList() {
        if (privateChatUserList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PrivateChatUserDao targetDao = daoSession.getPrivateChatUserDao();
            List<PrivateChatUser> privateChatUserListNew = targetDao
                    ._queryUser_PrivateChatUserList(userId);
            synchronized (this) {
                if (privateChatUserList == null) {
                    privateChatUserList = privateChatUserListNew;
                }
            }
        }
        return privateChatUserList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1275855340)
    public synchronized void resetPrivateChatUserList() {
        privateChatUserList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 264349507)
    public List<UsualGift> getUsualGiftList() {
        if (usualGiftList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsualGiftDao targetDao = daoSession.getUsualGiftDao();
            List<UsualGift> usualGiftListNew = targetDao
                    ._queryUser_UsualGiftList(userId);
            synchronized (this) {
                if (usualGiftList == null) {
                    usualGiftList = usualGiftListNew;
                }
            }
        }
        return usualGiftList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1612399897)
    public synchronized void resetUsualGiftList() {
        usualGiftList = null;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }


}
