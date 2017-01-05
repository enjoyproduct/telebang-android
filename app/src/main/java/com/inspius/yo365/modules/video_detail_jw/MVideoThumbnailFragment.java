package com.inspius.yo365.modules.video_detail_jw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.helper.ImageUtil;
import com.inspius.yo365.model.VideoModel;
import com.inspius.yo365.player.WebViewPlayerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Billy on 12/1/16.
 */

public class MVideoThumbnailFragment extends Fragment {
    public static MVideoThumbnailFragment newInstance(VideoModel model, boolean isAutoPlay) {
        MVideoThumbnailFragment fragment = new MVideoThumbnailFragment();
        fragment.videoModel = model;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

    @BindView(R.id.imvThumbnail)
    ImageView imvThumbnail;

    @BindView(R.id.tvnTime)
    TextView tvnTime;

    protected VideoModel videoModel;
    private Context mContext;
    boolean isAutoPlay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.m_fragment_video_thumbnail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageLoader.getInstance().displayImage(videoModel.getThumbnail(), imvThumbnail, ImageUtil.optionsImageDefault);
        tvnTime.setText(videoModel.getTimeRemain());

        if (isAutoPlay)
            doPlayVideo();
    }

    @OnClick(R.id.relativePlay)
    void doPlayVideo() {
        if (videoModel == null)
            return;

        Intent intent = new Intent(mContext, WebViewPlayerActivity.class);
        intent.putExtra(AppConstant.KEY_BUNDLE_VIDEO, videoModel);
        intent.putExtra(AppConstant.KEY_BUNDLE_AUTO_PLAY, true);

        startActivity(intent);
    }
}
