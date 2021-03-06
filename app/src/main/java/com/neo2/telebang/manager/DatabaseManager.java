package com.neo2.telebang.manager;

import android.content.Context;

import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.app.GlobalApplication;
import com.neo2.telebang.greendao.DBKeyword;
import com.neo2.telebang.greendao.DBKeywordDao;
import com.neo2.telebang.greendao.DBVideoDownload;
import com.neo2.telebang.greendao.DBVideoDownloadDao;
import com.neo2.telebang.greendao.DBWishListVideo;
import com.neo2.telebang.greendao.DBWishListVideoDao;
import com.neo2.telebang.greendao.DaoMaster;
import com.neo2.telebang.greendao.DaoSession;
import com.neo2.telebang.model.VideoModel;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Billy on 11/11/16.
 */

public class DatabaseManager implements IDatabaseManager {
    /**
     * Class tag. Used for debug.
     */
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    /**
     * Instance of DatabaseManager
     */
    private static DatabaseManager instance;

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    private DaoSession daoSession;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     */
    public DatabaseManager() {
        this.context = GlobalApplication.getAppContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ENCRYPTED ? "yo365-db-encrypted" : "yo-365-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("inspius") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * @return this.instance
     */
    public static IDatabaseManager getInstance() {

        if (instance == null)
            instance = new DatabaseManager();

        return instance;
    }

    @Override
    public List<DBWishListVideo> listVideoAtWishList(int page, int userID) {
        DBWishListVideoDao dao = daoSession.getDBWishListVideoDao();

        if (page == 0)
            page = 1;

        int limit = AppConstant.LIMIT_WISH_LIST;
        int offset = (page - 1) * limit;

        QueryBuilder<DBWishListVideo> queryBuilder = dao.queryBuilder().orderDesc(DBWishListVideoDao.Properties.Id).where(DBWishListVideoDao.Properties.UserID.eq(userID)).limit(limit).offset(offset);
        return queryBuilder.list();
    }

    @Override
    public DBWishListVideo insertVideoToWishList(int userID, VideoModel videoModel) {
        DBWishListVideoDao dao = daoSession.getDBWishListVideoDao();

        DBWishListVideo entity = new DBWishListVideo();
        entity.setVideoID(videoModel.getVideoId());
        entity.setVideoName(videoModel.getTitle());
        entity.setVideoCategory(videoModel.getCategoryName());
        entity.setVideoCreateAt(videoModel.getUpdateAt());
        entity.setVideoThumbnail(videoModel.getThumbnail());
        entity.setUserID(userID);

        long id = dao.insert(entity);
        if (id > 0) {
            entity.setId(id);

            return entity;
        }

        return null;
    }

    @Override
    public DBWishListVideo getVideoWithListByVideoID(int userID, int videoID) {
        DBWishListVideoDao dao = daoSession.getDBWishListVideoDao();
        QueryBuilder<DBWishListVideo> queryBuilder = dao.queryBuilder().orderDesc(DBWishListVideoDao.Properties.Id).where(DBWishListVideoDao.Properties.UserID.eq(userID), DBWishListVideoDao.Properties.VideoID.eq(videoID)).limit(1);
        return queryBuilder.unique();
    }

    @Override
    public void deleteVideoAtWishListByVideoId(int userID, int videoId) {
        DBWishListVideoDao dao = daoSession.getDBWishListVideoDao();
        QueryBuilder<DBWishListVideo> queryBuilder = dao.queryBuilder().where(DBWishListVideoDao.Properties.UserID.eq(userID), DBWishListVideoDao.Properties.VideoID.eq(videoId));
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void deleteVideoWishList(Long id) {
        DBWishListVideoDao dao = daoSession.getDBWishListVideoDao();
        dao.deleteByKey(id);
    }

    @Override
    public List<DBVideoDownload> getVideosDownload(int page) {

        int limit = 20;

        if (page < 1)
            page = 1;
        int offset = (page - 1) * limit;

        DBVideoDownloadDao dao = daoSession.getDBVideoDownloadDao();
        QueryBuilder<DBVideoDownload> queryBuilder = dao.queryBuilder().orderDesc(DBVideoDownloadDao.Properties.Id).limit(limit).offset(offset);

        return queryBuilder.list();
    }


    @Override
    public List<DBVideoDownload> getVideosDownload() {
        DBVideoDownloadDao dao = daoSession.getDBVideoDownloadDao();
        QueryBuilder<DBVideoDownload> queryBuilder = dao.queryBuilder().orderDesc(DBVideoDownloadDao.Properties.Id);

        return queryBuilder.list();
    }

    @Override
    public DBVideoDownload insertVideoDownload(VideoModel videoModel, String videoPath) {
        DBVideoDownload entity = new DBVideoDownload();
        entity.setVideoID(videoModel.getVideoId());
        entity.setVideoCategory(videoModel.getCategoryName());
        entity.setVideoCreateAt(videoModel.getUpdateAt());
        entity.setVideoName(videoModel.getTitle());
        entity.setVideoPath(videoPath);

        DBVideoDownloadDao dao = daoSession.getDBVideoDownloadDao();

        long id = dao.insert(entity);
        if (id > 0) {
            entity.setId(id);

            return entity;
        }

        return null;
    }

    @Override
    public DBVideoDownload getVideoDownloadByVideoID(int videoID) {
        DBVideoDownloadDao dao = daoSession.getDBVideoDownloadDao();
        QueryBuilder<DBVideoDownload> queryBuilder = dao.queryBuilder().where(DBVideoDownloadDao.Properties.VideoID.eq(videoID)).limit(1);
        return queryBuilder.unique();
    }

    @Override
    public void deleteVideoDownloadByID(long id) {
        DBVideoDownloadDao dao = daoSession.getDBVideoDownloadDao();
        dao.deleteByKey(id);
    }

    @Override
    public DBKeyword insertKeyword(String keyword) {
        DBKeyword entity = new DBKeyword();
        entity.setKeyword(keyword);

        DBKeywordDao dao = daoSession.getDBKeywordDao();

        long id = dao.insert(entity);
        if (id > 0) {
            entity.setId(id);

            return entity;
        }

        return null;
    }

    @Override
    public List<DBKeyword> getListKeyword(int limit) {
        DBKeywordDao dao = daoSession.getDBKeywordDao();
        if (dao.count() > 100)
            dao.deleteAll();

        QueryBuilder<DBKeyword> queryBuilder = dao.queryBuilder().orderDesc(DBKeywordDao.Properties.Id).limit(limit);
        return queryBuilder.list();
    }
}
