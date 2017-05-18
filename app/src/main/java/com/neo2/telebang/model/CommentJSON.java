package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class CommentJSON implements Serializable {
    public int id;

    @JsonProperty("comment_text")
    public String commentText;

    @JsonProperty("create_at")
    public long createAt;

    @JsonProperty("video_id")
    public long videoId;

    @JsonProperty("user")
    public CustomerJSON customerJSON;
}
