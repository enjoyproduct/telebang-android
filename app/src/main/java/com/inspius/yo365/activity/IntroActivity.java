package com.inspius.yo365.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.inspius.yo365.R;
import com.inspius.yo365.fragment.IntroSlideFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.manager.CustomerManager;

/**
 * Created by Billy on 11/15/16.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        addSlide(IntroSlideFragment.newInstance(R.layout.screen_intro_1));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_intro_2));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_intro_3));
        addSlide(IntroSlideFragment.newInstance(R.layout.screen_intro_4));

        setColorSkipButton(Color.parseColor("#777777"));
        setColorDoneText(Color.parseColor("#777777"));
        setNextArrowColor(Color.parseColor("#777777"));

        selectedIndicatorColor = Color.parseColor("#EE2763");
        unselectedIndicatorColor = Color.parseColor("#E5E5E5");

        AppUtil.verifyStoragePermissions(this);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        loadMainActivity();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        pager.setCurrentItem(slidesNumber - 1);
    }

    public void getStarted(View v) {
        loadMainActivity();
    }

    void loadMainActivity() {
        Intent intent;
        if (CustomerManager.getInstance().isLogin())
            intent = new Intent(this, AppSlideActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }
}
