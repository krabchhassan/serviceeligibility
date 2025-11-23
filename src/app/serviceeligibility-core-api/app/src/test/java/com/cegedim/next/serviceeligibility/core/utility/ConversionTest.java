package com.cegedim.next.serviceeligibility.core.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConversionTest {

  @Test
  void testFormatString() {
    String azertString = "azert";

    Assertions.assertEquals("test     ", Conversion.formatString("test", 9, 15));
    Assertions.assertEquals("         ", Conversion.formatString("", 9, 15));
    Assertions.assertEquals(
        "test grandeur n", Conversion.formatString("test grandeur nature", 9, 15));
    Assertions.assertEquals("t", Conversion.formatString("test", 0, 1));
    Assertions.assertEquals("te", Conversion.formatString("test", 0, 2));
    Assertions.assertEquals("20000101", Conversion.formatString("20000101", 8, 8));
    Assertions.assertEquals("  ", Conversion.formatString(" ", 2, 10));
    Assertions.assertEquals("  ", Conversion.formatString(" ", 2, null));
    Assertions.assertEquals(azertString, Conversion.formatString("azerty12", null, 5));
    Assertions.assertEquals(azertString, Conversion.formatString("azerty1", 0, 5, true));
    Assertions.assertEquals("zerty", Conversion.formatString("azerty", 0, 5, false));
    Assertions.assertEquals(azertString, Conversion.formatString(azertString, 0, 5, false));
    Assertions.assertEquals("azer", Conversion.formatString("azer", 0, 5, false));
    Assertions.assertEquals("azer", Conversion.formatString("azer", 0, 5, false));
    Assertions.assertEquals(" aze", Conversion.formatString("aze", 4, 5, false));
  }
}
