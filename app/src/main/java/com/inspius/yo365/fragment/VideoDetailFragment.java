package com.inspius.yo365.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.activity.VideoCommentActivity;
import com.inspius.yo365.api.APIResponseListener;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.base.StdFragment;
import com.inspius.yo365.greendao.DBVideoDownload;
import com.inspius.yo365.greendao.DBWishListVideo;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.manager.DatabaseManager;
import com.inspius.yo365.model.LikeStatusResponse;
import com.inspius.yo365.model.VideoModel;
import com.inspius.yo365.service.DownloadRequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
    }

    void initInfo() {
        tvnTitle.setText(videoModel.getTitle());
//        tvnSeries.setText(videoModel.getSeries());
//        tvnAuthor.setText(videoModel.getAuthor());
        tvnDescription.setText(Html.fromHtml(videoModel.getDescription()));
        tvnViewCounter.setText(videoModel.getViewCounterStringFormat());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image_default)
                .showImageForEmptyUri(R.drawable.no_image_default)
                .showImageOnFail(R.drawable.no_image_default)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage(videoModel.getThumbnail(), imvThumbnail, options);


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

    @OnClick(R.id.btnPlay)
    void doPlayVideo() {

    }

    @OnClick(R.id.linearComment)
    void doComment() {
        Intent intent = new Intent(mContext, VideoCommentActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        startActivity(intent);
    }
}
