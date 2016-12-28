package com.inspius.yo365.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Billy on 1/6/16.
 */
public class DataCategoryJSON {
    @JsonProperty("all_category")
    public List<CategoryJSON> listCategory;

    @JsonProperty("top_category")
    public List<Long> listIdTopCategory;
}
