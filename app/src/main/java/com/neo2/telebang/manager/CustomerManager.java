package com.neo2.telebang.manager;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.login.LoginManager;
import com.inspius.coreapp.helper.InspiusSharedPrefUtils;
import com.neo2.telebang.api.APIResponseListener;
import com.neo2.telebang.api.RPC;
import com.neo2.telebang.app.AppConstant;
import com.neo2.telebang.app.GlobalApplication;
import com.neo2.telebang.listener.CustomerListener;
import com.neo2.telebang.model.CustomerJSON;
import com.neo2.telebang.model.ImageFileModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 11/9/15.
 */
public class CustomerManager {
    private static CustomerManager mInstance;
    private AppConstant.LOGIN_TYPE stateLogin = AppConstant.LOGIN_TYPE.NOT_LOGIN;
    private CustomerJSON customerModel;
    private List<CustomerListener> listeners = new ArrayList<>();
    private Context mContext;

    public static synchronized CustomerManager getInstance() {
        if (mInstance == null)
            mInstance = new CustomerManager();

        return mInstance;
    }

    public CustomerManager() {
        this.mContext = GlobalApplication.getAppContext();
    }

    /**
     * @return
     */
    public boolean isLogin() {
        if (stateLogin == null)
            return false;

        return stateLogin != AppConstant.LOGIN_TYPE.NOT_LOGIN;

    }

    public AppConstant.LOGIN_TYPE getLoginCacheType() {
        String username = getUsername();
        String password = getPassword();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
            return AppConstant.LOGIN_TYPE.SYSTEM;

        if (!TextUtils.isEmpty(getFacebookAccessToken()))
            return AppConstant.LOGIN_TYPE.FACEBOOK;

        return AppConstant.LOGIN_TYPE.NOT_LOGIN;
    }

    private void logoutFacebook(final APIResponseListener listener) {
        stateLogin = AppConstant.LOGIN_TYPE.NOT_LOGIN;
        InspiusSharedPrefUtils.removeFromPrefs(mContext, AppConstant.KEY_ACCESS_TOKEN);

        LoginManager.getInstance().logOut();

        listener.onSuccess(true);
        notificationStateLogin();
    }

    private void parseLoginSystemSuccess(String username, String password, Object results, APIResponseListener listener) {
        customerModel = (CustomerJSON) results;
        updateLoginSystem(username, password);
        updateSubscription(customerModel.paystack_auth_code, customerModel.subscribed_date);
        if (listener != null)
            listener.onSuccess(customerModel);
    }

    private void parseLoginFacebookSuccess(String accessToken, Object results, APIResponseListener listener) {
        customerModel = (CustomerJSON) results;

        updateLoginFacebook(accessToken);
        updateSubscription(customerModel.paystack_auth_code, customerModel.subscribed_date);
        if (listener != null)
            listener.onSuccess(customerModel);
    }
    /**
     * =============================================================================================
     */

    /**
     * @param listener
     */
    public void subscribeStateLogin(CustomerListener listener) {
        if (listeners.contains(listener))
            return;

        listeners.add(listener);
    }

    /**
     * @param listener
     */
    public void unSubscribeStateLogin(CustomerListener listener) {
        listeners.remove(listener);
    }

    /**
     * @param listener
     * @return
     */
    public boolean callAutoLoginRequest(final APIResponseListener listener) {
        AppConstant.LOGIN_TYPE type = getLoginCacheType();

        switch (type) {
            case NOT_LOGIN:
                if (listener != null)
                    listener.onError("Not Exit Account");
                break;

            case SYSTEM:
                String email = getUsername();
                String password = getPassword();
                callLogin(true, email, password, listener);
                break;

            case FACEBOOK:
                String accessToken = getFacebookAccessToken();
                callLoginFacebook(accessToken, listener);
                break;
        }

        return true;
    }

    /**
     * @param listener
     */
    public void callLogout(APIResponseListener listener) {
        switch (stateLogin) {
            case NOT_LOGIN:
                listener.onSuccess(true);
                break;

            case SYSTEM:
                stateLogin = AppConstant.LOGIN_TYPE.NOT_LOGIN;

                InspiusSharedPrefUtils.removeFromPrefs(mContext, AppConstant.KEY_USERNAME);
                InspiusSharedPrefUtils.removeFromPrefs(mContext, AppConstant.KEY_PASSWORD);
                listener.onSuccess(true);
                notificationStateLogin();
                break;

            case FACEBOOK:
                logoutFacebook(listener);
                break;
        }
    }

    /**
     * @param username
     * @param password
     * @param listener
     */
    public void callLogin(final boolean isRemember, final String username, final String password, final APIResponseListener listener) {
        RPC.requestAuthentic(username, password, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stateLogin = AppConstant.LOGIN_TYPE.NOT_LOGIN;

                if (listener != null)
                    listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                parseLoginSystemSuccess(username, password, results, listener);

                if (!isRemember) {
                    InspiusSharedPrefUtils.removeFromPrefs(mContext, AppConstant.KEY_USERNAME);
                    InspiusSharedPrefUtils.removeFromPrefs(mContext, AppConstant.KEY_PASSWORD);
                }
            }
        });
    }

    /**
     * @param username
     * @param email
     * @param password
     * @param listener
     */
    public void callRegister(final String username, final String email, final String password, final APIResponseListener listener) {
        RPC.requestRegister(username, email, password, new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                parseLoginSystemSuccess(username, password, results, listener);
            }
        });
    }

    /**
     * @param accessToken
     * @param listener
     */
    public void callLoginFacebook(final String accessToken, final APIResponseListener listener) {
        RPC.signInWithFacebook(accessToken, new APIResponseListener() {
            @Override
            public void onError(String message) {
                stateLogin = AppConstant.LOGIN_TYPE.NOT_LOGIN;
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                parseLoginFacebookSuccess(accessToken, results, listener);
            }
        });
    }

    /**
     * @param currentPass
     * @param newPass
     * @param listener
     */
    public void callChangePassword(final String currentPass, final String newPass, final APIResponseListener listener) {
        RPC.changePassword(getAccountID(), currentPass, newPass, new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                updatePassword(newPass);
                listener.onSuccess(results);
            }
        });
    }
    /**
     * @param paystack_auth_code
     * @param timestamp
     * @param listener
     */
    public void callUpdateSubscription(String cardNum, final String paystack_auth_code, final int timestamp, final int type, final APIResponseListener listener) {
        RPC.updateSubscription(getAccountID(), cardNum,  paystack_auth_code, timestamp, type, new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                updateSubscription(paystack_auth_code, timestamp);
                listener.onSuccess(results);
            }
        });
    }
    /**
     * @param customerUpdate
     * @param listener
     */
    public void callChangeProfile(CustomerJSON customerUpdate, final APIResponseListener listener) {
        customerUpdate.id = getAccountID();

        RPC.changeProfile(customerUpdate, new APIResponseListener() {
            @Override
            public void onError(String message) {

                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                customerModel = (CustomerJSON) results;
                listener.onSuccess(customerModel);

                for (CustomerListener listener : listeners)
                    listener.onCustomerProfileChanged(customerModel);
            }
        });
    }

    public void callChangeAvatar(ImageFileModel imageFileModel, final APIResponseListener listener) {
        RPC.changeAvatar(getAccountID(), imageFileModel, new APIResponseListener() {
            @Override
            public void onError(String message) {
                listener.onError(message);
            }

            @Override
            public void onSuccess(Object results) {
                customerModel = (CustomerJSON) results;
                listener.onSuccess(customerModel);

                for (CustomerListener listener : listeners)
                    listener.onCustomerProfileChanged(customerModel);
            }
        });
    }

    /**
     * @return
     */
    public CustomerJSON getCustomerJSON() {
        if (!isLogin())
            return null;

        return customerModel;
    }

    /**
     * @return
     */
    public int getAccountID() {
        if (!isLogin() || customerModel == null)
            return -1;

        return customerModel.id;
    }

    public boolean isPremiumAccount() {
        if (!isLogin() || customerModel == null)
            return false;

        return (customerModel.vip == 1);
    }


    /**
     * @return
     */
    public String getUsername() {
        return InspiusSharedPrefUtils.getFromPrefs(mContext, AppConstant.KEY_USERNAME, "");
    }

    public String getPassword() {
        return InspiusSharedPrefUtils.getFromPrefs(mContext, AppConstant.KEY_PASSWORD, "");
    }

    /**
     * ===========================================================================================
     */

    private void updateLoginSystem(String username, String password) {
        stateLogin = AppConstant.LOGIN_TYPE.SYSTEM;
        InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_USERNAME, username);
        updatePassword(password);
        notificationStateLogin();
    }

    private void updateLoginFacebook(String accessToken) {
        stateLogin = AppConstant.LOGIN_TYPE.FACEBOOK;
        InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_ACCESS_TOKEN, accessToken);
        notificationStateLogin();
    }

    private void updatePassword(String password) {
        InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_PASSWORD, password);
    }
    public void updateSubscription(String code, int date) {
        InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_PAYSTACK_AUTH_CODE, code);
        InspiusSharedPrefUtils.saveToPrefs(mContext, AppConstant.KEY_SUBSCRIBED_DATE, date);
    }
    private void notificationStateLogin() {
        for (CustomerListener listener : listeners) {
            if (stateLogin == AppConstant.LOGIN_TYPE.NOT_LOGIN)
                listener.onCustomerLogout();
            else
                listener.onCustomerLoggedIn(customerModel);
        }
    }

    public String getFacebookAccessToken() {
        return InspiusSharedPrefUtils.getFromPrefs(mContext, AppConstant.KEY_ACCESS_TOKEN, "");
    }
}
