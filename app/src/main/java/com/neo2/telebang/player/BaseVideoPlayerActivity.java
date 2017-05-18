package com.neo2.telebang.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.model.VideoModel;

import butterknife.ButterKnife;

/**
 * Created by Billy on 1/6/17.
 */

public abstract class BaseVideoPlayerActivity extends AppCompatActivity {
    protected VideoModel videoModel;
    protected boolean shouldAutoPlay;

    public abstract int getLayoutResID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(getLayoutResID());

        ButterKnife.bind(this);

        if (getIntent() == null)
            return;

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        if (!bundle.containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) bundle.getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        if (videoModel == null)
            finish();

        this.shouldAutoPlay = bundle.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, true);
    }
}
