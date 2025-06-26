package com.sepproject.paypalback.dtos;

public class PayPalCheckoutRequest {
    private String orderId;
    private String merchantId;
    private double amount;
    private String paypalOrderId;
    private String redirectUrl;

    public PayPalCheckoutRequest() {}

    public PayPalCheckoutRequest(String orderId, String merchantId, double amount, String paypalOrderId, String redirectUrl) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.paypalOrderId = paypalOrderId;
        this.redirectUrl = redirectUrl;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaypalOrderId() { return paypalOrderId; }
    public void setPaypalOrderId(String paypalOrderId) { this.paypalOrderId = paypalOrderId; }
    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }
}