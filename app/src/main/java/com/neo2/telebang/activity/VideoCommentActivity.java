package com.neo2.telebang.activity;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inspius.coreapp.helper.InspiusUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.StdActivity;
import com.neo2.telebang.fragment.VideoCommentFragment;
import com.neo2.telebang.fragment.VideoDetailFragment;
import com.neo2.telebang.model.VideoModel;

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
