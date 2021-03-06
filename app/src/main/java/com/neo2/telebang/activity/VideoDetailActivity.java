package com.neo2.telebang.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inspius.coreapp.helper.InspiusUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.StdActivity;
import com.neo2.telebang.fragment.SplashFragment;
import com.neo2.telebang.fragment.VideoDetailFragment;
import com.neo2.telebang.model.VideoModel;

import butterknife.ButterKnife;

public class VideoDetailActivity extends StdActivity {
    private VideoModel videoModel;
    private InterstitialAd mInterstitialAd;
    private boolean isAutoPlay;

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
        isAutoPlay = getIntent().getExtras().getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);

        if (videoModel == null)
            return;

        initAds();
        initFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(null);
        }
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
                    //requestNewInterstitial();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showInterstitialAds();
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
