package com.whzl.mengbi.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.whzl.mengbi.greendao.UsualGift;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USUAL_GIFT".
*/
public class UsualGiftDao extends AbstractDao<UsualGift, Long> {

    public static final String TABLENAME = "USUAL_GIFT";

    /**
     * Properties of entity UsualGift.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GiftId = new Property(1, Long.class, "giftId", false, "GIFT_ID");
        public final static Property Times = new Property(2, Long.class, "times", false, "TIMES");
        public final static Property UserId = new Property(3, Long.class, "userId", false, "USER_ID");
    }

    private Query<UsualGift> user_UsualGiftListQuery;

    public UsualGiftDao(DaoConfig config) {
        super(config);
    }
    
    public UsualGiftDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USUAL_GIFT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"GIFT_ID\" INTEGER," + // 1: giftId
                "\"TIMES\" INTEGER," + // 2: times
                "\"USER_ID\" INTEGER);"); // 3: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USUAL_GIFT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UsualGift entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long giftId = entity.getGiftId();
        if (giftId != null) {
            stmt.bindLong(2, giftId);
        }
 
        Long times = entity.getTimes();
        if (times != null) {
            stmt.bindLong(3, times);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(4, userId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UsualGift entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long giftId = entity.getGiftId();
        if (giftId != null) {
            stmt.bindLong(2, giftId);
        }
 
        Long times = entity.getTimes();
        if (times != null) {
            stmt.bindLong(3, times);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(4, userId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UsualGift readEntity(Cursor cursor, int offset) {
        UsualGift entity = new UsualGift( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // giftId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // times
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UsualGift entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGiftId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTimes(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UsualGift entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UsualGift entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UsualGift entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "usualGiftList" to-many relationship of User. */
    public List<UsualGift> _queryUser_UsualGiftList(Long userId) {
        synchronized (this) {
            if (user_UsualGiftListQuery == null) {
                QueryBuilder<UsualGift> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.UserId.eq(null));
                user_UsualGiftListQuery = queryBuilder.build();
            }
        }
        Query<UsualGift> query = user_UsualGiftListQuery.forCurrentThread();
        query.setParameter(0, userId);
        return query.list();
    }

}
