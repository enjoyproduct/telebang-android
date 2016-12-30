package com.inspius.yo365.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dev on 3/6/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerJSON {
    public int id;
    public String email;
    public String username;
    public String avatar;

    @JsonProperty("firstname")
    public String firstName;

    @JsonProperty("lastname")
    public String lastName;

    public String phone;

    @JsonProperty("address")
    public String address;

    public String city;
    public String country;
    public String zip;
    public int vip;

    public String getFullName() {
        String fullName = "";
        fullName = String.format("%s %s", firstName, lastName).trim();

        if (TextUtils.isEmpty(fullName))
            fullName = username.trim();

        if (TextUtils.isEmpty(fullName))
            fullName = "Guest";

        return fullName;
    }
}
