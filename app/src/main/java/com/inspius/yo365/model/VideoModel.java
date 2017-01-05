package com.inspius.yo365.model;

import android.text.TextUtils;

import com.inspius.yo365.api.AppRestClient;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.helper.VideoUtil;
import com.inspius.yo365.helper.YouTubeUrlParser;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class VideoModel implements Serializable {
    private VideoJSON videoJSON;
    private String thumbnail;
    private String viewCounter1;
    private String viewCounter2;
    private CategoryJSON categoryJSON;
    private String videoPath;

    private String series;
    private AppConstant.VIDEO_TYPE videoType = AppConstant.VIDEO_TYPE.UPLOAD;

    public VideoModel(VideoJSON videoJSON) {
        this.videoJSON = videoJSON;

        this.viewCounter1 = VideoUtil.getStatsFormat(String.valueOf(videoJSON.statsJSON.views));
        this.viewCounter2 = String.format("%s views", viewCounter1);
        this.series = videoJSON.series;

        if (TextUtils.isEmpty(series))
            series = "No Series";

        if (!TextUtils.isEmpty(videoJSON.videoLinkJSON.type))
            videoType = AppConstant.VIDEO_TYPE.fromString(videoJSON.videoLinkJSON.type);

        if (!TextUtils.isEmpty(videoJSON.urlImage))
            this.thumbnail = videoJSON.urlImage;
        else if (videoType == AppConstant.VIDEO_TYPE.YOUTUBE)
            this.thumbnail = YouTubeUrlParser.getThumbnailPath(videoJSON.videoLinkJSON.url);

        this.categoryJSON = VideoUtil.getCategoryByID(videoJSON.categoryId);

        videoPath = videoJSON.videoLinkJSON.url;
        if (videoType == AppConstant.VIDEO_TYPE.VIMEO)
            videoPath = String.format(AppConstant.RELATIVE_URL_PLAY_VIMEO, videoPath);

        if (videoType == AppConstant.VIDEO_TYPE.FACEBOOK)
            videoPath = String.format(AppRestClient.getAbsoluteUrl(AppConstant.RELATIVE_URL_PLAY_FACEBOOK), videoPath);
    }

    public String getTitle() {
        return videoJSON.title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getViewCounterNumberFormat() {
        return viewCounter1;
    }

    public String getViewCounterStringFormat() {
        return viewCounter2;
    }

    public String getTimeRemain() {
        return videoJSON.videoLinkJSON.length;
    }


    public String getVideoPath() {
        return videoPath;
    }

    public AppConstant.VIDEO_TYPE getVideoType() {
        return videoType;
    }

    public int getVideoId() {
        return videoJSON.id;
    }

    public String getUpdateAt() {
        return videoJSON.updateAt;
    }

    public String getCategoryName() {
        if (categoryJSON != null)
            return categoryJSON.name;
        return "unknown";
    }

    public String getSeries() {
        return series;
    }

    public String getDescription() {
        if (!TextUtils.isEmpty(videoJSON.description))
            return videoJSON.description;

        return "No Description Available";
    }

    public String getAuthor() {
        if (!TextUtils.isEmpty(videoJSON.author))
            return videoJSON.author;

        return "unknown";
    }

    public String getSocialLink() {
        if (!TextUtils.isEmpty(videoJSON.urlSocial))
            return videoJSON.urlSocial;
        return getVideoPath();
    }
}
