package com.adri.economy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class OperationMessageDTO implements Serializable {

    private long accountId;
    private long operationId;

    public OperationMessageDTO(@JsonProperty("operationId") long operationId,
                               @JsonProperty("accountId") long accountId){
        this.accountId = accountId;
        this.operationId = operationId;
    }

    @Override
    public String toString() {
        return "OperationMessageDTO{" +
                "operationId=" + operationId +
                ", accountId=" + accountId +
                '}';
    }
}
