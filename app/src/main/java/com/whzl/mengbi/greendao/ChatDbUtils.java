package com.whzl.mengbi.greendao;

import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.SPUtils;

import java.util.List;

/**
 * @author nobody
 * @date 2019-07-08
 */
public class ChatDbUtils {
    private static ChatDbUtils dbUtils;

    public static ChatDbUtils getInstance() {
        if (dbUtils == null) {
            synchronized (ChatDbUtils.class) {
                if (dbUtils == null) {
                    dbUtils = new ChatDbUtils();
                }
            }
        }
        return dbUtils;
    }

    /**
     * 更新聊天列表
     */
    public void updatePrivateChatUser(long mUserId, PrivateChatUser user) {
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        PrivateChatUser privateChatUser = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.eq(mUserId),
                PrivateChatUserDao.Properties.PrivateUserId.eq(user.getPrivateUserId())).unique();
        if (privateChatUser == null) {
            privateChatUserDao.insert(user);
        } else {
//            privateChatUser.setTimestamp(user.getTimestamp());
//            privateChatUser = user;
            user.setId(privateChatUser.getId());
            long uncheckTime = privateChatUser.getUncheckTime();
            if (uncheckTime == 99) {
                user.setUncheckTime((long) 99);
            } else {
                user.setUncheckTime(++uncheckTime);
            }
//            user.setId(privateChatUser.getId());
//            privateChatUser.setLastMessage(user.getLastMessage());
            privateChatUserDao.update(user);
        }
    }


    /**
     * 删除聊天列表记录
     */
    public void deleteChatUser(PrivateChatUser privateChatUser) {
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        privateChatUserDao.deleteByKey(privateChatUser.getId());
        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        List<PrivateChatContent> list = privateChatContentDao.queryBuilder().where(
                PrivateChatContentDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatContentDao.Properties.PrivateUserId.eq(privateChatUser.getPrivateUserId())).list();
        for (int i = 0; i < list.size(); i++) {
            privateChatContentDao.deleteByKey(list.get(i).getId());
        }
    }

    public List<PrivateChatUser> queryChatUsetList() {
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        List<PrivateChatUser> privateChatUsers = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()))).list();
        return privateChatUsers;
    }

    /**
     * 新增用户
     */
    public void insertUser(long mUserId, User user) {
        UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
        User unique = userDao.queryBuilder().where(UserDao.Properties.UserId.eq(mUserId)).unique();
        if (unique == null) {
            userDao.insert(user);
        }
    }

    /**
     * 新增聊天信息
     */
    public void insertChatContent(PrivateChatContent chatContent) {
        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        List<PrivateChatContent> list = privateChatContentDao.queryBuilder().where(
                PrivateChatContentDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatContentDao.Properties.PrivateUserId.eq(chatContent.getPrivateUserId())).list();
        if (list != null && list.size() > AppUtils.PRIVATE_MAX_NUM) {
            privateChatContentDao.deleteByKey(list.get(0).getId());
        }
        privateChatContentDao.insert(chatContent);
    }

    /**
     * 查找聊天信息
     */
    public List<PrivateChatContent> queryChatContent(long privateUserId) {
        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        List<PrivateChatContent> list = privateChatContentDao.queryBuilder().where(
                PrivateChatContentDao.Properties.UserId.eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatContentDao.Properties.PrivateUserId.eq(privateUserId)).list();
        return list;
    }

}
