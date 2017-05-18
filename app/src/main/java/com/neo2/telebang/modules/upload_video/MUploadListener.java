package com.neo2.telebang.modules.upload_video;

import com.neo2.telebang.api.APIResponseListener;

/**
 * Created by Billy on 1/12/17.
 */

public interface MUploadListener extends APIResponseListener {
    void onProgress(int percent);
}
