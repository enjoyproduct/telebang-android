package com.inspius.yo365.helper;

import android.support.annotation.NonNull;

import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.service.AppSession;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getYoutubeIdByUrl(@NonNull String videoUrl) {
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }
}
