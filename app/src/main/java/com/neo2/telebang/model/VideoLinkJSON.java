package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoLinkJSON implements Serializable {
    public String url;
    public String type;
    public String length;
    public String subtitle;
}
