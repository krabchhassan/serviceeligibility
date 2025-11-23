package com.cegedim.next.serviceeligibility.core.kafka.observability.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface AbstractEvent {

  static Map<String, Object> getData(final AbstractEvent event) {
    return Arrays.stream(event.getClass().getDeclaredFields())
        .map(Field::getName)
        .collect(
            Collectors.toMap(
                Function.identity(),
                f -> {
                  try {
                    return getReadMethod(f, event);
                  } catch (IntrospectionException
                      | InvocationTargetException
                      | IllegalAccessException e) {
                    throw new BuildEventException();
                  }
                }));
  }

  private static Object getReadMethod(final String propertyName, final AbstractEvent event)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    return new PropertyDescriptor(propertyName, event.getClass()).getReadMethod().invoke(event);
  }
}
