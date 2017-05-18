package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoJSON implements Serializable {
    public int id;

    @JsonProperty("url_social")
    public String urlSocial;

    @JsonProperty("category_id")
    public long categoryId;

    public SeriesJSON series;
    public CustomerJSON author;

    @JsonProperty("url_image")
    public String urlImage;

    public String description;
    public String title;

    @JsonProperty("update_at")
    public String updateAt;

    @JsonProperty("create_at")
    public String createAt;

    @JsonProperty("stats")
    public VideoStatsJSON statsJSON;

    @JsonProperty("video")
    public VideoLinkJSON videoLinkJSON;

    @JsonProperty("vip_play")
    public int vipPlay;

    @JsonProperty("another_category_ids")
    public Object anotherCategoryId;

    @JsonProperty("episode_no")
    public int episodeNo;
}
