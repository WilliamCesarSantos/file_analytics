package com.fileanalytics.pojoconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Factory to produce {@link IPojoConverter} with identifier
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class PojoConverterFactory {

    private static final Logger logger = LoggerFactory.getLogger(PojoConverterFactory.class);
    private final Map<String, IPojoConverter> converters = new TreeMap<>(Comparator.comparing(String::toString));

    public PojoConverterFactory(@Autowired List<IPojoConverter> converters) {
        converters.forEach(it ->
                this.converters.put(it.getIdentifier(), it));
    }

    /**
     * Produces a new {@link IPojoConverter} by the identifier
     *
     * @param identifier
     * @param <T>
     * @return IPojoConverter
     * @throws IllegalStateException if none converter to identifier
     */
    public <T> IPojoConverter<T> create(final String identifier) {
        final IPojoConverter converter = converters.get(identifier);
        if (converter == null) {
            throw new IllegalStateException("Not found any factory to: " + identifier);
        }
        logger.debug("Found factory: {} to identifier: {}", converter.getClass(), identifier);
        return converter;
    }
}
