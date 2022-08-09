package de.hsos.roomplanner.util.paramconverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import de.hsos.roomplanner.util.Dimension;

/**
 * @author Benno Steinkamp
 */
@Provider
public class DimensionConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.equals(Dimension.class)) {
            return (ParamConverter<T>) new DimensionConverter();
        }
        return null;
    }

}