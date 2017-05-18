package com.neo2.telebang.player;

import android.os.Bundle;

import com.neo2.telebang.R;
import com.neo2.telebang.widget.DMWebVideoView;

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
