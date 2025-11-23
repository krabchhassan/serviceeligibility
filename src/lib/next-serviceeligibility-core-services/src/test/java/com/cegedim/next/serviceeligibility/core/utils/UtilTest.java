package com.cegedim.next.serviceeligibility.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilTest {

  @Test
  void shouldReturnDateFinOffline() {
    String dateFinOffline = Util.getDateFinOffline("2023-12-31", null, null, false);
    Assertions.assertEquals("2023-12-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-12-31", null, "2023-10-31", false);
    Assertions.assertEquals("2023-10-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-12-31", null, null, true);
    Assertions.assertEquals("2023-12-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-10-31", "2023-12-31", null, false);
    Assertions.assertEquals("2023-12-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-12-31", null, "2023-10-31", true);
    Assertions.assertEquals("2023-10-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-10-31", null, "2023-12-31", true);
    Assertions.assertEquals("2023-12-31", dateFinOffline);

    dateFinOffline = Util.getDateFinOffline("2023-10-31", "2023-12-31", null, true);
    Assertions.assertEquals("2023-10-31", dateFinOffline);
  }
}
