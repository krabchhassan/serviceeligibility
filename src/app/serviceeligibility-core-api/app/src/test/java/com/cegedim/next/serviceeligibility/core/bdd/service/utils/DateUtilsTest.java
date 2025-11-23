package com.cegedim.next.serviceeligibility.core.bdd.service.utils;

import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDateTime;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  @Test
  void should_create_date() {
    XMLGregorianCalendar xmlDate =
        DateUtils.stringToXMLGregorianCalendar("2011-01-01", Constants.YYYY_MM_DD);
    Assertions.assertEquals(2011, xmlDate.getYear());
    Assertions.assertEquals(1, xmlDate.getMonth());
    Assertions.assertEquals(1, xmlDate.getDay());
  }

  @Test
  void should_create_string() {
    String date = "2011-02-02";
    XMLGregorianCalendar xmlDate =
        DateUtils.stringToXMLGregorianCalendar(date, Constants.YYYY_MM_DD);
    Assertions.assertEquals(2011, xmlDate.getYear());
    Assertions.assertEquals(2, xmlDate.getMonth());
    Assertions.assertEquals(2, xmlDate.getDay());

    Assertions.assertEquals(
        date, DateUtils.convertXmlGregorianToString(xmlDate, Constants.YYYY_MM_DD));
  }

  @Test
  void test_isPeriodeValide() {
    LocalDateTime dateDebutPeriodeOK = LocalDateTime.of(2019, 12, 31, 0, 0);
    LocalDateTime dateFinPeriodeOK = LocalDateTime.of(2020, 1, 1, 0, 0);
    LocalDateTime dateDebutPeriodeNOK = LocalDateTime.of(2020, 1, 1, 0, 0);
    LocalDateTime dateFinPeriodeNOK = LocalDateTime.of(2019, 12, 31, 0, 0);
    Assertions.assertTrue(DateUtils.isPeriodeValide(dateDebutPeriodeOK, dateFinPeriodeOK));
    Assertions.assertFalse(DateUtils.isPeriodeValide(dateDebutPeriodeNOK, dateFinPeriodeNOK));
  }

  @Test
  void testOverlapp() {
    Assertions.assertTrue(
        DateUtils.isOverlapping("2020/01/01", "2020/12/31", "2020/01/01", "2022/12/31"));
    Assertions.assertTrue(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", "2020/12/31", "2020/01/01", "2022/12/31"));

    Assertions.assertFalse(
        DateUtils.isOverlapping("2020/01/01", "2020/12/31", "2021/01/01", "2022/12/31"));
    Assertions.assertFalse(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", "2020/12/31", "2021/01/01", "2022/12/31"));

    Assertions.assertFalse(
        DateUtils.isOverlapping("2020/01/01", "2020/12/31", "2022/01/01", "2022/12/31"));
    Assertions.assertFalse(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", "2020/12/31", "2022/01/01", "2022/12/31"));

    Assertions.assertTrue(
        DateUtils.isOverlapping("2020/01/01", "2020/11/30", "2020/10/20", "2022/12/31"));
    Assertions.assertTrue(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", "2020/11/30", "2020/10/20", "2022/12/31"));

    Assertions.assertTrue(DateUtils.isOverlapping("2020/01/01", null, "2020/10/20", "2022/12/31"));
    Assertions.assertTrue(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", null, "2020/10/20", "2022/12/31"));

    Assertions.assertTrue(DateUtils.isOverlapping("2020/01/01", "2022/12/31", "2020/10/20", null));
    Assertions.assertTrue(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", "2022/12/31", "2020/10/20", null));

    Assertions.assertTrue(DateUtils.isOverlapping("2020/01/01", null, "2020/10/20", null));
    Assertions.assertTrue(
        DateUtils.isOverlappingSlashPeriod("2020/01/01", null, "2020/10/20", null));
  }
}
