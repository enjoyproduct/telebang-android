package com.inspius.yo365.api;

/**
 * Created by it.kupi on 5/30/2015.
 */
public interface APIResponseListener {
    public void onError(String message);
    public void onSuccess(Object results);
}
