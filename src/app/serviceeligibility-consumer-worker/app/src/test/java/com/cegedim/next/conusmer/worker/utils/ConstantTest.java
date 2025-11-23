package com.cegedim.next.conusmer.worker.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cegedim.next.consumer.worker.utils.Constants;
import org.junit.jupiter.api.Test;

class ConstantTest {
  @Test
  void testKafkaKeyGeneration() {
    Integer a = Constants.stringToInt("declaratn123");
    Integer b = Constants.stringToInt("123con;qsdf  ");
    assertEquals(246, a + b);
  }
}
