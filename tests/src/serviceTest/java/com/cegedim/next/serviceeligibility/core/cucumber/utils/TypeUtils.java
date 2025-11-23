package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

@UtilityClass
public class TypeUtils {

  public static <T, U> ParameterizedTypeReference<Map<T, U>> mapParamType(
      Class<T> tClass, Class<U> uClass) {
    return ParameterizedTypeReference.forType(
        ResolvableType.forClassWithGenerics(Map.class, tClass, uClass).getType());
  }

  public static <T> ParameterizedTypeReference<List<T>> listParamType(Class<T> tClass) {
    return ParameterizedTypeReference.forType(
        ResolvableType.forClassWithGenerics(List.class, tClass).getType());
  }

  public static <T> ParameterizedTypeReference<T> simpleParamType(Class<T> tClass) {
    return ParameterizedTypeReference.forType(ResolvableType.forClass(tClass).getType());
  }
}
