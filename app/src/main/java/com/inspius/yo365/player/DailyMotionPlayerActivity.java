package com.inspius.yo365.player;

import android.os.Bundle;

import com.inspius.yo365.R;
import com.inspius.yo365.widget.DMWebVideoView;

import butterknife.BindView;

public class DailyMotionPlayerActivity extends BaseVideoPlayerActivity {
    @BindView(R.id.dmWebVideoView)
    DMWebVideoView videoView;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_dailymotion_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoView.loadUrl(videoModel.getVideoPath());
        videoView.setAutoPlay(true);
    }
}
