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

    /**
     * 获取单例
     */
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
            privateChatUser.setTimestamp(user.getTimestamp());
            privateChatUser.setId(privateChatUser.getId());
            privateChatUserDao.update(privateChatUser);
        }
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
}
