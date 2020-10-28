package com.fileanalytics.customer;

import com.fileanalytics.pojoconverter.IPojoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Convert String array in {@link Customer}. The position in array should be [1] = CNPJ, [2] = Name and [3] = Business Area
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class CustomerConverter implements IPojoConverter<Customer> {

    /**
     * Identifier customer in line of file. Value equals 002
     */
    private static final String CUSTOMER_IDENTIFIER = "002";
    private static final Integer CUSTOMER_ARRAY_SIZE = 4;
    private static final Integer CUSTOMER_CNPJ_POSITION = 1;
    private static final Integer CUSTOMER_NAME_POSITION = 2;
    private static final Integer CUSTOMER_BUSINESS_AREA_POSITION = 3;

    private static final Logger logger = LoggerFactory.getLogger(CustomerConverter.class);

    /**
     * @return {@link #CUSTOMER_IDENTIFIER}
     * @see IPojoConverter#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return CUSTOMER_IDENTIFIER;
    }

    /**
     * @param values
     * @return Customer
     * @see IPojoConverter#convert(String[])
     * @see CustomerConverter
     */
    @Override
    public Optional<Customer> convert(String[] values) {
        logger.debug("Converting {} to Customer", values);
        return Optional.ofNullable(values)
                .filter(array -> array.length == CUSTOMER_ARRAY_SIZE)
                .map(array ->
                        new Customer(array[CUSTOMER_CNPJ_POSITION], array[CUSTOMER_NAME_POSITION], array[CUSTOMER_BUSINESS_AREA_POSITION]));
    }
}
