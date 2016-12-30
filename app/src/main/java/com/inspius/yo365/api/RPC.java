package com.inspius.yo365.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspius.coreapp.helper.InspiusUtils;
import com.inspius.coreapp.helper.Logger;
import com.inspius.yo365.app.AppConstant;
import com.inspius.yo365.model.CommentJSON;
import com.inspius.yo365.model.CustomerJSON;
import com.inspius.yo365.model.DataCategoryJSON;
import com.inspius.yo365.model.ImageFileModel;
import com.inspius.yo365.model.LikeStatusResponse;
import com.inspius.yo365.model.ResponseJSON;
import com.inspius.yo365.model.VideoJSON;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by it.kupi on 5/30/2015.
 */
public class RPC {
    public static final String LOG_TAG = RPC.class.getSimpleName();

    /* ======================================= CUSTOMER =======================================*/

    public static void requestAuthentic(final String username, final String password, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USERNAME, username);
        params.put(AppConstant.KEY_PASSWORD, password);

        AppRestClient.post(AppConstant.RELATIVE_URL_LOGIN, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                parseResponseCustomer(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void signInWithFacebook(final String accessToken, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_ACCESS_TOKEN, accessToken);

        AppRestClient.post(AppConstant.RELATIVE_URL_LOGIN_FACE_BOOK, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                parseResponseCustomer(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }


    public static void requestForgotPassword(final String email, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_EMAIL, email);

        AppRestClient.post(AppConstant.RELATIVE_URL_FORGOT_PASSWORD, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON responseString) {
                if (responseString.isResponseSuccessfully(listener))
                    listener.onSuccess("An email will be sent to you with reset password link");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void requestRegister(final String username, final String email, final String password, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USERNAME, username);
        params.put(AppConstant.KEY_EMAIL, email);
        params.put(AppConstant.KEY_PASSWORD, password);

        AppRestClient.post(AppConstant.RELATIVE_URL_REGISTER, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                parseResponseCustomer(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void changePassword(final int accountID, final String currentPass, final String newPass, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, String.valueOf(accountID));
        params.put(AppConstant.KEY_OLD_PASS, currentPass);
        params.put(AppConstant.KEY_NEW_PASS, newPass);

        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGE_PASSWORD, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        listener.onSuccess("Password updated successfully!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackError(e.getMessage(), listener);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void changeProfile(final CustomerJSON customerModel, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, String.valueOf(customerModel.id));
        params.put(AppConstant.KEY_EMAIL, customerModel.email);
        params.put(AppConstant.KEY_FIRST_NAME, customerModel.firstName);
        params.put(AppConstant.KEY_LAST_NAME, customerModel.lastName);
        params.put(AppConstant.KEY_PHONE_NUMBER, customerModel.phone);
        params.put(AppConstant.KEY_ADDRESS, customerModel.address);
        params.put(AppConstant.KEY_CITY, customerModel.city);
        params.put(AppConstant.KEY_COUNTRY, customerModel.country);
        params.put(AppConstant.KEY_ZIP, customerModel.zip);

        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGE_PROFILE, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                parseResponseCustomer(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void changeAvatar(int accountID, ImageFileModel model, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_USER_ID, accountID);
        try {
            String type = model.getMimeType();
            params.put(AppConstant.KEY_AVATAR, model.getFile(), type);
        } catch (FileNotFoundException e) {
            listener.onError("Can't change avatar!");
            return;
        }

        AppRestClient.post(AppConstant.RELATIVE_URL_CHANGE_AVATAR, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                parseResponseCustomer(response, listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    /**
     * @param response
     * @param listener
     */
    private static void parseResponseCustomer(ResponseJSON response, final APIResponseListener listener) {
        try {
            if (response.isResponseSuccessfully(listener)) {
                CustomerJSON customerJSON = new ObjectMapper().readValue(response.getContentString(), CustomerJSON.class);
                listener.onSuccess(customerJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(e.getMessage(), listener);
        }
    }

    /* ======================================= END CUSTOMER =======================================*/

    /* ======================================= START VIDEOS =======================================*/

    public static void getLatestVideos(final int pageNumber, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_VIDEO_LATEST;
        String url = String.format(fmUrl, pageNumber, AppConstant.LIMIT_VIDEOS_HOMES);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                if (response.isResponseSuccessfully(listener))
                    onVideosResponse(response.getContentNode().get("videos").toString(), listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getMostVideos(final int pageNumber, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_VIDEO_MOST;
        String url = String.format(fmUrl, pageNumber, AppConstant.LIMIT_VIDEOS_HOMES);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                if (response.isResponseSuccessfully(listener))
                    onVideosResponse(response.getContentNode().get("videos").toString(), listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getTrendingVideos(final int pageNumber, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_VIDEO_TRENDING;
        String url = String.format(fmUrl, pageNumber, AppConstant.LIMIT_VIDEOS_HOMES);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                if (response.isResponseSuccessfully(listener))
                    onVideosResponse(response.getContentString(), listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    private static void onVideosResponse(String response, APIResponseListener listener) {
        try {
            List<VideoJSON> listData = new ObjectMapper().readValue(response, new TypeReference<List<VideoJSON>>() {
            });
            listener.onSuccess(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCategories(final APIResponseListener listener) {
        AppRestClient.get(AppConstant.RELATIVE_URL_CATEGORIES, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        DataCategoryJSON categoryJSON = new ObjectMapper().readValue(response.getContentString(), DataCategoryJSON.class);
                        listener.onSuccess(categoryJSON);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getVideosByCategory(int categoryId, int pageNumber, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_GET_VIDEOS_BY_CATEGORY;
        String url = String.format(fmUrl, categoryId, pageNumber, AppConstant.LIMIT_VIDEOS_HOMES);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                if (response.isResponseSuccessfully(listener))
                    onVideosResponse(response.getContentString(), listener);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getUserLikeVideoState(int customerID, int videoID, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_GET_USER_LIKE_STATUS;
        String url = String.format(fmUrl, videoID, customerID);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        LikeStatusResponse data = new ObjectMapper().readValue(response.getContentString(), LikeStatusResponse.class);

                        listener.onSuccess(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void requestUserLikeVideo(int customerID, int videoID, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_VIDEO_ID, videoID);
        params.put(AppConstant.KEY_CUSTOMER_ID, customerID);

        AppRestClient.post(AppConstant.RELATIVE_URL_USER_LIKE_VIDEO, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        LikeStatusResponse data = new ObjectMapper().readValue(response.getContentString(), LikeStatusResponse.class);

                        listener.onSuccess(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }


    public static void getVideoDetailByID(int videoID, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_GET_VIDEO_BY_ID;
        String url = String.format(fmUrl, videoID);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        VideoJSON data = new ObjectMapper().readValue(response.getContentString(), VideoJSON.class);

                        listener.onSuccess(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void getVideoComments(int videoID, int pageNumber, final APIResponseListener listener) {
        String fmUrl = AppConstant.RELATIVE_URL_GET_VIDEO_COMMENTS;
        String url = String.format(fmUrl, videoID, pageNumber, AppConstant.LIMIT_VIDEO_COMMENT);

        AppRestClient.get(url, null, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        List<CommentJSON> listData = new ObjectMapper().readValue(response.getContentString(), new TypeReference<List<CommentJSON>>() {
                        });
                        listener.onSuccess(listData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    public static void postCommentVideo(int customerID, int videoID, String comment, final APIResponseListener listener) {
        RequestParams params = new RequestParams();
        params.put(AppConstant.KEY_VIDEO_ID, videoID);
        params.put(AppConstant.KEY_COMMENT_TEXT, comment);
        params.put(AppConstant.KEY_CUSTOMER_ID, customerID);

        AppRestClient.post(AppConstant.RELATIVE_URL_INSERT_COMMENT, params, new BaseJsonHttpResponseHandler<ResponseJSON>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, ResponseJSON response) {
                try {
                    if (response.isResponseSuccessfully(listener)) {
                        CommentJSON data = new ObjectMapper().readValue(response.getContentString(), CommentJSON.class);
                        listener.onSuccess(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, ResponseJSON errorResponse) {
                onError(throwable, listener);
            }

            @Override
            protected ResponseJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return onResponse(rawJsonData);
            }
        });
    }

    /* ======================================= END VIDEOS =======================================*/

    /* ======================================= COMMON =======================================*/

    private static void onError(Throwable throwable, final APIResponseListener listener) {
        callbackError(throwable.getMessage(), listener);

//        debugHeaders(LOG_TAG, headers);
//        debugStatusCode(LOG_TAG, statusCode);
//        debugThrowable(LOG_TAG, throwable);

//        if (errorResponse != null)
//            debugResponse(LOG_TAG, errorResponse);
    }

    private static ResponseJSON onResponse(String rawJsonData) throws IOException {
        ResponseJSON responseJSON = new ObjectMapper().readValue(rawJsonData, ResponseJSON.class);

        return responseJSON;
    }

    private static void callbackError(String message, final APIResponseListener listener) {
        if (listener != null)
            listener.onError(message);
    }

    /**
     * @param TAG
     * @param statusCode
     */
    protected static final void debugStatusCode(String TAG, int statusCode) {
        if (InspiusUtils.isProductionEnvironment())
            return;

        String msg = String.format(Locale.getDefault(), "Return Status Code: %d", statusCode);
        Logger.d(TAG, msg);
    }

    /**
     * @param TAG
     * @param headers
     */
    protected static final void debugHeaders(String TAG, Header[] headers) {
        if (InspiusUtils.isProductionEnvironment())
            return;

        if (headers != null) {
            Logger.d(TAG, "Return Headers:");
            StringBuilder builder = new StringBuilder();
            for (Header h : headers) {
                String _h = String.format(Locale.getDefault(), "%s : %s", h.getName(), h.getValue());
                Logger.d(TAG, _h);
                builder.append(_h);
                builder.append("\n");
            }
            Logger.d(TAG, builder.toString());
        }
    }

    /**
     * @param t
     * @return
     */
    protected static String throwableToString(Throwable t) {
        if (t == null)
            return null;

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * @param TAG
     * @param response
     */
    protected static final void debugResponse(String TAG, String response) {
        if (InspiusUtils.isProductionEnvironment())
            return;

        if (response != null) {
            Logger.d(TAG, "Response data:");
            Logger.d(TAG, response);
        }
    }

    protected static final void debugThrowable(String TAG, Throwable t) {
        if (!InspiusUtils.isProductionEnvironment())
            return;

        if (t != null) {
            Logger.d(TAG, "AsyncHttpClient returned error");
            Logger.d(TAG, throwableToString(t));
        }
    }

    /* ======================================= COMMON END=======================================*/
}