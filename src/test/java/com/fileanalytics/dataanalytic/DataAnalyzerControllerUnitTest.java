package com.fileanalytics.dataanalytic;

import com.fileanalytics.customer.Customer;
import com.fileanalytics.order.Order;
import com.fileanalytics.pojoconverter.IPojoConverter;
import com.fileanalytics.pojoconverter.PojoConverterFactory;
import com.fileanalytics.salesman.SalesMan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Unit test for {@link DataAnalyzerControllerUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class DataAnalyzerControllerUnitTest {

    private BufferedReader reader;
    private PojoConverterFactory factory;
    private IPojoConverter converter;

    private DataAnalyzerController controller;

    @Before
    public void before() {
        reader = Mockito.mock(BufferedReader.class);
        Mockito.doReturn(Stream.empty()).when(reader).lines();

        converter = Mockito.mock(IPojoConverter.class);
        Mockito.doReturn(Optional.of(new Customer("cpnj","name","businessArea"))).when(converter).convert(Mockito.any());

        factory = Mockito.mock(PojoConverterFactory.class);
        Mockito.doReturn(converter).when(factory).create(Mockito.anyString());

        controller = new DataAnalyzerController(factory);
    }

    @Test
    public void analyzeEmptyReaderAndReturnsEmptyCustomerCollection() {
        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertNotNull(analyze.getCustomers());
        Assert.assertTrue(analyze.getCustomers().isEmpty());
    }

    @Test
    public void analyzeEmptyReaderAndReturnsEmptySalesManCollection() {
        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertNotNull(analyze.getSalesMan());
        Assert.assertTrue(analyze.getSalesMan().isEmpty());
    }

    @Test
    public void analyzeEmptyReaderAndReturnsEmptyOrderCollection() {
        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertNotNull(analyze.getOrders());
        Assert.assertTrue(analyze.getOrders().isEmpty());
    }

    @Test
    public void analyzeCallsFactoryCreateByIdentifierRegister() {
        Mockito.doReturn(Stream.of("001ç1234567891234çPedroç50000")).when(reader).lines();

        controller.analyze(reader);

        Mockito.verify(factory, Mockito.times(1)).create("001");
    }

    @Test
    public void analyzeCallsPojoConverterConvert() {
        Mockito.doReturn(Stream.of("001ç1234567891234çPedroç50000")).when(reader).lines();

        controller.analyze(reader);

        Mockito.verify(converter, Mockito.times(1)).convert(Mockito.any());
    }

    @Test
    public void analyzeReaderAndReturnCustomerCollection() {
        Mockito.doReturn(Optional.of(new Customer("cpnj","name","businessArea"))).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("002ç2345675434544345çJose da SilvaçRural")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertFalse(analyze.getCustomers().isEmpty());
    }

    @Test
    public void analyzeReaderAndReturnCustomer() {
        final Customer customer = new Customer("cpnj","name","businessArea");
        Mockito.doReturn(Optional.of(customer)).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("002ç2345675434544345çJose da SilvaçRural")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertEquals(customer, analyze.getCustomers().stream().findFirst().get());
    }

    @Test
    public void analyzeReaderAndReturnSalesManCollection() {
        Mockito.doReturn(Optional.of(new SalesMan("cpf","name", BigDecimal.TEN))).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("001ç1234567891234çPedroç50000")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertFalse(analyze.getSalesMan().isEmpty());
    }

    @Test
    public void analyzeReaderAndReturnSalesMan() {
        final SalesMan salesMan = new SalesMan("cpf","name", BigDecimal.TEN);
        Mockito.doReturn(Optional.ofNullable(salesMan)).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("unit-test")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertEquals(salesMan, analyze.getSalesMan().stream().findFirst().get());
    }

    @Test
    public void analyzeReaderAndReturnOrderCollection() {
        Mockito.doReturn(Optional.of(new Order(0L, "salesManName", Collections.emptyList(), BigDecimal.ZERO))).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertFalse(analyze.getOrders().isEmpty());
    }

    @Test
    public void analyzeReaderAndReturnOrder() {
        final Order order = new Order(0L, "salesManName", Collections.emptyList(), BigDecimal.ZERO);
        Mockito.doReturn(Optional.of(order)).when(converter).convert(Mockito.any());
        Mockito.doReturn(Stream.of("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro")).when(reader).lines();

        final AnalyzedData analyze = controller.analyze(reader);
        Assert.assertEquals(order, analyze.getOrders().stream().findFirst().get());
    }

    @Test(expected = NullPointerException.class)
    public void analyzeDoesNotAcceptNull() {
        controller.analyze(null);
    }

    @Test
    public void moreExpensiveOrderReturnsEmptyOptionalWhenCollectionIsEmpty() {
        final Optional<Order> order = controller.moreExpensiveOrder(Collections.emptyList());
        Assert.assertFalse(order.isPresent());
    }

    @Test
    public void moreExpensiveOrderReturnsIt() {
        final Random random = new Random();
        final Order orderOne = new Order(random.nextLong(), "unit", Collections.emptyList(), BigDecimal.TEN);
        final Order orderTwo = new Order(random.nextLong(), "test", Collections.emptyList(), BigDecimal.valueOf(100));
        final Order orderThree = new Order(random.nextLong(), "unit-test", Collections.emptyList(), BigDecimal.valueOf(500));

        final Optional<Order> order = controller.moreExpensiveOrder(Arrays.asList(orderOne, orderTwo, orderThree));
        Assert.assertEquals(orderThree, order.get());
    }

    @Test(expected = NullPointerException.class)
    public void moreExpensiveOrderDoesNotAcceptNull() {
        controller.moreExpensiveOrder(null);
    }

    @Test
    public void worstSellerReturnsEmptyOptionalWhenCollectionIsEmpty() {
        final Optional<String> worstSeller = controller.worstSeller(Collections.emptyList());
        Assert.assertFalse(worstSeller.isPresent());
    }

    @Test
    public void worstSellerReturnsIt() {
        final Random random = new Random();
        final Order orderOne = new Order(random.nextLong(), "unit", Collections.emptyList(), BigDecimal.TEN);
        final Order orderTwo = new Order(random.nextLong(), "test", Collections.emptyList(), BigDecimal.valueOf(100));
        final Order orderThree = new Order(random.nextLong(), "unit-test", Collections.emptyList(), BigDecimal.valueOf(500));
        final Order orderFour = new Order(random.nextLong(), "unit", Collections.emptyList(), BigDecimal.TEN);

        final Optional<String> worstSeller = controller.worstSeller(Arrays.asList(orderOne, orderTwo, orderThree, orderFour));
        Assert.assertEquals("unit", worstSeller.get());
    }
}
