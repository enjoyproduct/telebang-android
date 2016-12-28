package com.inspius.yo365.helper;

import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.service.AppSession;

import java.text.DecimalFormat;

/**
 * Created by Billy on 12/27/16.
 */

public class VideoUtil {
    public static String getStatsFormat(String value) {
        try {
            DecimalFormat digitformat = new DecimalFormat("###,###,###,###");
            return digitformat.format(Long.valueOf(value));
        } catch (NumberFormatException numberFormatExp) {
            return value;
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
