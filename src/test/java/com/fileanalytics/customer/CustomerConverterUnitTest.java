package com.fileanalytics.customer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Unit test for {@link CustomerConverter}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class CustomerConverterUnitTest {

    private CustomerConverter converter = new CustomerConverter();

    @Test
    public void getIdentifierReturnsStaticValue() {
        final String identifier = converter.getIdentifier();
        Assert.assertEquals("002", identifier);
    }

    @Test
    public void convertAcceptNullAndReturnOptionalEmpty() {
        final Optional<Customer> converted = converter.convert(null);
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithTwoPositionReturnsOptionalEmpty() {
        final Optional<Customer> converted = converter.convert(new String[]{"one", "two"});
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithFourPositionReturnsCustomer() {
        final String[] values = {"identifier", "cnpj", "name", "business_area"};
        final Optional<Customer> converted = converter.convert(values);
        Assert.assertTrue(converted.isPresent());
    }

    @Test
    public void convertReturnsCustomerWithCnpj() {
        final String[] values = {null, "cnpj", null, null};
        final Customer converted = converter.convert(values).get();
        Assert.assertEquals("cnpj", converted.getCnpj());
    }

    @Test
    public void convertReturnsCustomerWithName() {
        final String[] values = {null, null, "name", null};
        final Customer converted = converter.convert(values).get();
        Assert.assertEquals("name", converted.getName());
    }

    @Test
    public void convertReturnsCustomerWithBusinessArea() {
        final String[] values = {null, null, null, "business_area"};
        final Customer converted = converter.convert(values).get();
        Assert.assertEquals("business_area", converted.getBusinessArea());
    }
}
