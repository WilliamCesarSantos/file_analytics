package com.fileanalytics.order;

import java.math.BigDecimal;

/**
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public class OrderItem {

    private final Long id;
    private final Integer quantity;
    private final BigDecimal salePrice;

    public OrderItem(Long id, Integer quantity, BigDecimal salePrice) {
        this.id = id;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

}
