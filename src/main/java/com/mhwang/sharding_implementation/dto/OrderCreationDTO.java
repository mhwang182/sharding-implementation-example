package com.mhwang.sharding_implementation.dto;

public class OrderCreationDTO {

    private Long customerId;

    private Long productSku;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductSku() {
        return productSku;
    }

    public void setProductSku(Long productSku) {
        this.productSku = productSku;
    }
}
