/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inspius.yo365.player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.inspius.yo365.R;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.app.DeveloperKey;
import com.inspius.yo365.helper.VideoUtil;
import com.inspius.yo365.model.VideoModel;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class YoutubePlayerActivity extends YouTubeFailureRecoveryActivity {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    private YouTubePlayerView playerView;
    private String videoYoutubeID;
    boolean shouldAutoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_youtube_player);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.initialize(DeveloperKey.DEVELOPER_YOUTUBE_KEY, this);

        if (getIntent() == null)
            return;

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        if (!bundle.containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        VideoModel videoModel = (VideoModel) bundle.getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        videoYoutubeID = VideoUtil.getYoutubeIdByUrl(videoModel.getVideoPath());
        this.shouldAutoPlay = bundle.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);

        int controlFlags = player.getFullscreenControlFlags();
        setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);

        if (wasRestored) {
            player.play();
        } else {
            if (shouldAutoPlay)
                player.loadVideo(videoYoutubeID);
            else
                player.cueVideo(videoYoutubeID);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
