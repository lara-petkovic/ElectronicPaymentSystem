package com.example.sep.dtos;

public class ClientSubscriptionDto {
    public String subscriptions;
    public String merchantId;
    public Long orderId;

    public String getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(String subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ClientSubscriptionDto(){}
    public ClientSubscriptionDto(String subscription, String merchantId, Long merchantOrderId) {
        this.subscriptions=subscription;
        this.merchantId=merchantId;
        this.orderId=merchantOrderId;
    }

    @Override
    public String toString() {
        return "{" +
                "subscriptions='" + subscriptions + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
