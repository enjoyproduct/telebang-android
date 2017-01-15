package com.inspius.yo365.api;

import android.os.Looper;

import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.coreapp.helper.Logger;
import com.inspius.yo365.app.AppConfig;
import com.inspius.yo365.app.GlobalApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Admin on 30/1/2016.
 */
public class AppRestClient {
    public static final String TAG = AppRestClient.class.getSimpleName();

    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static AsyncHttpClient synClient = new SyncHttpClient();

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_APP_ID = "AppId";
    private static final String HEADER_BASIC = "basic";

    public static void initAsyncHttpClient() {
//        client.setBasicAuth("username","password/token");
    }

    public static void cancelAllRequests() {
        synClient.cancelAllRequests(true);
        asyncClient.cancelAllRequests(true);
    }

    public static void cancelRequestsByTAG(String TAG) {
        synClient.cancelRequestsByTAG(TAG, true);
        asyncClient.cancelRequestsByTAG(TAG, true);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        get(url, null, responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler).setTag(url);
    }

    public static void download(String url, FileAsyncHttpResponseHandler responseHandler) {
        getClient().get(GlobalApplication.getAppContext(), url, responseHandler).setTag(url);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler).setTag(url);
        if (params != null)
            Logger.d(TAG, "params : " + params.toString());
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        String url;
        if (InspiusUtils.isProductionEnvironment())
            url = AppConfig.BASE_URL_PRODUCTION + relativeUrl;
        else
            url = AppConfig.BASE_URL_DEVELOPMENT + relativeUrl;

        Logger.d(TAG, url);
        return url;
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return synClient;
        return asyncClient;
    }
}
