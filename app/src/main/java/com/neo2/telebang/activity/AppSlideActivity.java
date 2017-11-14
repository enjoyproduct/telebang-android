package com.neo2.telebang.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.BaseAppSlideActivityInterface;
import com.neo2.telebang.base.StdActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.paystack.android.PaystackSdk;

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
