package com.inspius.yo365.modules.video_detail_jw;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.inspius.yo365.R;
import com.inspius.yo365.app.DeveloperKey;
import com.inspius.yo365.helper.VideoUtil;

/**
 * Created by Billy on 12/1/16.
 */

public class MYoutubeVideoDetailActivity extends MBaseVideoDetailActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayer youTubePlayer;
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @Override
    void initPlayer() {
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();
        playerFragment.initialize(DeveloperKey.DEVELOPER_YOUTUBE_KEY, this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, playerFragment).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (youTubePlayer != null)
                youTubePlayer.setFullscreen(true);
        } else
            youTubePlayer.setFullscreen(false);
    }

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        this.youTubePlayer = youTubePlayer;
//        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
//        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
//        //alongside the player UI
//        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        int controlFlags = youTubePlayer.getFullscreenControlFlags();
        setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        youTubePlayer.setFullscreenControlFlags(controlFlags);

        if (restored) {
            youTubePlayer.play();
        } else {
            String mYoutubeId = VideoUtil.getYoutubeIdByUrl(videoModel.getVideoPath());
            if (shouldAutoPlay)
                youTubePlayer.loadVideo(mYoutubeId);
            else
                youTubePlayer.cueVideo(mYoutubeId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
