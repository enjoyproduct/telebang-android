package com.neo2.telebang.model;

import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConstant;

/**
 * Created by dev on 3/3/2015.
 */
public class MenuModel {
    public boolean isActive;
    public int icon;
    public String title;
    public AppConstant.MENU_TYPE type;
    public boolean enable;
    public boolean showLine;

    public MenuModel() {
        enable = false;
        this.icon = R.drawable.inspius_logo;
        isActive = false;
    }

    public MenuModel(AppConstant.MENU_TYPE type, String title, int icon, boolean enable) {
        this.icon = icon;
        this.title = title;
        this.type = type;
        this.enable = enable;
    }

    public MenuModel(AppConstant.MENU_TYPE type, String title, int icon) {
        this.type = type;
        this.title = title;
        this.icon = icon;
        this.enable = false;
    }

    public String getIconPath() {
        return "drawable://" + icon;
    }
}
