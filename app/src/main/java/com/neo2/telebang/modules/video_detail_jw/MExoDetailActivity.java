package com.neo2.telebang.modules.video_detail_jw;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.neo2.telebang.R;

/**
 * Created by Billy on 12/1/16.
 */

public class MExoDetailActivity extends MBaseVideoDetailActivity {

    @Override
    void initPlayer() {
        MExoPlayerFragment playerFragment = MExoPlayerFragment.newInstance(videoModel, shouldAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            doLayout(true);
        } else {
            doLayout(false);
        }
    }
}
