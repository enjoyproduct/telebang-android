package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Billy on 1/5/16.
 */
public class SeriesJSON implements Serializable {
    @JsonProperty("id")
    public int id;

    public String thumbnail;
    public String title;

    @JsonProperty("short_description")
    public String shortDesc;

    public int completed;
}
