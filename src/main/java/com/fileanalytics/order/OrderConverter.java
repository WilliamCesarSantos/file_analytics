package com.fileanalytics.order;

import com.fileanalytics.pojoconverter.IPojoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Convert String array in {@link Order}. The position in array should be [1] = Identifier(long), [2] = items(String) and [3] = Salesman name.
 * Items in position two should be array with position [0] = Identifier (long), [1] = quantity (integer) and [2] = sales price
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class OrderConverter implements IPojoConverter<Order> {

    /**
     * Identifier customer in line of file. Value equals 003
     */
    private static final String ORDER_IDENTIFIER = "003";

    private static final Integer ORDER_ARRAY_SIZE = 4;
    private static final Integer ORDER_ID_POSITION = 1;
    private static final Integer ORDER_ITEMS_POSITION = 2;
    private static final Integer ORDER_SALESMAN_NAME_POSITION = 3;

    private static final Integer ORDER_ITEM_ARRAY_SIZE = 3;
    private static final String ORDER_ITEM_SEPARATOR = ",";
    private static final String ORDER_ITEM_FIELD_SEPARATOR = "-";
    private static final String ORDER_ITEM_START = "[";
    private static final String ORDER_ITEM_END = "]";
    private static final Integer ORDER_ITEM_ID_POSITION = 0;
    private static final Integer ORDER_ITEM_QUANTITY_POSITION = 1;
    private static final Integer ORDER_ITEM_SALES_PRICE_POSITION = 2;

    private static final Logger logger = LoggerFactory.getLogger(OrderConverter.class);

    /**
     * @return {@link #ORDER_IDENTIFIER}
     * @see IPojoConverter#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return ORDER_IDENTIFIER;
    }

    /**
     * @param values
     * @return Order
     * @see IPojoConverter#convert(String[])
     * @see OrderConverter
     */
    @Override
    public Optional<Order> convert(String[] values) {
        logger.debug("Converting {} to Order", values);
        return Optional.ofNullable(values)
                .filter(array -> array.length == ORDER_ARRAY_SIZE)
                .map(array -> {
                    final List<OrderItem> items = Optional.ofNullable(array[ORDER_ITEMS_POSITION])
                            .map(it -> it.replace(ORDER_ITEM_START, ""))
                            .map(it -> it.replace(ORDER_ITEM_END, ""))
                            .map(it -> it.split(ORDER_ITEM_SEPARATOR))
                            .map(it ->
                                    Stream.of(it)
                                            .map(item -> item.split(ORDER_ITEM_FIELD_SEPARATOR))
                                            .map(this::orderItem)
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toList())
                            ).orElseGet(ArrayList::new);

                    final BigDecimal total = items.stream()
                            .map(item -> item.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce((first, second) -> first.add(second)).orElse(BigDecimal.ZERO);

                    final String id = array[ORDER_ID_POSITION];
                    return new Order(
                            id != null ? Long.valueOf(id) : null,
                            array[ORDER_SALESMAN_NAME_POSITION],
                            items,
                            total);
                });
    }

    private OrderItem orderItem(final String[] values) {
        logger.debug("Converting {} to OrderItem", values);
        OrderItem item = null;
        if (values != null && values.length == ORDER_ITEM_ARRAY_SIZE) {
            final String id = values[ORDER_ITEM_ID_POSITION];
            final String quantity = values[ORDER_ITEM_QUANTITY_POSITION];
            final String salesPrice = values[ORDER_ITEM_SALES_PRICE_POSITION];

            item = new OrderItem(
                    id != null ? Long.valueOf(id) : null,
                    quantity != null ? Integer.valueOf(quantity) : 0,
                    salesPrice != null ? new BigDecimal(salesPrice) : BigDecimal.ZERO);
        }
        return item;
    }
}
