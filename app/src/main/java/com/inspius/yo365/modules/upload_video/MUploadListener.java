package com.inspius.yo365.modules.upload_video;

import com.inspius.yo365.api.APIResponseListener;

/**
 * Created by Billy on 1/12/17.
 */

public interface MUploadListener extends APIResponseListener {
    void onProgress(int percent);
}
