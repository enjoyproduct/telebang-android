package com.inspius.yo365.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.inspius.yo365.R;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.base.BaseAppSlideActivityInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppSlideActivity extends StdActivity implements BaseAppSlideActivityInterface {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_slide);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public void toggleDrawer() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }
}
