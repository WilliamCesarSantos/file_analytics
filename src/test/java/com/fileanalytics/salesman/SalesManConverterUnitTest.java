package com.fileanalytics.salesman;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Unit test for {@link SalesManConverterUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class SalesManConverterUnitTest {

    private SalesManConverter converter = new SalesManConverter();

    @Test
    public void getIdentifierReturnsStaticValue() {
        final String identifier = converter.getIdentifier();
        Assert.assertEquals("001", identifier);
    }

    @Test
    public void convertAcceptNullAndReturnOptionalEmpty() {
        final Optional<SalesMan> converted = converter.convert(null);
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithTwoPositionReturnsOptionalEmpty() {
        final Optional<SalesMan> converted = converter.convert(new String[]{"one", "two"});
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithFourPositionReturnsCustomer() {
        final String[] values = {"identifier", "cpf", "name", "1.0"};
        final Optional<SalesMan> converted = converter.convert(values);
        Assert.assertTrue(converted.isPresent());
    }

    @Test
    public void convertReturnsSalesmanWithCnpj() {
        final String[] values = {null, "cpf", null, null};
        final SalesMan converted = converter.convert(values).get();
        Assert.assertEquals("cpf", converted.getCpf());
    }

    @Test
    public void convertReturnsSalesmanWithName() {
        final String[] values = {null, null, "name", null};
        final SalesMan converted = converter.convert(values).get();
        Assert.assertEquals("name", converted.getName());
    }

    @Test
    public void convertReturnsSalesmanWithBusinessArea() {
        final String[] values = {null, null, null, "5.0"};
        final SalesMan converted = converter.convert(values).get();
        Assert.assertEquals(BigDecimal.valueOf(5.0), converted.getSalary());
    }
}
