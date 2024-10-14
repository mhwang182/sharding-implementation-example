package com.mhwang.sharding_implementation.dto;

public class OrderCreationDTO {

    private String customerId;

    private Long productSku;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getProductSku() {
        return productSku;
    }

    public void setProductSku(Long productSku) {
        this.productSku = productSku;
    }
}
