package com.fileanalytics.salesman;

import com.fileanalytics.pojoconverter.IPojoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Convert String array in {@link SalesMan}. The position in array should be [1] = CPF, [2] = Name and [3] = Salary
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class SalesManConverter implements IPojoConverter<SalesMan> {

    /**
     * Identifier order in line of file. Value equals 001
     */
    private static final String SALES_MAN_IDENTIFIER = "001";
    private static final Integer SALES_MAN_ARRAY_SIZE = 4;
    private static final Integer SALES_MAN_CPF_POSITION = 1;
    private static final Integer SALES_MAN_NAME_POSITION = 2;
    private static final Integer SALES_MAN_SALARY_POSITION = 3;


    private static final Logger logger = LoggerFactory.getLogger(SalesManConverter.class);

    /**
     * @return {@link #SALES_MAN_IDENTIFIER}
     * @see IPojoConverter#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return SALES_MAN_IDENTIFIER;
    }

    /**
     * @param values
     * @return SalesMan
     * @see IPojoConverter#convert(String[])
     * @see SalesManConverter
     */
    @Override
    public Optional<SalesMan> convert(String[] values) {
        logger.debug("Converting {} to SalesMan", values);
        return Optional.ofNullable(values)
                .filter(array -> array.length == SALES_MAN_ARRAY_SIZE)
                .map(array -> {
                    final String cpf = array[SALES_MAN_CPF_POSITION];
                    final String name = array[SALES_MAN_NAME_POSITION];
                    final String salary = array[SALES_MAN_SALARY_POSITION];
                    return new SalesMan(cpf, name, salary != null ? new BigDecimal(salary) : null);
                });
    }
}
