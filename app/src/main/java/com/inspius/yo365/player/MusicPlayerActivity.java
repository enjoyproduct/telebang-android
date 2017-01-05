package com.inspius.yo365.player;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspius.yo365.R;
import com.inspius.yo365.helper.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import co.mobiwise.library.MusicPlayerView;

/**
 * Created by Admin on 30/6/2016.
 */
public class MusicPlayerActivity extends BaseVideoPlayerActivity {
    @BindView(R.id.mpv)
    MusicPlayerView mpv;

    @BindView(R.id.textViewSong)
    TextView textViewSong;

    @BindView(R.id.imvThumbnail)
    ImageView imvThumbnail;

    private MediaPlayer player;
    int length;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_music_player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mpv.setCoverURL(videoModel.getThumbnail());
        ImageLoader.getInstance().displayImage(videoModel.getThumbnail(), imvThumbnail, ImageUtil.optionsImageDefault);

        if (videoModel.getTitle() != null)
            textViewSong.setText(videoModel.getTitle());

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
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }
}
