package com.inspius.yo365.helper;

import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.service.AppSession;

import java.text.DecimalFormat;

/**
 * Created by Billy on 12/27/16.
 */

public class VideoUtil {
    public static String getStatsFormat(String value) {
        return getStatsFormat(Long.valueOf(value));
    }

    public static String getStatsFormat(long value) {
        try {
            DecimalFormat digitformat = new DecimalFormat("###,###,###,###");
            return digitformat.format(value);
        } catch (NumberFormatException numberFormatExp) {
            return String.valueOf(value);
        }
    }

    public static CategoryJSON getCategoryByID(long categoryID) {
        for (CategoryJSON category : AppSession.getInstance().getCategoryData().listCategory) {
            if (categoryID == category.id)
                return category;

        }

        return null;
    }
}
