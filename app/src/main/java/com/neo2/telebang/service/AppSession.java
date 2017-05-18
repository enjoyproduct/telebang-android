package com.neo2.telebang.service;

import com.neo2.telebang.model.DataCategoryJSON;

import java.util.ArrayList;

/**
 * Created by Billy on 1/11/16.
 */
public class AppSession {
    private static AppSession mInstance;

    public static synchronized AppSession getInstance() {
        if (mInstance == null)
            mInstance = new AppSession();

        return mInstance;
    }

    private static DataCategoryJSON categoryData;

    public DataCategoryJSON getCategoryData() {
        if (categoryData == null) {
            categoryData = new DataCategoryJSON();
            categoryData.listCategory = new ArrayList<>();
            categoryData.listIdTopCategory = new ArrayList<>();
        }

        return categoryData;
    }

    public void setCategoryData(DataCategoryJSON categoryData) {
        AppSession.categoryData = categoryData;
    }
}
