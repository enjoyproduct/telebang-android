package com.neo2.telebang.modules.video_detail_jw;

import android.support.v4.app.FragmentTransaction;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

/**
 * Created by Billy on 12/1/16.
 */

public class MJWVideoDetailActivity extends MBaseVideoDetailActivity {
    JWPlayerSupportFragment playerFragment;

    @Override
    void initPlayer() {
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .file(videoModel.getVideoPath())
                .autostart(shouldAutoPlay)
                .image(videoModel.getThumbnail())
                .build();

        playerFragment = JWPlayerSupportFragment.newInstance(playerConfig);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }
}
