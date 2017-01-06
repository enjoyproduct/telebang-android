package com.inspius.yo365.app;

import android.text.TextUtils;

/**
 * Created by Billy on 12/1/15.
 */
public class AppConstant {
    public static int RESPONSE_CODE_SUCCESS = 1;
    public static int LIMIT_VIDEOS_HOMES = 10;
    public static int LIMIT_VIDEO_COMMENT = 20;
    public static int LIMIT_VIDEO_SEARCH = 10;
    public static int LIMIT_NEWS = 15;
    public static int REQUEST_ALBUM_PIC = 1;

    public static final String KEY_NOTIFICATION_CONTENT_TYPE = "content_type";
    public static final String KEY_NOTIFICATION_CONTENT_ID = "content_id";
    public static final int REQUEST_NOTIFICATION_DETAIL = 112;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ACCESS_TOKEN = "access-token";
    public static final String KEY_VIDEO_ID = "video_id";
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_COMMENT_TEXT = "comment_text";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_OLD_PASS = "old_password";
    public static final String KEY_NEW_PASS = "new_password";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_FIELD = "field";

    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_PHONE_NUMBER = "phonenumber";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_ZIP = "zip";

    public static final String KEY_KEYWORD = "keyword";
    public static final String KEY_LIMIT = "limit";
    public static final String KEY_PAGE_NUMBER = "page";
    public static final String KEY_FIRST_OPEN_APP = "first-open-app";

    public static final String RELATIVE_URL_LOGIN = "/api/login";
    public static final String RELATIVE_URL_LOGIN_FACE_BOOK = "/api/loginFacebook";
    public static final String RELATIVE_URL_REGISTER = "/api/register";
    public static final String RELATIVE_URL_FORGOT_PASSWORD = "/api/forgot_password";
    public static final String RELATIVE_URL_CHANGE_PASSWORD = "/api/change_password";
    public static final String RELATIVE_URL_CHANGE_PROFILE = "/api/change_profile";
    public static final String RELATIVE_URL_CHANGE_AVATAR = "api/change_avatar";
    public static final String RELATIVE_URL_VIDEO_LATEST = "/api/getListVideoLasted/%s/%s";
    public static final String RELATIVE_URL_VIDEO_MOST = "/api/getListVideoMostView/%s/%s";
    public static final String RELATIVE_URL_VIDEO_TRENDING = "/api/getListVideoTrending/%s/%s";
    public static final String RELATIVE_URL_CATEGORIES = "/api/categories";
    public static final String RELATIVE_URL_GET_VIDEOS_BY_CATEGORY = "/api/getListVideoByCategory/%s/%s/%s";
    public static final String RELATIVE_URL_GET_USER_LIKE_STATUS = "/api/getLikeVideoStatus/%s/%s";
    public static final String RELATIVE_URL_USER_LIKE_VIDEO = "/api/likevideo";
    public static final String RELATIVE_URL_GET_VIDEO_BY_ID = "/api/getVideoById/%s";
    public static final String RELATIVE_URL_GET_VIDEO_COMMENTS = "/api/getListCommentVideo/%s/%s/%s";
    public static final String RELATIVE_URL_INSERT_COMMENT = "/api/insertCommentVideo";
    public static final String RELATIVE_URL_SEARCH_BY_KEYWORD = "/api/getListVideoByKeyword";
    public static final String RELATIVE_URL_UPDATE_VIDEO_COUNTER = "api/updateStatistics";
    public static final String RELATIVE_URL_PLAY_FACEBOOK = "/api/playFacebookVideo?video_url=%s";
    public static final String RELATIVE_URL_PLAY_VIMEO = "%s?player_id=player&autoplay=1&title=0&byline=0&portrait=0&api=1&maxheight=480&maxwidth=800";

    public static final String URL_YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/%s/hqdefault.jpg";

    public static final String KEY_BUNDLE_VIDEO = "video";
    public static final String KEY_BUNDLE_AUTO_PLAY = "auto_play";

    /**
     * Date, Time format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    /**
     * ENUM
     */
    public enum MENU_TYPE {
        HOME, CATEGORIES, WATCH_LIST, MY_ACCOUNT, ABOUT_US, TERM, FEEDBACK, HELP, SHARE, DOWNLOAD, NEWS
    }

    public enum LOGIN_TYPE {
        NOT_LOGIN, SYSTEM, FACEBOOK;
    }

    public enum VIDEO_TYPE {
        NONE("NONE"), UPLOAD("UPLOAD"), YOUTUBE("YOUTUBE"), VIMEO("VIMEO"), MP3("MP3"), FACEBOOK("FACEBOOK"), DAILY_MOTION("DAILY_MOTION"), JW_PLAYER("JW_PLAYER");

        private final String text;

        private VIDEO_TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static VIDEO_TYPE fromString(String text) {
            if (text != null) {
                for (VIDEO_TYPE b : VIDEO_TYPE.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return NONE;
        }
    }

    public enum YO_MODULE {
        DEFAULT("default"), VIDEO_DETAIL_JW("module-detail-jw"), NEWS_1("news_1"),;

        private final String text;

        private YO_MODULE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum COUNTER_FIELD {
        VIEW("view"), SHARE("share");

        private final String text;

        private COUNTER_FIELD(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public enum NOTIFICATION_TYPE {
        VIDEO("video"), NEWS("news"),;

        private final String text;

        private NOTIFICATION_TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static NOTIFICATION_TYPE fromString(String text) {
            if (!TextUtils.isEmpty(text)) {
                for (NOTIFICATION_TYPE b : NOTIFICATION_TYPE.values()) {
                    if (text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            return VIDEO;
        }
    }
}
