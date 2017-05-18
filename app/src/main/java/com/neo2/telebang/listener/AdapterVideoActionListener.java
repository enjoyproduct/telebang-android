package com.neo2.telebang.listener;

import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.VideoModel;

/**
 * Created by Billy on 1/6/16.
 */
public interface AdapterVideoActionListener {
    void onPlayClickListener(VideoModel model);

    void onItemClickListener(VideoModel model);

    void onUserClickListener(CustomerJSON model);
}
