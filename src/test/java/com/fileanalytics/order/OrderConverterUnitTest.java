package com.fileanalytics.order;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Unit test for {@link OrderConverterUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class OrderConverterUnitTest {

    private OrderConverter converter = new OrderConverter();

    @Test
    public void getIdentifierReturnsStaticValue() {
        final String identifier = converter.getIdentifier();
        Assert.assertEquals("003", identifier);
    }

    @Test
    public void convertAcceptNullAndReturnOptionalEmpty() {
        final Optional<Order> converted = converter.convert(null);
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithTwoPositionReturnsOptionalEmpty() {
        final Optional<Order> converted = converter.convert(new String[]{"one", "two"});
        Assert.assertFalse(converted.isPresent());
    }

    @Test
    public void convertReceivedArrayWithFourPositionReturnsCustomer() {
        final String[] values = {"identifier", "123", "[1-34-10]", "seller"};
        final Optional<Order> converted = converter.convert(values);
        Assert.assertTrue(converted.isPresent());
    }

    @Test
    public void convertReturnsOrderWithIdentifier() {
        final String[] values = {null, "123", null, null};
        final Order converted = converter.convert(values).get();
        Assert.assertEquals((Long) 123L, converted.getId());
    }

    @Test
    public void convertReturnsOrderWithSeller() {
        final String[] values = {null, null, null, "seller"};
        final Order converted = converter.convert(values).get();
        Assert.assertEquals("seller", converted.getSalesManName());
    }

    @Test
    public void convertReturnsOrderWithTotalValue() {
        final String[] values = {"identifier", "123", "[1-2-10,2-30-2.50]", "seller"};
        final Order converted = converter.convert(values).get();
        Assert.assertTrue(BigDecimal.valueOf(95).compareTo(converted.getTotal()) == 0);
    }

    @Test
    public void convertReturnsOrderWithItems() {
        final String[] values = {null, null, "[1-10-100,2-30,3-40-3.10]", null};
        final Order converted = converter.convert(values).get();
        Assert.assertNotNull(converted.getItems());
    }

    @Test
    public void convertReturnsOrderWithItemsWithAllFields() {
        final String[] values = {null, null, "[1-10-100]", null};
        final Order converted = converter.convert(values).get();
        converted.getItems().forEach(item -> {
            Assert.assertNotNull(item.getId());
            Assert.assertEquals((Long) 1L, item.getId());
            Assert.assertEquals((Integer) 10, item.getQuantity());
            Assert.assertEquals(BigDecimal.valueOf(100), item.getSalePrice());
        });
    }
}
