package com.inspius.yo365.app;

import com.inspius.yo365.R;
import com.inspius.yo365.model.MenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/10/2016.
 */
public class MenuSetting {
    private static MenuSetting mInstance;

    private final List<MenuModel> listMenuAtSlideMenu = new ArrayList<>();
    private final AppConstant.MENU_TYPE defaultTypeMenuActive = AppConstant.MENU_TYPE.HOME;

    public static synchronized MenuSetting getInstance() {
        if (mInstance == null)
            mInstance = new MenuSetting();

        return mInstance;
    }

    public MenuSetting() {
        settingSlideMenu();
        settingMainMenu();
    }

    private void settingSlideMenu() {
        // Home item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.HOME, "Home", R.drawable.ic_slide_menu_home, true));

        // Categories item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.CATEGORIES, "Categories", R.drawable.ic_slide_menu_categories, true));

        // Watch item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.WATCH_LIST, "My favourite", R.drawable.ic_slide_menu_watch_list, true));

        // Download item
        listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.DOWNLOAD, "Download List", R.drawable.ic_action_video_download, true));

        // Account item
        if (AppConfig.NEWS_MODULE == AppConstant.YO_MODULE.NEWS_1)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.NEWS, "News/Blog", R.drawable.ic_slide_menu_news, true));

        // Account item
        if (AppConfig.UPLOAD_MODULE == AppConstant.YO_MODULE.UPLOAD_VIDEO_1)
            listMenuAtSlideMenu.add(new MenuModel(AppConstant.MENU_TYPE.UPLOAD_VIDEO, "Upload Video", R.drawable.ic_slide_menu_news, true));

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
    }

    private void settingMainMenu() {

    }

    public List<MenuModel> getListMenuAtSlideMenu() {
        return listMenuAtSlideMenu;
    }

    public AppConstant.MENU_TYPE getDefaultTypeMenuActive() {
        return defaultTypeMenuActive;
    }
}

