package com.fileanalytics.order;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public class Order {

    private final Long id;
    private final String salesManName;
    private final List<OrderItem> items;
    private final BigDecimal total;

    public Order(Long id, String salesManName, List<OrderItem> items, BigDecimal total) {
        this.id = id;
        this.salesManName = salesManName;
        this.items = items;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

}
