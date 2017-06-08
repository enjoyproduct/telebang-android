package com.neo2.telebang.app;

import com.neo2.telebang.R;
import com.neo2.telebang.model.MenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/10/2016.
 */
public class MenuSetting {
    private static MenuSetting mInstance;

    private final List<MenuModel> listMenuAtSlideMenu = new ArrayList<>();
    private final List<MenuModel> listMenuAtAccount = new ArrayList<>();
    private final AppConstant.MENU_TYPE defaultTypeMenuActive = AppConstant.MENU_TYPE.HOME_1;

    public static synchronized MenuSetting getInstance() {
        if (mInstance == null)
            mInstance = new MenuSetting();

        return mInstance;
    }

    public MenuSetting() {
        settingSlideMenu();
        settingAccountMenu();
        settingMainMenu();
    }

    private void settingSlideMenu() {
        // Home item
        if (AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.HOME_1 || AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.DEFAULT)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.HOME_1, "Home 1", R.drawable.ic_slide_menu_home, true));

        // Home item
        if (AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.HOME_2 || AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.DEFAULT)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.HOME_2, "Home 2", R.drawable.ic_slide_menu_home, true));

        // Home item
        if (AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.HOME_3 || AppConfig.HOME_SCREEN == AppConstant.YO_SCREEN.DEFAULT)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.HOME_3, "Home 3", R.drawable.ic_slide_menu_home, true));

        // Categories item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.CATEGORIES, "Categories", R.drawable.ic_slide_menu_categories, true));

        // Series item
        if (AppConfig.SERIES_MODULE == AppConstant.YO_MODULE.SERIES_1)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.SERIES, "Series", R.drawable.ic_menu_series, true));

        // Watch item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.WATCH_LIST, "My Favourite", R.drawable.ic_slide_menu_watch_list, true));

        // Download item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.DOWNLOAD, "My Download", R.drawable.ic_action_video_download, true));

        // Account item
        if (AppConfig.NEWS_MODULE == AppConstant.YO_MODULE.NEWS_1)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.NEWS, "News/Blog", R.drawable.ic_slide_menu_news, true));

        // Upload item
        if (AppConfig.UPLOAD_MODULE == AppConstant.YO_MODULE.UPLOAD_VIDEO_1)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.UPLOAD_VIDEO, "Upload Video", R.drawable.ic_slide_menu_upload, true));

        // About item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.ABOUT_US, "About Us", R.drawable.ic_slide_menu_about, true));

        // Terms item
        MenuModel about = new MenuModel(AppConstant.MENU_TYPE.TERM, "Terms & Privacy Policy", R.drawable.ic_slide_menu_term, true);
        about.showLine = true;
        listMenuAtSlideMenu.add(about);

        // Feedback item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.FEEDBACK, "Feedback", R.drawable.ic_slide_menu_feedback, true));

        // Help item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.HELP, "Help", R.drawable.ic_slide_menu_help, true));

        // Share item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.SHARE, "Share", R.drawable.ic_slide_menu_share, true));

        // Subscription item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.SUBSCRIPTION, "Subscription", R.drawable.ic_slide_menu_news, true));
    }

    private void settingAccountMenu() {
        // Watch item
        listMenuAtAccount.add(new MenuModel(AppConstant.MENU_TYPE.WATCH_LIST, "My Favourite", R.drawable.ic_slide_menu_watch_list, true));

        // Download item
        listMenuAtAccount.add(new MenuModel(AppConstant.MENU_TYPE.DOWNLOAD, "My Download", R.drawable.ic_action_video_download, true));

        // Account item
        if (AppConfig.NEWS_MODULE == AppConstant.YO_MODULE.NEWS_1)
            listMenuAtAccount.add(new MenuModel(AppConstant.MENU_TYPE.NEWS, "News/Blog", R.drawable.ic_slide_menu_news, true));

        // About item
        MenuModel about =
                new MenuModel(AppConstant.MENU_TYPE.ABOUT_US, "About Us", R.drawable.ic_slide_menu_about, true);
        about.showLine = true;
        listMenuAtAccount.add(about);

        // Terms item
        listMenuAtAccount.add(new MenuModel(AppConstant.MENU_TYPE.TERM, "Terms & Privacy Policy", R.drawable.ic_slide_menu_term, true));

        // Feedback item
        listMenuAtAccount.add(new MenuModel(AppConstant.MENU_TYPE.FEEDBACK, "Feedback", R.drawable.ic_slide_menu_feedback, true));
    }

    private void settingMainMenu() {

    }

    public List<MenuModel> getListMenuAtSlideMenu() {
        return listMenuAtSlideMenu;
    }

    public List<MenuModel> getListMenuAtAccount() {
        return listMenuAtAccount;
    }

    public AppConstant.MENU_TYPE getDefaultTypeMenuActive() {
        return defaultTypeMenuActive;
    }
}

