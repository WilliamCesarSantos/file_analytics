package com.fileanalytics.salesman;

import java.math.BigDecimal;

/**
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public class SalesMan {

    private final String cpf;
    private final String name;
    private final BigDecimal salary;

    public SalesMan(String cpf, String name, BigDecimal salary) {
        this.cpf = cpf;
        this.name = name;
        this.salary = salary;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

}
