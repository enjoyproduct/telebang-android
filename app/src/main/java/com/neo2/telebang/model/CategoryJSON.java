package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Billy on 1/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryJSON implements Serializable{
    public int id;
    public String image;
    public String icon;
    public String name;
    public int enable;

    @JsonProperty("parentID")
    public int parentID;
}
