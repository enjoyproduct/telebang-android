package com.inspius.yo365.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.greendao.DBVideoDownload;

import com.inspius.yo365.greendao.DBWishListVideoDao;
import com.inspius.yo365.greendao.DBVideoDownloadDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dBWishListVideoDaoConfig;
    private final DaoConfig dBVideoDownloadDaoConfig;

    private final DBWishListVideoDao dBWishListVideoDao;
    private final DBVideoDownloadDao dBVideoDownloadDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dBWishListVideoDaoConfig = daoConfigMap.get(DBWishListVideoDao.class).clone();
        dBWishListVideoDaoConfig.initIdentityScope(type);

        dBVideoDownloadDaoConfig = daoConfigMap.get(DBVideoDownloadDao.class).clone();
        dBVideoDownloadDaoConfig.initIdentityScope(type);

        dBWishListVideoDao = new DBWishListVideoDao(dBWishListVideoDaoConfig, this);
        dBVideoDownloadDao = new DBVideoDownloadDao(dBVideoDownloadDaoConfig, this);

        registerDao(DBWishListVideo.class, dBWishListVideoDao);
        registerDao(DBVideoDownload.class, dBVideoDownloadDao);
    }
    
    public void clear() {
        dBWishListVideoDaoConfig.clearIdentityScope();
        dBVideoDownloadDaoConfig.clearIdentityScope();
    }

    public DBWishListVideoDao getDBWishListVideoDao() {
        return dBWishListVideoDao;
    }

    public DBVideoDownloadDao getDBVideoDownloadDao() {
        return dBVideoDownloadDao;
    }

}
