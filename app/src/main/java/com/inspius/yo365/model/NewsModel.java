package com.inspius.yo365.model;

import com.inspius.yo365.helper.TimeUtil;
import com.inspius.yo365.helper.VideoUtil;

/**
 * Created by Billy on 12/31/16.
 */

public class NewsModel {
    private NewsJSON newsJSON;
    private String updateAt;

    public NewsModel(NewsJSON newsJSON) {
        this.newsJSON = newsJSON;

        updateAt = TimeUtil.getDateTimeFormat(newsJSON.updateAt);
    }

    public String getUpdateAt() {
        return updateAt;
    }


    public String getThumbnail() {
        return newsJSON.thumbnail;
    }


    public String getTitle() {
        return newsJSON.title;
    }

    public String getViewCounter() {
        return String.format("%s views", VideoUtil.getStatsFormat(newsJSON.viewCounter));
    }
}
