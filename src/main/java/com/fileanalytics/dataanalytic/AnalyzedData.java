package com.fileanalytics.dataanalytic;

import com.fileanalytics.customer.Customer;
import com.fileanalytics.order.Order;
import com.fileanalytics.salesman.SalesMan;

import java.util.Collection;

/**
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public class AnalyzedData {

    private final Collection<Customer> customers;
    private final Collection<SalesMan> salesMan;
    private final Collection<Order> orders;

    public AnalyzedData(Collection<Customer> customers, Collection<SalesMan> salesMan, Collection<Order> orders) {
        this.customers = customers;
        this.salesMan = salesMan;
        this.orders = orders;
    }

    public Collection<Customer> getCustomers() {
        return customers;
    }

    public Collection<SalesMan> getSalesMan() {
        return salesMan;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

}
