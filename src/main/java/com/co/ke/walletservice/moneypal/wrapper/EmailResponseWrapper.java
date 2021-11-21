package com.co.ke.walletservice.moneypal.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailResponseWrapper {
    @JsonProperty("responseCode")
    private int responseCode;
    @JsonProperty("responseMessage")
    private String responseMessage;
}
