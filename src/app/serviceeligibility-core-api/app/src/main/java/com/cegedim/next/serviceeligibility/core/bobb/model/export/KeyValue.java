package com.cegedim.next.serviceeligibility.core.bobb.model.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class KeyValue<K, V> {

  private final K key;

  private final V value;

  public static <K, V> KeyValue<K, V> pair(final K key, final V value) {
    return new KeyValue<>(key, value);
  }
}
