package com.neo2.telebang.modules.video_detail_jw;

import android.support.v4.app.FragmentTransaction;

/**
 * Created by Billy on 12/1/16.
 */

public class MMusicDetailActivity extends MBaseVideoDetailActivity {
    @Override
    void initPlayer() {
        MMusicPlayerFragment playerFragment = MMusicPlayerFragment.newInstance(videoModel, shouldAutoPlay);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
