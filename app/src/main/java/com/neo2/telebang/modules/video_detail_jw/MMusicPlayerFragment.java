package com.neo2.telebang.modules.video_detail_jw;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neo2.telebang.R;
import com.neo2.telebang.helper.ImageUtil;
import com.neo2.telebang.model.VideoModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import co.mobiwise.library.MusicPlayerView;

/**
 * Created by Billy on 12/1/16.
 */

public class MMusicPlayerFragment extends Fragment {
    public static MMusicPlayerFragment newInstance(VideoModel model, boolean isAutoPlay) {
        MMusicPlayerFragment fragment = new MMusicPlayerFragment();
        fragment.videoModel = model;
        fragment.isAutoPlay = isAutoPlay;
        return fragment;
    }

    protected VideoModel videoModel;
    boolean isAutoPlay;

    MusicPlayerView mpv;
    private MediaPlayer player;
    int length;
    ImageView imvThumbnail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.m_fragment_music_player, container, false);
        mpv = (MusicPlayerView) view.findViewById(R.id.mpv);
        imvThumbnail = (ImageView) view.findViewById(R.id.imvThumbnail);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (TextUtils.isEmpty(videoModel.getThumbnail()))
            mpv.setCoverDrawable(R.drawable.no_image_default);
        else
            mpv.setCoverURL(videoModel.getThumbnail());

        ImageLoader.getInstance().displayImage(videoModel.getThumbnail(), imvThumbnail, ImageUtil.optionsImageDefault);

        mpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player == null) {
                    try {
                        playAudio(videoModel.getVideoPath());

                        int duration = player.getDuration() / 1000;
                        if (duration <= 0)
                            duration = getDuration();

                        if (duration > 0)
                            mpv.setMax(duration);

                        mpv.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mpv.isRotating() && player != null) {
                        mpv.stop();
                        length = player.getCurrentPosition();
                        player.pause();

                    } else if (!mpv.isRotating() && player != null) {
                        mpv.start();
                        //  player.seekTo(length);
                        player.start();
                    }
                }
            }
        });

        if (isAutoPlay) {
            if (player == null) {
                try {
                    playAudio(videoModel.getVideoPath());
                    int duration = player.getDuration() / 1000;
                    if (duration <= 0)
                        duration = getDuration();

                    if (duration > 0)
                        mpv.setMax(duration);

                    mpv.start();
                    mpv.toggle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int getDuration() {
        String[] timef = videoModel.getTimeRemain().split(":");
        int temp = 0;
        if (timef.length == 2) {
            int minute = Integer.parseInt(timef[0]);
            int second = Integer.parseInt(timef[1]);
            temp = second + (60 * minute);
        } else if (timef.length == 3) {
            int hour = Integer.parseInt(timef[0]);
            int minute = Integer.parseInt(timef[1]);
            int second = Integer.parseInt(timef[2]);
            temp = second + (60 * minute) + hour * 60 * 60;
        }

        return temp;
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }

    private void killMediaPlayer() {
        if (player != null) {
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }
}
