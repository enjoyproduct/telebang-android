package com.neo2.telebang.listener;

import com.neo2.telebang.model.CustomerJSON;

/**
 * Created by Billy on 10/7/15.
 */
public interface CustomerListener {
    void onCustomerLoggedIn(CustomerJSON customer);

    void onCustomerLogout();

    void onCustomerProfileChanged(CustomerJSON customer);
}
