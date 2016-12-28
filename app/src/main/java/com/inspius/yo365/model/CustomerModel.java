package com.inspius.yo365.model;

/**
 * Created by Billy on 11/2/16.
 */

public class CustomerModel {
    private CustomerJSON customerJSON;

    public CustomerModel(CustomerJSON customerJSON) {
        this.customerJSON = customerJSON;
    }

    public int getCustomerID() {
        return customerJSON.id;
    }
}
