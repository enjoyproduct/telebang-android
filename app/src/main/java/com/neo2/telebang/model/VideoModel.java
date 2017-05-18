package com.neo2.telebang.model;

import android.text.TextUtils;

import com.neo2.telebang.api.AppRestClient;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.helper.VideoUtil;
import com.neo2.telebang.helper.YouTubeUrlParser;

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
        this.series = "No Series";
        if (videoJSON.series != null && !TextUtils.isEmpty(videoJSON.series.title))
            this.series = videoJSON.series.title;

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
        if (categoryJSON != null && !TextUtils.isEmpty(categoryJSON.name))
            return categoryJSON.name;

        return "unknown";
    }
    public int getVipPlay()
    {
          return videoJSON.vipPlay;
    }
    public int getVip()
    {
        return videoJSON.author.vip;
    }
    public String getSeries() {
        return series;
    }

    public String getDescription() {
        if (!TextUtils.isEmpty(videoJSON.description))
            return videoJSON.description;

        return "No Description Available";
    }

    public String getAuthorName() {
        if (videoJSON.author != null)
            return videoJSON.author.getFullName();

        return "Guest";
    }

    public CustomerJSON getAuthor() {
        return videoJSON.author;
    }

    public String getSocialLink() {
        if (!TextUtils.isEmpty(videoJSON.urlSocial))
            return videoJSON.urlSocial;
        return getVideoPath();
    }
}
