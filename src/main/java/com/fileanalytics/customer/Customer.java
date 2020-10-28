package com.fileanalytics.customer;

/**
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public class Customer {

    private final String cnpj;
    private final String name;
    private final String businessArea;

    public Customer(String cnpj, String name, String businessArea) {
        this.cnpj = cnpj;
        this.name = name;
        this.businessArea = businessArea;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getName() {
        return name;
    }

    public String getBusinessArea() {
        return businessArea;
    }

}
