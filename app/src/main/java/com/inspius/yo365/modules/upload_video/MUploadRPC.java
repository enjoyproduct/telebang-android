package com.inspius.yo365.modules.upload_video;

import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspius.coreapp.helper.Logger;
import com.inspius.yo365.api.AppRestClient;
import com.inspius.yo365.api.RPC;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.model.ResponseJSON;
import com.inspius.yo365.model.VideoJSON;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Billy on 1/5/17.
 */

public class MUploadRPC {
    public static void requestUpLoadVideo(int userId, MVideoUploadModel video, final MUploadListener listener) {
        RequestParams params = new RequestParams();

        if (!TextUtils.isEmpty(video.getVideoDes()))
            params.put(MUploadConstant.KEY_VIDEO_DESC, video.getVideoDes());

        params.put(AppConstant.KEY_USER_ID, userId);

        if (!TextUtils.isEmpty(video.getCategoryId()))
            params.put(MUploadConstant.KEY_CATE_IDS, video.getCategoryId());

        params.put(MUploadConstant.KEY_VIDEO_TITLE, video.getVideoTitle());

        // video file

        try {
            params.put(MUploadConstant.KEY_FILE_VIDEO, video.getVideoFile(), video.getVideoFileType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        params.put(MUploadConstant.KEY_VIDEO_LENGTH, video.getVideoLength());

        // thumbnail
        if (video.getThumbnailFile() != null) {
            byte[] b = video.getThumbnailBytes();
            String name = video.getThumbnailName();
            String type = video.getThumbnailMimeType();

            params.put(MUploadConstant.KEY_THUMBNAIL_VIDEO, new ByteArrayInputStream(b), name, type);
        }

        AppRestClient.post(MUploadConstant.RELATIVE_URL_UPLOAD_VIDEO, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        if (listener != null) {
                            VideoJSON data = new ObjectMapper().readValue(response.getContentString(), VideoJSON.class);

                            listener.onSuccess(data);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                RPC.onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return RPC.onResponse(rawJsonData);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int percent = (int) (100 * bytesWritten / totalSize);

                listener.onProgress(percent);
            }
        });
    }
}
