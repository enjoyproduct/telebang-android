package com.neo2.telebang.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 5/19/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionJSON {
    public int id;
    public String amount;
    public String card_number;
    public int time;
    public int user_id;

}
