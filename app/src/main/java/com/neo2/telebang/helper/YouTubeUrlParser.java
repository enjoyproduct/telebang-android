package com.neo2.telebang.helper;

import android.support.annotation.NonNull;

import com.neo2.telebang.app.AppConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Billy on 10/27/15.
 */
public class YouTubeUrlParser {
    // (?:youtube(?:-nocookie)?\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})
    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getVideoId(@NonNull String videoUrl) {
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return videoUrl;
    }

    public static String getVideoUrl(@NonNull String videoId) {
        return "http://youtu.be/" + videoId;
    }

    public static String getThumbnailPath(@NonNull String youtubePath) {
        return String.format(AppConstant.URL_YOUTUBE_THUMBNAIL, getVideoId(youtubePath));
    }
}
