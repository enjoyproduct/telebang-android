package com.inspius.yo365.manager;

import com.inspius.yo365.greendao.DBKeyword;
import com.inspius.yo365.greendao.DBVideoDownload;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 11/11/16.
 */

public interface IDatabaseManager {

    /**
     * WishList
     */
    List<DBWishListVideo> listVideoAtWishList(int page, int userID);

    DBWishListVideo insertVideoToWishList(int userID, VideoModel videoModel);

    DBWishListVideo getVideoWithListByVideoID(int userID, int videoID);

    void deleteVideoAtWishListByVideoId(int userID, int videoId);

    void deleteVideoWishList(Long id);

    /**
     * Download Videos
     */

    DBVideoDownload insertVideoDownload(VideoModel videoModel, String videoPath);

    List<DBVideoDownload> getVideosDownload(int page);

    List<DBVideoDownload> getVideosDownload();

    DBVideoDownload getVideoDownloadByVideoID(int videoID);

    void deleteVideoDownloadByID(long id);

    /**
     * Keyword
     *
     * @param keyword
     * @return
     */
    DBKeyword insertKeyword(String keyword);

    List<DBKeyword> getListKeyword(int limit);
}
