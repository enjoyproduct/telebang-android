package com.inspius.yo365.activity;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdActivity;
import com.inspius.yo365.fragment.VideoCommentFragment;
import com.inspius.yo365.fragment.VideoDetailFragment;
import com.inspius.yo365.model.VideoModel;

import butterknife.ButterKnife;

public class VideoCommentActivity extends StdActivity {
    private VideoModel videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);
        ButterKnife.bind(this);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);

        if (videoModel == null)
            return;

        initFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    public void initFragment() {
        addFragment(VideoCommentFragment.newInstance(videoModel));
    }

    @Override
    public void handleBackPressInThisActivity() {
        finish();
    }
}
