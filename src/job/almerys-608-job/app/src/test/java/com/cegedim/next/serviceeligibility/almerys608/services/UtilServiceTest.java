package com.cegedim.next.serviceeligibility.almerys608.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class UtilServiceTest {

  @Test
  void testNumber() {
    Integer retour = UtilService.resizeNumericField(120, 2);
    Assertions.assertEquals(retour, 20);

    Assertions.assertNull(UtilService.resizeNumericField(null, 2));

    retour = UtilService.resizeNumericField(1230101, 6);
    Assertions.assertEquals(retour, 230101);

    retour = UtilService.resizeNumericField(2147483620, 10);
    Assertions.assertEquals(retour, 2147483620);
  }
}
