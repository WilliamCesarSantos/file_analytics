package com.fileanalytics.dataanalytic;

import com.fileanalytics.customer.Customer;
import com.fileanalytics.pojoconverter.PojoConverterFactory;
import com.fileanalytics.order.Order;
import com.fileanalytics.salesman.SalesMan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Analyze file and generate data
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Service
public class DataAnalyzerController {

    private static final String LINE_SEPARATOR = "ç";
    private static final Integer IDENTIFIER_POSITION = 0;
    private static final Logger logger = LoggerFactory.getLogger(DataAnalyzerController.class);

    private final PojoConverterFactory factory;

    /**
     * @param factory
     */
    public DataAnalyzerController(@Autowired PojoConverterFactory factory) {
        this.factory = factory;
    }

    /**
     * Read content and generate data
     *
     * @param reader
     * @return AnalyzedData
     * @throws IOException
     */
    public AnalyzedData analyze(final BufferedReader reader) {
        Objects.requireNonNull(reader, "reader cannot be null");
        final Map<Class, Collection> objects = new HashMap<>();

        logger.debug("Reading values");

        reader.lines().forEach(line -> {
            logger.debug("Converting line: {}", line);
            final String[] values = line.split(LINE_SEPARATOR);
            final String identifier = values[IDENTIFIER_POSITION];

            factory.create(identifier).convert(values)
                    .ifPresent(obj -> {
                        logger.trace("Converter line: {} to object by identifier: {}", line, identifier);
                        if (!objects.containsKey(obj.getClass())) {
                            logger.trace("Type: {} not present publiin cache, put new list for it", obj.getClass());
                            objects.put(obj.getClass(), new ArrayList());
                        }
                        objects.get(obj.getClass()).add(obj);
                    });
        });

        final Collection<Customer> customers = objects.getOrDefault(Customer.class, Collections.emptyList());
        final Collection<SalesMan> salesMan = objects.getOrDefault(SalesMan.class, Collections.emptyList());
        final Collection<Order> orders = objects.getOrDefault(Order.class, Collections.emptyList());

        logger.info("Finished reading and was found {} customer, {} sales man and {} orders", customers.size(), salesMan.size(), orders.size());

        return new AnalyzedData(customers, salesMan, orders);
    }

    /**
     * Identifier and returns the order is more expensive
     *
     * @param orders
     * @return Order
     */
    public Optional<Order> moreExpensiveOrder(final Collection<Order> orders) {
        Objects.requireNonNull(orders, "orders cannot be null");
        return orders.stream()
                .max(Comparator.comparing(Order::getTotal));
    }

    /**
     * Identifier and returns the worst seller, using the total sold value as a goal
     *
     * @param orders
     * @return String - name seller
     */
    public Optional<String> worstSeller(final Collection<Order> orders) {
        Objects.requireNonNull(orders, "orders cannot be null");
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getSalesManName))
                .entrySet()
                .stream()
                .map(entry -> {
                    final String name = entry.getKey();
                    final BigDecimal value = entry.getValue().stream()
                            .map(Order::getTotal)
                            .reduce(BigDecimal.ZERO, (first, second) -> first.add(second));
                    return new Object[]{name, value};
                })
                .min(Comparator.comparing(it -> ((BigDecimal) it[1])))
                .map(it -> (String) it[0]);
    }

}
