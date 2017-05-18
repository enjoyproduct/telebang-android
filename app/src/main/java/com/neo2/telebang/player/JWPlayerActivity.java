package com.neo2.telebang.player;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.neo2.telebang.R;
import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;

/**
 * Created by Billy on 1/3/17.
 */

public class JWPlayerActivity extends BaseVideoPlayerActivity {
    private JWPlayerSupportFragment playerFragment;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_jw_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .file(videoModel.getVideoPath())
                .autostart(true)
                .image(videoModel.getThumbnail())
                .build();

        playerFragment = JWPlayerSupportFragment.newInstance(playerConfig);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, playerFragment).commit();
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        playerFragment.getPlayer().onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        playerFragment.getPlayer().onPause();
        super.onPause();
    }
}
