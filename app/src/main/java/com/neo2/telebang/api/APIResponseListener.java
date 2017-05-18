package com.neo2.telebang.api;

/**
 * Created by it.kupi on 5/30/2015.
 */
public interface APIResponseListener {
    void onError(String message);
    void onSuccess(Object results);
}
