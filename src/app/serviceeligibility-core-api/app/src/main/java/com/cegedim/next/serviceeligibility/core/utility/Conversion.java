package com.cegedim.next.serviceeligibility.core.utility;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Conversion {

  private static final Log log = LogFactory.getLog(Conversion.class);

  private Conversion() {}

  public static XMLGregorianCalendar date2Gregorian(Date date) {
    try {
      DatatypeFactory df = DatatypeFactory.newInstance();
      GregorianCalendar gCalendar = new GregorianCalendar();
      gCalendar.setTime(date);
      XMLGregorianCalendar xmlCalendar = df.newXMLGregorianCalendar(gCalendar);
      xmlCalendar.setTimezone(gCalendar.getTimeZone().getOffset(date.getTime()) / (60 * 60 * 1000));
      return xmlCalendar;
    } catch (Exception e) {
      if (log.isDebugEnabled()) {
        log.debug(e.getMessage(), e);
      }
    }
    return null;
  }

  public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xmlCal) {
    try {
      GregorianCalendar c = xmlCal.toGregorianCalendar();
      return c.getTime();
    } catch (Exception e) {
      if (log.isDebugEnabled()) {
        log.debug(e.getMessage(), e);
      }
    }
    return null;
  }

  /**
   * Formatte chaine. minLength et/ou maxLength doit etre renseigne (ie non null). Complete si
   * besoin avec espaces de fin. Garde les premiers caracteres si la chaine a ete coupee.
   *
   * @param value null ou chaine en entree
   * @param minLength
   * @param maxLength
   * @return null si value est null, ou value modifiee
   */
  public static String formatString(String value, Integer minLength, Integer maxLength) {
    return formatString(value, minLength, maxLength, true);
  }

  /**
   * Formatte chaine. minLength et/ou maxLength doit etre renseigne (ie non null). Complete si
   * besoin avec espaces de fin et aligne a gauche ou a droite dans le cas ou la chaine a ete
   * coupee.
   *
   * @param value null ou chaine en entree
   * @param minLength
   * @param maxLength
   * @param premiers true pour garder les premiers caracteres, false pour garder les derniers
   * @return null si value est null, ou value modifiee
   */
  public static String formatString(
      String value, Integer minLength, Integer maxLength, boolean premiers) {
    if (value == null) {
      return null;
    }
    int max = value.length();
    if (maxLength != null) {
      max = maxLength;
    }
    int min = 0;
    if (minLength != null) {
      min = minLength;
    }
    String res;
    String substring;
    if (premiers) {
      substring = value.substring(0, Math.min(value.length(), max));
    } else {
      substring = value.substring(Math.max(value.length() - max, 0), value.length());
    }
    if (min > 0) {
      if (premiers) {
        res = String.format("%1$-" + min + "s", substring);
      } else {
        res = String.format("%1$" + min + "s", substring);
      }
    } else {
      res = substring;
    }
    return res;
  }
}
