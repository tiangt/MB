package com.whzl.mengbi.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.whzl.mengbi.greendao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, Long.class, "userId", true, "_id");
        public final static Property Avatar = new Property(1, String.class, "avatar", false, "AVATAR");
        public final static Property Nickname = new Property(2, String.class, "nickname", false, "NICKNAME");
        public final static Property SeesionId = new Property(3, String.class, "seesionId", false, "SEESION_ID");
        public final static Property Recharged = new Property(4, Boolean.class, "recharged", false, "RECHARGED");
    }

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: userId
                "\"AVATAR\" TEXT," + // 1: avatar
                "\"NICKNAME\" TEXT," + // 2: nickname
                "\"SEESION_ID\" TEXT," + // 3: seesionId
                "\"RECHARGED\" INTEGER);"); // 4: recharged
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(2, avatar);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String seesionId = entity.getSeesionId();
        if (seesionId != null) {
            stmt.bindString(4, seesionId);
        }
 
        Boolean recharged = entity.getRecharged();
        if (recharged != null) {
            stmt.bindLong(5, recharged ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(2, avatar);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String seesionId = entity.getSeesionId();
        if (seesionId != null) {
            stmt.bindString(4, seesionId);
        }
 
        Boolean recharged = entity.getRecharged();
        if (recharged != null) {
            stmt.bindLong(5, recharged ? 1L: 0L);
        }
    }

    @Override
    protected final void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // avatar
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // seesionId
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0 // recharged
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAvatar(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSeesionId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRecharged(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
