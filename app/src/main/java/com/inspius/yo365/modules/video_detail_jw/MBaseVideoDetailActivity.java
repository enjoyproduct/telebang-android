package com.inspius.yo365.modules.video_detail_jw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.activity.VideoCommentActivity;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.greendao.DBVideoDownload;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.manager.CustomerManager;
import com.inspius.yo365.manager.DatabaseManager;
import com.inspius.yo365.model.LikeStatusResponse;
import com.inspius.yo365.model.VideoModel;
import com.inspius.yo365.service.DownloadRequestQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/16.
 */

public abstract class MBaseVideoDetailActivity extends AppCompatActivity {
    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnTitle)
    TextView tvnTitle;

    @BindView(R.id.tvnDescription)
    TextView tvnDescription;

    @BindView(R.id.tvnCreateAt)
    TextView tvnCreateAt;

    @BindView(R.id.tvnViewCounter)
    TextView tvnViewCounter;

    @BindView(R.id.imvHeaderWishList)
    ImageView imvAddToWishList;

    @BindView(R.id.imvHeaderDownload)
    ImageView imvDownload;

    @BindView(R.id.imvLike)
    ImageView imvLike;

    @BindView(R.id.tvnLike)
    TextView tvnLike;

    @BindView(R.id.ad_view)
    AdView mAdView;

    @BindView(R.id.linearContent)
    LinearLayout linearContent;

    @BindView(R.id.container)
    FrameLayout frameContainer;

    protected VideoModel videoModel;
    protected boolean shouldAutoPlay;
    private DBWishListVideo dbWishListVideo;

    protected int containerViewId = R.id.container;

    private CustomerManager mCustomerManager;
    protected Context mContext;
    private InterstitialAd mInterstitialAd;

    abstract void initPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_video_detail);
        ButterKnife.bind(this);

        mCustomerManager = CustomerManager.getInstance();
        mContext = getApplicationContext();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() == null)
            return;

        Bundle arguments = getIntent().getExtras();
        if (arguments == null)
            return;

        if (!arguments.containsKey(AppConstant.KEY_BUNDLE_VIDEO))
            return;

        videoModel = (VideoModel) arguments.getSerializable(AppConstant.KEY_BUNDLE_VIDEO);
        shouldAutoPlay = arguments.getBoolean(AppConstant.KEY_BUNDLE_AUTO_PLAY, false);

        tvnHeaderTitle.setText(videoModel.getCategoryName());

        initInfo();
        initAds();
        initPlayer();
    }

    void initInfo() {
        tvnTitle.setText(videoModel.getTitle());
//        tvnSeries.setText(videoModel.getSeries());
//        tvnAuthor.setText(videoModel.getAuthor());
        tvnDescription.setText(Html.fromHtml(videoModel.getDescription()));
        tvnViewCounter.setText(videoModel.getViewCounterStringFormat());

        DBVideoDownload dbVideoDownload = DatabaseManager.getInstance().getVideoDownloadByVideoID(videoModel.getVideoId());
        if (dbVideoDownload != null)
            updateStateDownloadButton(true);
        else
            updateStateDownloadButton(false);

        if (mCustomerManager.isLogin())
            dbWishListVideo = DatabaseManager.getInstance().getVideoWithListByVideoID(mCustomerManager.getAccountID(), videoModel.getVideoId());
        updateStateViewWishList();

        checkLikeStatus();
    }

    void initAds() {
        if (AppConfig.SHOW_ADS_BANNER) {
            /**
             * Show Banner Ads
             */

            AdRequest adRequest;

            if (InspiusUtils.isProductionEnvironment()) {
                adRequest = new AdRequest.Builder().build();
            } else {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
            }

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    if (mAdView != null)
                        mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    mAdView.setVisibility(View.GONE);
                }
            });
        } else {
            mAdView.setVisibility(View.GONE);
        }

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

    void updateStateViewWishList() {
        if (dbWishListVideo != null)
            imvAddToWishList.setSelected(true);
        else
            imvAddToWishList.setSelected(false);
    }

    void updateStateDownloadButton(boolean isDownload) {
        imvDownload.setSelected(isDownload);
    }

    void updateStateLikeButton(boolean isLiked) {
        imvLike.setSelected(isLiked);
        if (isLiked)
            tvnLike.setText("Liked");
        else
            tvnLike.setText("Like");
    }

    @OnClick(R.id.imvHeaderBack)
    void doBack() {
        finish();
    }

    void checkLikeStatus() {
        if (!mCustomerManager.isLogin()) {
            updateStateLikeButton(false);
            return;
        }

        RPC.getUserLikeVideoState(mCustomerManager.getAccountID(), videoModel.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onSuccess(Object results) {
                LikeStatusResponse likeStatusResponse = (LikeStatusResponse) results;

                if (likeStatusResponse.action.equalsIgnoreCase("Liked"))
                    updateStateLikeButton(true);
                else
                    updateStateLikeButton(false);
            }
        });
    }

    @OnClick(R.id.imvHeaderWishList)
    void doAddWishList() {
        if (!mCustomerManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));

            return;
        }

        boolean isWishList = imvAddToWishList.isSelected();
        imvAddToWishList.setSelected(!isWishList);

        if (dbWishListVideo != null) {
            DatabaseManager.getInstance().deleteVideoWishList(dbWishListVideo.getId());
            dbWishListVideo = null;
        } else {
            dbWishListVideo = DatabaseManager.getInstance().insertVideoToWishList(mCustomerManager.getAccountID(), videoModel);
        }

        updateStateViewWishList();
    }

    @OnClick(R.id.linearLike)
    void doLike() {
        if (!mCustomerManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));
            return;
        }

        boolean isLike = imvLike.isSelected();
        updateStateLikeButton(!isLike);

        RPC.requestUserLikeVideo(mCustomerManager.getAccountID(), videoModel.getVideoId(), new APIResponseListener() {
            @Override
            public void onError(String message) {
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                LikeStatusResponse likeStatusResponse = (LikeStatusResponse) results;

                if (likeStatusResponse.action.equalsIgnoreCase("Liked"))
                    updateStateLikeButton(true);
                else
                    updateStateLikeButton(false);
            }
        });
    }

    @OnClick(R.id.linearShare)
    void doShare() {
        if (videoModel == null)
            return;

        String urlShare = videoModel.getSocialLink();

        Intent intent = InspiusIntentUtils.shareText(videoModel.getTitle(), urlShare);
        startActivity(intent);

        requestUpdateVideoCounter(AppConstant.COUNTER_FIELD.SHARE);
    }

    @OnClick(R.id.imvHeaderDownload)
    void doDownload() {
        if (!mCustomerManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));

            return;
        }

        if (!isCustomerPlayOrDownloadVideo())
            return;

        if (imvDownload.isSelected())
            return;

        if (!AppUtil.verifyStoragePermissions(this))
            return;

        updateStateDownloadButton(true);

        if (videoModel.getVideoType() == AppConstant.VIDEO_TYPE.UPLOAD || videoModel.getVideoType() == AppConstant.VIDEO_TYPE.MP3) {
            DownloadRequestQueue.getInstance().downloadVideo(videoModel);
        } else {
            updateStateDownloadButton(false);
            DialogUtil.showMessageBox(mContext, "Download Unsupported File Formats");
        }
    }

    boolean isCustomerPlayOrDownloadVideo() {
//        if (videoModel.isVipPlayer() && !mAccountDataManager.isVip()) {
//            if (!mAccountDataManager.isLogin()) {
//                DialogUtil.showMessageBox(this, getString(R.string.msg_request_login_vip));
//            } else if (!mAccountDataManager.isVip()) {
//                DialogUtil.showMessageVip(this, getString(R.string.msg_need_vip), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // implement payment
//                    }
//                });
//            }
//            return false;
//        }

        return true;
    }

    @OnClick(R.id.linearComment)
    void doComment() {
        Intent intent = new Intent(mContext, VideoCommentActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }

    void requestUpdateVideoCounter(AppConstant.COUNTER_FIELD field) {
        RPC.updateVideoCounter(mCustomerManager.getAccountID(), videoModel.getVideoId(), field, null);
    }
}
