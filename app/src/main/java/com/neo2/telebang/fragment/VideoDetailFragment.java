package com.neo2.telebang.fragment;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.coreapp.helper.InspiusSharedPrefUtils;
import com.inspius.coreapp.helper.InspiusUtils;
import com.neo2.telebang.R;
import com.neo2.telebang.activity.SubscriptionActivity;
import com.neo2.telebang.activity.VideoCommentActivity;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConfig;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.base.StdFragment;
import com.neo2.telebang.greendao.DBVideoDownload;
import com.neo2.telebang.greendao.DBWishListVideo;
import com.neo2.telebang.helper.AppUtil;
import com.neo2.telebang.helper.DialogUtil;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.helper.TimeUtil;
import com.neo2.telebang.manager.CustomerManager;
import com.neo2.telebang.manager.DatabaseManager;
import com.neo2.telebang.model.LikeStatusResponse;
import com.neo2.telebang.model.VideoModel;
import com.neo2.telebang.player.DailyMotionPlayerActivity;
import com.neo2.telebang.player.JWPlayerActivity;
import com.neo2.telebang.player.MusicPlayerActivity;
import com.neo2.telebang.player.WebViewPlayerActivity;
import com.neo2.telebang.player.YoutubePlayerActivity;
import com.neo2.telebang.service.DownloadRequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class VideoDetailFragment extends StdFragment {
    public static final String TAG = VideoDetailFragment.class.getSimpleName();

    public static VideoDetailFragment newInstance(VideoModel videoModel, boolean shouldAutoPlay) {
        VideoDetailFragment fragment = new VideoDetailFragment();
        fragment.videoModel = videoModel;
        fragment.shouldAutoPlay = shouldAutoPlay;
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnTitle)
    TextView tvnTitle;

//    @BindView(R.id.tvnAuthor)
//    TextView tvnAuthor;

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

    @BindView(R.id.imvThumbnail)
    ImageView imvThumbnail;

//    @BindView(R.id.tvnSeries)
//    TextView tvnSeries;

    @BindView(R.id.ad_view)
    AdView mAdView;

    private VideoModel videoModel;
    protected boolean shouldAutoPlay;
    private DBWishListVideo dbWishListVideo;

    @Override
    public int getLayout() {
        return R.layout.fragment_video_detail;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        tvnHeaderTitle.setText(videoModel.getCategoryName());

        // init info
        initInfo();

        // init ads
        initAds();

        // update view counter
        requestUpdateVideoCounter(AppConstant.COUNTER_FIELD.VIEW);
    }

    void initInfo() {
        tvnTitle.setText(videoModel.getTitle());
//        tvnSeries.setText(videoModel.getSeries());
//        tvnAuthor.setText(videoModel.getAuthor());
        tvnCreateAt.setText(videoModel.getUpdateAt());
        tvnDescription.setText(Html.fromHtml(videoModel.getDescription()));
        tvnViewCounter.setText(videoModel.getViewCounterStringFormat());

        ImageLoader.getInstance().displayImage(videoModel.getThumbnail(), imvThumbnail, ImageUtil.optionsImageDefault);

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
        getActivity().finish();
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
        imvLike.setSelected(!isLike);

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

        if (!AppUtil.verifyStoragePermissions(getActivity()))
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

    @OnClick(R.id.imvPlay)
    void doPlayClicked() {
        //check login status
        if (CustomerManager.getInstance().getUsername().equals("")) {
            DialogUtil.showMessageBox(getActivity(), "Please login");
            return ;
        }
        if (checkVIP()) {
            playVideo();
        } else {
            Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
            getActivity().startActivityForResult(intent, 1000);
        }
    }

    @OnClick(R.id.btnPlay)
    void doPlayVideo() {
        //check login status
        if (CustomerManager.getInstance().getUsername().equals("")) {
            DialogUtil.showMessageBox(getActivity(), "Please login");
            return ;
        }
        if (checkVIP()) {
            playVideo();
        } else {
            Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
            getActivity().startActivityForResult(intent, 1000);
        }
    }
    boolean checkVIP() {

        if (videoModel.getVipPlay() == 0) {
            return true;
        } else {
            //check last subscription date
            int lastSubscribedType = InspiusSharedPrefUtils.getFromPrefs(getContext(), AppConstant.KEY_SUBSCRIBED_TYPE, 0);
            int lastSubscribedTimestamp = InspiusSharedPrefUtils.getFromPrefs(getContext(), AppConstant.KEY_SUBSCRIBED_DATE, 0);
            int currentTimeStamp = TimeUtil.getCurrentTimeStamp();
           if ((currentTimeStamp - lastSubscribedTimestamp) > (3600 * 24 * 30 * countMonth(lastSubscribedType))) {
           // if ((currentTimeStamp - lastSubscribedTimestamp) > (60)) {
                return false;
            }
            return  true;
        }
    }
    int countMonth(int type) {
        switch (type) {
            case 0:
                return 1;
            case 1:
                return 3;
            case 2:
                return 6;
            case 3:
                return 12;
            default:
                return 1;
        }
    }
    void playVideo() {
        if (videoModel == null)
            return;

        if (!isCustomerPlayOrDownloadVideo())
            return;

        Intent intent = null;
        switch (videoModel.getVideoType()) {
            case YOUTUBE:
                intent = new Intent(mContext, YoutubePlayerActivity.class);
                break;

            case MP3:
                intent = new Intent(mContext, MusicPlayerActivity.class);
                break;

            case DAILY_MOTION:
                intent = new Intent(mContext, DailyMotionPlayerActivity.class);
                break;

            case UPLOAD:
                intent = new Intent(mContext, JWPlayerActivity.class);
                break;

            default:
                intent = new Intent(mContext, WebViewPlayerActivity.class);
                break;
        }

        if (intent != null) {
            intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
            intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, true);

            startActivity(intent);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) { //subscription success
            doPlayVideo();
        }
    }
}
