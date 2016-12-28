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
import com.inspius.yo365.fragment.SplashFragment;
import com.inspius.yo365.fragment.VideoDetailFragment;
import com.inspius.yo365.model.VideoModel;

import butterknife.ButterKnife;

public class VideoDetailActivity extends StdActivity {
    private VideoModel videoModel;
    private InterstitialAd mInterstitialAd;
    private boolean isAutoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);

        if (getIntent() == null)
            return;

        if (getIntent().getExtras() == null)
            return;

        if (!getIntent().getExtras().containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) getIntent().getExtras().getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        isAutoPlay = getIntent().getExtras().getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);

        if (videoModel == null)
            return;

        initAds();
        initFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.id.container;
    }

    public void initFragment() {
        addFragment(VideoDetailFragment.newInstance(videoModel, isAutoPlay));
    }

    void initAds() {
        if (AppConfig.SHOW_ADS_INTERSTITIAL) {
            // Create the InterstitialAd and set the adUnitId.
            mInterstitialAd = new InterstitialAd(this);
            // Defined in res/values/strings.xml
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

            // Loading ads
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });
            requestNewInterstitial();
        }
    }

    public void showInterstitialAds() {
        if (!AppConfig.SHOW_ADS_INTERSTITIAL)
            return;

        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            requestNewInterstitial();
        }
    }

    private void requestNewInterstitial() {
        if (!AppConfig.SHOW_ADS_INTERSTITIAL)
            return;

        AdRequest adRequest;
        if (InspiusUtils.isProductionEnvironment()) {
            adRequest = new AdRequest.Builder().build();
        } else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        }

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void handleBackPressInThisActivity() {
        finish();
    }
}
