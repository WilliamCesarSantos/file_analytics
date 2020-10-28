package com.fileanalytics.pojoconverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit test for {@link PojoConverterFactoryUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class PojoConverterFactoryUnitTest {

    private final String identifier = "unit-test";
    private IPojoConverter<Object> converter;
    private PojoConverterFactory factory;

    @Before
    public void before() {
        converter = Mockito.mock(IPojoConverter.class);
        Mockito.doReturn(identifier).when(converter).getIdentifier();

        factory = new PojoConverterFactory(Arrays.asList(converter));
    }

    @Test
    public void createPojoConverterByExistentIdentifier() {
        final IPojoConverter<Object> found = factory.create(identifier);
        Assert.assertEquals(converter, found);
    }

    @Test(expected = IllegalStateException.class)
    public void createPojoConverterByNonExistentIdentifierThrowsException() {
        factory.create("pojo-converter");
    }
}
