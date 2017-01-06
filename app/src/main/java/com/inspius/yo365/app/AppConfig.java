package com.inspius.yo365.app;

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
    public static final String BASE_URL_PRODUCTION = "http://test.inspius.com/yovideo/index.php";
    public static final String BASE_URL_DEVELOPMENT = "http://test.inspius.com/yovideo/index.php";

    public static final String URL_PAGE_TERM = "https://codecanyon.net/item/yovideo-social-network-of-video-android/14527631";
    public static final String URL_PAGE_HELP = "http://inspius.com/envato/forums/forum/android/";
    public static final String URL_PAGE_ABOUT = "http://inspius.com/";

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

    public static final AppConstant.YO_MODULE VIDEO_DETAIL_MODULE = AppConstant.YO_MODULE.VIDEO_DETAIL_JW;
    public static final AppConstant.YO_MODULE NEWS_MODULE = AppConstant.YO_MODULE.NEWS_1;
    public static final boolean IS_SHOW_INTRO = true; // show intro app

}
