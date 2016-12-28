package com.inspius.yo365.listener;

import com.inspius.yo365.model.CustomerModel;

/**
 * Created by Billy on 10/7/15.
 */
public interface CustomerListener {
    public void onCustomerLoggedIn(CustomerModel customer);

    public void onCustomerLogout();
}
