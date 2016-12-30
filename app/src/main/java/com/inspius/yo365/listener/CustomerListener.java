package com.inspius.yo365.listener;

import com.inspius.yo365.model.CustomerJSON;

/**
 * Created by Billy on 10/7/15.
 */
public interface CustomerListener {
    void onCustomerLoggedIn(CustomerJSON customer);

    void onCustomerLogout();

    void onCustomerProfileChanged(CustomerJSON customer);
}
