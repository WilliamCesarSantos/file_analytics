package com.fileanalytics.pojoconverter;

import java.util.Optional;

/**
 * Convert string array to pojo
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public interface IPojoConverter<T> {

    /**
     * Identifier convert to received value
     *
     * @return String
     */
    String getIdentifier();

    /**
     * Values to convert in pojo
     *
     * @param values
     * @return Any object
     */
    Optional<T> convert(final String[] values);

}
