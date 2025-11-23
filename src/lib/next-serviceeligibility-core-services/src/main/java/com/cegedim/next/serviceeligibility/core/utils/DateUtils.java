package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionTechnique;
import jakarta.annotation.Nullable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.function.Function;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class DateUtils {

  private static final DatatypeFactory datatypeFactory;

  static {
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new ExceptionTechnique("Init Error!", e);
    }
  }

  private DateUtils() {}

  public static final String FORMATTERSLASHED = "yyyy/MM/dd";

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  public static final String DD_MM_YY = "dd/MM/yyyy";
  public static final String DD_MM_YY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

  public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";

  public static final DateTimeFormatter FORMATTER_DATETIME =
      DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM);

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static final String YYYYMMDD = "yyyyMMdd";

  public static final String IDENTIFIANT_BATCH_FORMAT_DATE = "yyyyMMdd_HHmmss";
  public static final String BATCH_FILE_NAME_FORMAT_DATE = "yyyyMMddHHmmss";

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD);

  public static final DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern(YYYYMMDD);

  public static final DateTimeFormatter YYYY_MM_DD_T_HH_MM_SS_SSS_Z =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  public static final DateTimeFormatter YYYY_MM_DD_T_HH_MM_SS_SSSSSS_Z =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

  public static final DateTimeFormatter SLASHED_FORMATTER =
      DateTimeFormatter.ofPattern(FORMATTERSLASHED);

  public static final String YYYY_MM_DD_T_HH_MM_SS_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  public static final DateTimeFormatter HH_MM_SS_FORMATTER =
      DateTimeFormatter.ofPattern("HH:mm:ss");

  public static String generateDate() {
    return LocalDateTime.now(ZoneOffset.UTC).format(YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
  }

  private static Map<String, DateTimeFormatter> dateTimeFormatterHashMap = new HashMap<>();

  private static DateTimeFormatter getDateTimeFormatter(String format, boolean isLenient) {
    DateTimeFormatter dateTimeFormatter = dateTimeFormatterHashMap.get(format);
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern(format);
      if (isLenient) {
        dateTimeFormatter.withResolverStyle(ResolverStyle.LENIENT);
      }
      dateTimeFormatterHashMap.put(format, dateTimeFormatter);
    }
    return dateTimeFormatter;
  }

  public static boolean isOverlapping(
      LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
    return (((null == end2) || !start1.isAfter(end2)) && ((null == end1) || !start2.isAfter(end1)));
  }

  public static boolean isOverlapping(
      LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
    return (((null == end2) || !start1.isAfter(end2)) && ((null == end1) || !start2.isAfter(end1)));
  }

  public static boolean isOverlappingSlashPeriod(
      String start1, String end1, String start2, String end2) {
    return (((null == end2) || start1.compareTo(end2) <= 0)
        && ((null == end1) || start2.compareTo(end1) <= 0));
  }

  public static boolean isOverlapping(String start1, String end1, String start2, String end2) {
    LocalDate start1D =
        LocalDate.parse(start1.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE);
    LocalDate end1D =
        StringUtils.isNotBlank(end1)
            ? LocalDate.parse(
                end1.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
            : null;
    LocalDate start2D =
        LocalDate.parse(start2.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE);
    LocalDate end2D =
        StringUtils.isNotBlank(end2)
            ? LocalDate.parse(
                end2.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
            : null;
    return isOverlapping(start1D, end1D, start2D, end2D);
  }

  public static boolean betweenString(String dateTest, String dateDebut, String dateFin) {
    LocalDate dateTestD =
        dateTest != null
            ? LocalDate.parse(
                dateTest.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
            : null;
    LocalDate dateDebutD =
        dateDebut != null
            ? LocalDate.parse(
                dateDebut.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
            : null;
    LocalDate dateFinD =
        dateFin != null
            ? LocalDate.parse(
                dateFin.replace("/", "").replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE)
            : null;

    return betweenLocalDate(dateTestD, dateDebutD, dateFinD);
  }

  public static boolean betweenLocalDate(
      LocalDate dateTest, LocalDate dateDebut, LocalDate dateFin) {
    return dateDebut != null
        && !dateDebut.isAfter(dateTest)
        && (dateFin == null || !dateFin.isBefore(dateTest));
  }

  public static String getMinDate(String date1, String date2) {
    return getMinDate(date1, date2, SLASHED_FORMATTER);
  }

  public static String getMaxDate(String date1, String date2, DateTimeFormatter dateTimeFormatter) {
    if (date1 == null) {
      return date2;
    }
    if (date2 == null) {
      return date1;
    }

    LocalDate debutDate = LocalDate.parse(date1, dateTimeFormatter);
    LocalDate finDate = LocalDate.parse(date2, dateTimeFormatter);
    if (debutDate.isBefore(finDate)) {
      return date2;
    }
    return date1;
  }

  public static String getMinDate(String date1, String date2, DateTimeFormatter dateTimeFormatter) {
    if (date1 == null) {
      return date2;
    }
    if (date2 == null) {
      return date1;
    }

    LocalDate debutDate = LocalDate.parse(date1, dateTimeFormatter);
    LocalDate finDate = LocalDate.parse(date2, dateTimeFormatter);
    if (!debutDate.isBefore(finDate)) {
      return date2;
    }
    return date1;
  }

  public static String getMaxDate(String date1, String date2) {
    return getMaxDate(date1, date2, SLASHED_FORMATTER);
  }

  public static boolean isPeriodeValide(
      String dateDebutStr, String dateFinStr, DateTimeFormatter formatter) {
    LocalDate dateDebut = dateDebutStr != null ? LocalDate.parse(dateDebutStr, formatter) : null;
    LocalDate dateFin = dateFinStr != null ? LocalDate.parse(dateFinStr, formatter) : null;
    return DateUtils.isPeriodeValide(dateDebut, dateFin);
  }

  public static boolean isPeriodeValide(LocalDateTime dateDebut, LocalDateTime dateFin) {
    if (dateDebut != null && dateFin != null) {
      return dateDebut.isBefore(dateFin);
    }
    return true;
  }

  public static boolean isPeriodeValide(LocalDate dateDebut, LocalDate dateFin) {
    if (dateDebut != null && dateFin != null) {
      return dateDebut.isBefore(dateFin);
    }
    return true;
  }

  public static boolean isReverseDateStr(String debut, String fin) {
    if (StringUtils.isEmpty(debut) || StringUtils.isEmpty(fin)) {
      return false;
    }
    return debut.compareTo(fin) > 0;
  }

  public static boolean isReverseDate(String debut, String fin) {
    if (StringUtils.isEmpty(debut) || StringUtils.isEmpty(fin)) {
      return false;
    }
    LocalDate debutDate =
        LocalDate.parse(debut.replace("/", "").replace("-", ""), FORMATTER_YYYYMMDD);
    LocalDate finDate = LocalDate.parse(fin.replace("/", "").replace("-", ""), FORMATTER_YYYYMMDD);
    return debutDate.isAfter(finDate);
  }

  public static String getStringDatePlusDays(
      String dateFermeture, int days, DateTimeFormatter dateTimeFormatter) {
    return LocalDate.parse(dateFermeture, dateTimeFormatter)
        .plusDays(days)
        .format(dateTimeFormatter);
  }

  public static boolean beforeAnyFormat(String d1, String d2) {
    if (d1 != null) {
      d1 = d1.replace("/", "").replace("-", "");
    }
    if (d2 != null) {
      d2 = d2.replace("/", "").replace("-", "");
    }
    return d1 != null
        && (StringUtils.isBlank(d2)
            || LocalDate.parse(d1, DateTimeFormatter.BASIC_ISO_DATE)
                .isBefore(LocalDate.parse(d2, DateTimeFormatter.BASIC_ISO_DATE)));
  }

  public static boolean beforeAnyEnglishFormat(String d1, String d2) {
    if (d1 != null) {
      d1 = d1.replace("/", "").replace("-", "");
    }
    if (d2 != null) {
      d2 = d2.replace("/", "").replace("-", "");
    }
    return d1 != null && (StringUtils.isBlank(d2) || d1.compareTo(d2) < 0);
  }

  public static LocalDate parseAnyFormat(String date) {
    if (date == null) {
      return null;
    }
    date = date.replace("/", "").replace("-", "");
    return LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
  }

  public static boolean before(String d1, String d2) {
    return d2 == null || (d1 != null && LocalDate.parse(d1).isBefore(LocalDate.parse(d2)));
  }

  public static boolean after(String d1, String d2, DateTimeFormatter dateTimeFormatter) {
    return d1 == null
        || (d2 != null
            && LocalDate.parse(d1, dateTimeFormatter)
                .isAfter(LocalDate.parse(d2, dateTimeFormatter)));
  }

  public static boolean before(String d1, String d2, DateTimeFormatter dateTimeFormatter) {
    return d2 == null
        || (d1 != null
            && LocalDate.parse(d1, dateTimeFormatter)
                .isBefore(LocalDate.parse(d2, dateTimeFormatter)));
  }

  public static LocalDate parse(String toParse, DateTimeFormatter dateTimeFormatter) {
    if (StringUtils.isNotBlank(toParse)) {
      return LocalDate.parse(toParse, dateTimeFormatter);
    }
    return null;
  }

  public static LocalTime parseTime(String toParse, DateTimeFormatter dateTimeFormatter) {
    if (StringUtils.isNotBlank(toParse)) {
      return LocalTime.parse(toParse, dateTimeFormatter);
    }
    return null;
  }

  public static int compareDate(String dateString1, String dateString2) {
    return compareDate(dateString1, dateString2, FORMATTER);
  }

  public static int compareDate(
      String dateString1, String dateString2, DateTimeFormatter dateTimeFormatter) {
    if (dateString1 == null) {
      if (dateString2 == null) {
        return 0;
      } else {
        return 1;
      }
    } else if (dateString2 == null) {
      return -1;
    }
    LocalDate date1 = LocalDate.parse(dateString1, dateTimeFormatter);
    LocalDate date2 = LocalDate.parse(dateString2, dateTimeFormatter);
    return date1.compareTo(date2);
  }

  public static LocalDate stringToDate(String dateString) {
    if (StringUtils.isEmpty(dateString)) {
      return null;
    }

    return LocalDate.parse(dateString.replace("-", "").replace("/", ""), FORMATTER_YYYYMMDD);
  }

  public static String formatDate(String date) {
    if (StringUtils.isEmpty(date)) {
      return null;
    }

    return FORMATTER.format(Objects.requireNonNull(stringToDate(date)));
  }

  public static String formatDate(LocalDate date) {
    if (date == null) {
      return null;
    }

    return FORMATTER.format(date);
  }

  public static String formatDateTime(LocalDateTime date) {
    return formatDateTime(date, FORMATTER);
  }

  public static String formatDateTime(LocalDateTime date, DateTimeFormatter dateTimeFormatter) {
    if (date == null) {
      return null;
    }

    return dateTimeFormatter.format(date);
  }

  public static String formatDate(LocalDate date, DateTimeFormatter formatter) {
    if (date == null) {
      return null;
    }

    return formatter.format(date);
  }

  public static String formatTime(LocalDateTime date) {
    if (date == null) {
      return null;
    }

    return HH_MM_SS_FORMATTER.format(date.toLocalTime());
  }

  /**
   * Renvoie une date au format <b>yyyy/MM/dd 00:00:00</b> obtenue a partir de la date passee en
   * parametre.
   *
   * @param date la date dont on veut changer le format.
   * @return la date au format <b>yyyy/MM/dd 00:00:00</b>.
   */
  public static Date formatDateWithHourMinuteSecond(final Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
    try {
      return sdf.parse(sdf.format(date));
    } catch (ParseException e) {
      log.error("Impossible de parser la date : {}", date);
      throw new ExceptionService(e);
    }
  }

  /**
   * Transforme une {@link Date} en chaine de string au format yyyy/MM/dd. Return null si parametre
   * null
   */
  public static String formatDate(@Nullable Date date) {
    return formatDate(date, FORMATTERSLASHED);
  }

  /**
   * Transforme une {@link Date} en chaine de string au format passé en paramètre. Return null si
   * parametre null
   */
  public static String formatDate(@Nullable Date date, String format) {
    if (date == null) return null;
    return new SimpleDateFormat(format).format(date);
  }

  /** Transforme un {@link Date} en {@link LocalDateTime}, return null si parametre null */
  public static LocalDateTime dateToLocalDateTime(@Nullable Date date) {
    if (date == null) return null;
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public static Date parseDate(String s, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    try {
      return simpleDateFormat.parse(s);
    } catch (ParseException e) {
      log.error("Impossible de parser {} en format ", format);
      throw new ExceptionService(e);
    }
  }

  public static Date parseDate(String s, SimpleDateFormat simpleDateFormat) {
    try {
      return simpleDateFormat.parse(s);
    } catch (ParseException e) {
      throw new ExceptionService(e);
    }
  }

  public static LocalDate parseLocalDate(String stringDate, String format) {
    if (StringUtils.isEmpty(stringDate)) {
      return null;
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      return LocalDate.parse(stringDate, formatter);
    } catch (Exception e) {
      log.error("Impossible de parser {} en format {}", stringDate, format);
      throw new ExceptionService(e);
    }
  }

  public static LocalDateTime parseLocalDateTime(String stringDate, String format) {
    if (StringUtils.isEmpty(stringDate)) {
      return null;
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      return LocalDate.parse(stringDate, formatter).atStartOfDay();
    } catch (Exception e) {
      log.error("Impossible de parser {} en format {}", stringDate, format);
      throw new ExceptionService(e);
    }
  }

  public static XMLGregorianCalendar stringToXMLGregorianCalendar(String s, String format) {

    XMLGregorianCalendar result;
    Date date;
    GregorianCalendar gregorianCalendar;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    try {
      date = simpleDateFormat.parse(s);
      gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
      gregorianCalendar.setTime(date);
      result = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
      return result;
    } catch (ParseException e) {
      log.error("Impossible de parser stringToXMLGregorianCalendar.");
      throw new ExceptionService(e);
    }
  }

  public static String convertXmlGregorianToString(XMLGregorianCalendar xc, String format) {
    DateFormat df = new SimpleDateFormat(format);
    GregorianCalendar gCalendar = xc.toGregorianCalendar();

    Date date = gCalendar.getTime();
    return df.format(date);
  }

  public static void resetTimeInDate(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
  }

  public static Date removeTimeInDate(Date date) {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    resetTimeInDate(calendar);
    return calendar.getTime();
  }

  public static LocalDate getMaxDate(LocalDate date1, LocalDate date2) {
    if (date1 != null) {
      if (date2 != null) {
        return date1.isBefore(date2) ? date2 : date1;
      }
      return date1;
    }
    return date2;
  }

  public static LocalDateTime getMaxDate(LocalDateTime date1, LocalDateTime date2) {
    if (date1 != null) {
      if (date2 != null) {
        return date1.isBefore(date2) ? date2 : date1;
      }
      return date1;
    }
    return date2;
  }

  public static LocalDate getMaxDate(LocalDate date1, LocalDate date2, LocalDate date3) {
    return getMaxDate(getMaxDate(date1, date2), date3);
  }

  public static String getMaxDate(List<String> dates) {
    return dates.stream().filter(Objects::nonNull).max(String::compareTo).orElse(null);
  }

  public static String getMinDate(List<String> dates) {
    return dates.stream().filter(Objects::nonNull).min(String::compareTo).orElse(null);
  }

  public static String getMinDate(String... dates) {
    return getMinDate(Arrays.asList(dates));
  }

  public static String getMaxDateOrNull(String... dates) {
    return getMaxDateOrNull(Arrays.asList(dates));
  }

  public static String getMaxDateOrNull(List<String> dates) {
    return dates.contains(null) ? null : dates.stream().max(String::compareTo).orElse(null);
  }

  public static String getNullOrMaxDate(String... dates) {
    return getMaxDateOrNull(Arrays.asList(dates));
  }

  public static LocalDate getMinDate(LocalDate date1, LocalDate date2) {
    if (date1 != null) {
      if (date2 != null) {
        return date1.isAfter(date2) ? date2 : date1;
      }
      return date1;
    }
    return date2;
  }

  public static LocalDate getMinDate(LocalDate date1, LocalDate date2, LocalDate date3) {
    return getMinDate(getMinDate(date1, date2), date3);
  }

  public static boolean checkDateValidity(String date) {
    return !StringUtils.isNotEmpty(date)
        || (isValidDate(date, YYYY_MM_DD) && date.matches("\\d{4}-\\d{2}-\\d{2}"));
  }

  public static boolean isValidDate(String date, String format) {
    if (date.length() != format.length()) {
      return false;
    } else {
      try {
        LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        return true;
      } catch (DateTimeParseException var5) {
        log.error("isValidDate - Impossible de parser {} en format ", format);
        return false;
      }
    }
  }

  /**
   * Validation d'une date via une String selon un format donné
   *
   * @param date Date sous forme de String à valider
   * @param format Le format de date à respecter
   * @param isLenient Permet la gestion de date lunaire (true pour uniquement des dates grégorienne)
   * @return booléen indiquant la validité de la date
   */
  public static boolean isValidDate(String date, String format, boolean isLenient) {
    DateTimeFormatter sdf = getDateTimeFormatter(format, isLenient);
    if (date.length() != format.length()) {
      return false;
    }
    try {
      sdf.parse(date);
    } catch (DateTimeParseException e) {
      log.error(
          "DateTimeParseException - isValidDate date {}, au format {}, lenient {}",
          date,
          format,
          isLenient);
      return false;
    } catch (Exception e) {
      log.error(
          "Exception - isValidDate date {}, au format {}, lenient {}", date, format, isLenient);
      return false;
    }
    return true;
  }

  public static String dateMinusOneDay(String date) {
    if (date == null) {
      return null;
    }
    return formatDate(LocalDate.parse(date).minusDays(1));
  }

  public static String dateMinusOneDay(String date, DateTimeFormatter formatter) {
    if (date == null) {
      return null;
    }
    return formatDate(LocalDate.parse(date, formatter).minusDays(1), formatter);
  }

  public static String datePlusOneDay(String date, DateTimeFormatter formatter) {
    if (date == null) {
      return null;
    }
    return formatDate(LocalDate.parse(date, formatter).plusDays(1), formatter);
  }

  public static int getYearFromDate(LocalDate date) {
    if (date == null) {
      return 0;
    }

    return date.getYear();
  }

  public static boolean fromDate(final LocalDate dateTest, final LocalDate dateFin) {
    return dateFin == null || !dateFin.isBefore(dateTest);
  }

  public static <T> List<T> aggregateOverlappingPeriods(
      Collection<T> objects, Function<T, Periode> toPeriode) {
    List<T> toAggregate = new ArrayList<>(objects);
    toAggregate.sort(
        (o1, o2) ->
            compareDatesAnyFormat(toPeriode.apply(o1).getDebut(), toPeriode.apply(o2).getDebut()));

    LinkedList<T> merged = new LinkedList<>();
    for (T object : toAggregate) {
      if (merged.isEmpty()
          || beforeAnyFormat(
              toPeriode.apply(merged.getLast()).getFin(), toPeriode.apply(object).getDebut())) {
        merged.add(object);
      } else {
        String maxFin =
            getMaxDateOrNull(
                Arrays.asList(
                    toPeriode.apply(object).getFin(), toPeriode.apply(merged.getLast()).getFin()));
        if (maxFin != null) {
          toPeriode.apply(merged.getLast()).setFin(maxFin);
        } else {
          toPeriode.apply(merged.getLast()).setFinToNull();
        }
      }
    }

    return merged;
  }

  public static int compareDatesAnyFormat(String d1, String d2) {
    LocalDate date1 = stringToDate(d1);
    LocalDate date2 = stringToDate(d2);

    if (date1 == null) {
      return date2 == null ? 0 : 1;
    }

    if (date2 == null) {
      return -1;
    }
    return date1.compareTo(date2);
  }

  public static boolean isPeriodeNonPresente(PeriodeDroit periode, List<PeriodeDroit> periodes) {
    for (PeriodeDroit per : periodes) {
      if ((periode.getPeriodeDebut().compareTo(per.getPeriodeDebut()) >= 0
              && periode.getPeriodeDebut().compareTo(per.getPeriodeFin()) <= 0)
          || (periode.getPeriodeFin().compareTo(per.getPeriodeDebut()) >= 0
              && periode.getPeriodeFin().compareTo(per.getPeriodeFin()) <= 0)) {
        return false;
      }
    }
    return true;
  }

  public static boolean isLeapYear(int year) {
    return (((year % 4) == 0) && ((year % 100 != 0) || (year % 400 == 0)));
  }

  public static String parseDateAndNumberOfDays(String dateFin, int numberOfDays) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(FORMATTERSLASHED);
      Date dateDebutNew = sdf.parse(dateFin);
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateDebutNew);
      cal.add(Calendar.DATE, numberOfDays);
      return sdf.format(cal.getTime());
    } catch (ParseException e) {
      log.error("Erreur lors du parsing de la date de fin", e);
    }
    return null;
  }

  public static SimpleDateFormat getReaderConverterFormat() {
    return new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_SSSXXX);
  }

  public static Date nowUTC() {
    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);
    return Date.from(currentDate.toInstant(ZoneOffset.UTC));
  }

  public static String getDateFin(DomaineDroit domaineDroit, boolean isInsurer) {
    String finOffline = domaineDroit.getPeriodeDroit().getPeriodeFin();
    if (isInsurer && domaineDroit.getPeriodeOnline() != null) {
      String finOnline = domaineDroit.getPeriodeOnline().getPeriodeFin();
      if (StringUtils.isNotBlank(finOnline) && finOnline.compareTo(finOffline) < 0) {
        return finOnline;
      }
    }
    return finOffline;
  }

  /** Fusionne des objets ayant des periodes qui se superposent ou qui se suivent */
  public static <T> List<T> getObjPeriodsMerged(
      List<T> objs, Function<T, Periode> toPeriod, DateTimeFormatter formatter) {
    objs = DateUtils.aggregateOverlappingPeriods(ListUtils.emptyIfNull(objs), toPeriod);
    if (CollectionUtils.isNotEmpty(objs)) {
      Iterator<T> iterator = objs.iterator();
      T current = iterator.next();

      while (iterator.hasNext()) {
        T next = iterator.next();

        if (toPeriod.apply(current).getFin() != null) {
          String currentEndPeriodPlusOne =
              DateUtils.datePlusOneDay(toPeriod.apply(current).getFin(), formatter);
          // Vérifie si la fin de la période actuelle est le jour suivant du début de la
          // période suivante
          if (currentEndPeriodPlusOne != null
              && currentEndPeriodPlusOne.equals(toPeriod.apply(next).getDebut())) {
            if (toPeriod.apply(next).getFin() != null) {
              toPeriod.apply(current).setFin(toPeriod.apply(next).getFin());
            } else {
              toPeriod.apply(current).setFinToNull();
            }
            iterator.remove();
          } else {
            current = next;
          }
        }
      }
    }

    return objs;
  }

  public static List<Periode> getPeriodesFusionnees(
      List<Periode> periodes, DateTimeFormatter formatter) {
    return getObjPeriodsMerged(periodes, Function.identity(), formatter);
  }

  public static String getMinDateOrNull(List<String> dates) {
    return dates.stream().filter(Objects::nonNull).min(String::compareTo).orElse(null);
  }

  public static String getMinDateOrNull(String... dates) {
    return getMinDateOrNull(Arrays.asList(dates));
  }

  public static List<Periode> getPeriodesFusionnees(List<Periode> periodes) {
    return getObjPeriodsMerged(periodes, Function.identity(), FORMATTER);
  }

  public static List<Periode> getSplittedPeriods(
      Periode fullPeriode, List<Periode> periodesToRemove) {
    Periode workPeriode = new Periode(fullPeriode);
    List<Periode> periodesRestantes = new ArrayList<>();
    for (Periode periode : periodesToRemove) {
      if (DateUtils.before(workPeriode.getDebut(), periode.getDebut(), SLASHED_FORMATTER)) {
        Periode toAdd =
            new Periode(
                workPeriode.getDebut(),
                DateUtils.dateMinusOneDay(periode.getDebut(), SLASHED_FORMATTER));
        periodesRestantes.add(toAdd);
        workPeriode.setDebut(DateUtils.datePlusOneDay(periode.getFin(), SLASHED_FORMATTER));
      } else if (DateUtils.before(workPeriode.getFin(), periode.getFin(), SLASHED_FORMATTER)) {
        break;
      } else {
        workPeriode.setDebut(DateUtils.datePlusOneDay(periode.getFin(), SLASHED_FORMATTER));
      }
    }
    if (!DateUtils.after(workPeriode.getDebut(), workPeriode.getFin(), SLASHED_FORMATTER)) {
      Periode toAdd = new Periode(workPeriode.getDebut(), workPeriode.getFin());
      periodesRestantes.add(toAdd);
    }

    return periodesRestantes;
  }

  public static String getNow() {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
    return currentDate.format(formatter);
  }
}
