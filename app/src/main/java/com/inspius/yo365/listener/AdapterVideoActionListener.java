package com.inspius.yo365.listener;

import com.inspius.yo365.model.VideoModel;

/**
 * Created by Billy on 1/6/16.
 */
public interface AdapterVideoActionListener {
    void onPlayClickListener(VideoModel model);

    void onItemClickListener(VideoModel model);
}
