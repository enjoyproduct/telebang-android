package com.neo2.telebang.app;

import com.inspius.coreapp.config.InspiusConfig;

/**
 * Created by Billy on 9/3/15.
 */
public class AppConfig {

    /**
     * Change ENVIRONMENT to PRODUCTION for release app
     */
    public static final InspiusConfig.Environment ENVIRONMENT = InspiusConfig.Environment.DEVELOPMENT;

    /**
     * replace to your server
     */
    public static final String BASE_URL_PRODUCTION = "http://telebang.com/index.php";
    public static final String BASE_URL_DEVELOPMENT = "http://telebang.com/index.php";

//    public static final String BASE_URL_PRODUCTION = "http://192.168.2.17/telebang/index.php";
//    public static final String BASE_URL_DEVELOPMENT = "http://192.168.2.17/telebang/index.php";

    public static final String URL_PAGE_TERM = "https://codecanyon.net/item/yo365-smart-network-of-videos/19275244";
    public static final String URL_PAGE_HELP = "http://inspius.com/envato/forums/forum/android/yo365-smart-network-of-videos/";
    public static final String URL_PAGE_ABOUT = "https://codecanyon.net/item/yo365-smart-network-of-videos/19275244";
    public static final String URL_PAGE_UPLOAD = "http://store.inspius.com/downloads/yo365-video-upload-module-android/";
    public static final String URL_PAGE_NEWS = "http://store.inspius.com/downloads/yo365-news-blog-module-for-android/";

    /**
     * Splash loading duration
     */
    public static long SPLASH_DURATION = 1000;

    /**
     * Change SHOW_ADS_INTERSTITIAL
     * true : show ads
     * false : hide ads
     */
    public static final boolean SHOW_ADS_BANNER = true;
    public static final boolean SHOW_ADS_INTERSTITIAL = false;

    /**
     * CONFIG
     */
    public static final boolean IS_SHOW_INTRO = true; // show intro app

    public static final AppConstant.YO_SCREEN HOME_SCREEN = AppConstant.YO_SCREEN.HOME_2; // DEFAULT, HOME_1, HOME_3
    public static final AppConstant.YO_SCREEN VIDEO_CATEGORIES_SCREEN = AppConstant.YO_SCREEN.VIDEO_CATEGORIES_2; // DEFAULT, VIDEO_CATEGORIES_1, VIDEO_CATEGORIES_2, VIDEO_CATEGORIES_TREE_1, VIDEO_CATEGORIES_TREE_2, VIDEO_CATEGORIES_TREE_EXPAND

    // Module
    public static final AppConstant.YO_MODULE VIDEO_DETAIL_MODULE = AppConstant.YO_MODULE.DEFAULT; // DEFAULT, VIDEO_DETAIL_JW
    public static final AppConstant.YO_MODULE NEWS_MODULE = AppConstant.YO_MODULE.NEWS_1; // DEFAULT, NEWS_1
    public static final AppConstant.YO_MODULE UPLOAD_MODULE = AppConstant.YO_MODULE.UPLOAD_VIDEO_1; // DEFAULT, UPLOAD_VIDEO_1
    public static final AppConstant.YO_MODULE SERIES_MODULE = AppConstant.YO_MODULE.SERIES_1; // DEFAULT, SERIES_1

    //Login
    public static final boolean IS_REQUIRE_LOGIN = false; // show intro app
}
