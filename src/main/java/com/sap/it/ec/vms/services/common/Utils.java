package com.sap.it.ec.vms.services.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * @author I311690
 * @since 05/June/2021
 */
public class Utils {

  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private Utils() {
    // private constructor
  }

  public static <T> String convertToJson(T string) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    // Convert object to JSON string
    return mapper.writeValueAsString(string);
  }

  public static String generateUniqueId(String custId) {
    String timeStamp = new SimpleDateFormat(Constants.SDF_DATETIME).format(new Date());
    String uuid = timeStamp + "-" + custId + "-" + UUID.randomUUID().toString().substring(0, 5);
    LOGGER.info("Unique id: " + uuid);
    return uuid;
  }

  public static Date convertTimeToDateTime(long time) {
    return (new Date(time));
  }

  public static Date convertStringToDate(String strDate) throws ParseException {
    // Modern approach is DateTimeFormatter f = DateTimeFormatter.ofPattern(
    // "MMdduuHHmmss" ) ;
    try {
      SimpleDateFormat dateformat = new SimpleDateFormat(Constants.SDF_DATE);
      if (strDate != null && !strDate.isEmpty()) return dateformat.parse(strDate);
    } catch (ParseException e) {
      return null;
    }
    return null;
  }

  public static <T> String convertObjectToJson(T object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    return mapper.writeValueAsString(object);
  }

  public static <T> String convertObjectToJsonIncNull(T object) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }

  public static void copyNonNullProperties(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (java.beans.PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) emptyNames.add(pd.getName());
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static String formatDate(String strDate) {
    if (isNotNullEmpty(strDate)) {
      SimpleDateFormat dateformat = new SimpleDateFormat(Constants.SDF_DATE_WO_HYPHEN);
      SimpleDateFormat requiredDateformat = new SimpleDateFormat(Constants.SDF_DATE);
      Date date = null;
      try {
        date = dateformat.parse(strDate);
      } catch (ParseException e) {
        return null;
      }
      return requiredDateformat.format(date);
    }
    return null;
  }

  public static String formatDateFromEpoch(String strDate) {
    if (isNotNullEmpty(strDate)) {
      String requiredString = strDate.substring(strDate.indexOf('(') + 1, strDate.indexOf(')'));
      LocalDateTime dateTime =
          LocalDateTime.ofEpochSecond(Long.valueOf(requiredString) / 1000, 0, ZoneOffset.UTC);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.SDF_DATE, Locale.ENGLISH);
      return dateTime.format(formatter);
    }
    return null;
  }

  public static String formatDateTimestampFromEpoch(String strDate) {
    if (isNotNullEmpty(strDate)) {
      String requiredString = strDate.substring(strDate.indexOf('(') + 1, strDate.indexOf(')'));
      LocalDateTime dateTime =
          LocalDateTime.ofEpochSecond(Long.valueOf(requiredString) / 1000, 0, ZoneOffset.UTC);
      DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern(Constants.SDF_DATETIME_FORMAT, Locale.ENGLISH);
      return dateTime.format(formatter);
    }
    return null;
  }

  public static String getSystemTimestamp() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    return Long.toString(timestamp.getTime());
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static String formatDateForCRM(String strDate) {
    if (isNotNullEmpty(strDate)) {
      SimpleDateFormat pattern = new SimpleDateFormat(Constants.SDF_DATE);
      pattern.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date date;
      try {
        date = pattern.parse(strDate);
      } catch (ParseException e) {
        return null;
      }
      long epochDate = date.getTime();
      return "/Date(" + epochDate + ")/";
    }
    return null;
  }

  public static final boolean isNotNullEmpty(final String str) {
    return str != null && !str.isEmpty();
  }

  public static double convertStringToNumber(String value) {

    if (null != value) return new Double(value);
    return 0;
  }

  public static String getObjectFromJson(String fileName) throws IOException {
    Resource resource = new ClassPathResource(fileName);
    try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    }
  }

  public static Instant getUTCTime() {
    return Instant.now();
  }

  public static Object getObjectFromJson(String fileName, Class<?> c) throws IOException {
    Resource resource = new ClassPathResource(fileName);
    ObjectMapper mapper = null;
    try (InputStream is = resource.getInputStream(); ) {
      mapper = new ObjectMapper();
      return mapper.readValue(is, c);
    } catch (FileNotFoundException fnfe) {
      LOGGER.info("Utils : getObjectFromJson --> FileNotFoundException");
      throw fnfe;
    } catch (IOException ioe) {
      LOGGER.info("Utils : getObjectFromJson --> IOException");
      throw ioe;
    }
  }

  public Timestamp addDaysToCurrentDate(int days) throws Exception {

    Instant currentInstant = Instant.now();
    Timestamp newStamp = Timestamp.from(currentInstant.plus(days, ChronoUnit.DAYS));
    return newStamp;
  }

  public Timestamp addDaysToGivenTimeStamp(int days, Timestamp givenTimeStamp) throws Exception {

    Timestamp newStamp = Timestamp.from(givenTimeStamp.toInstant().plus(days, ChronoUnit.DAYS));
    // or
    /*
     * if(days < 0){ throw new Exception("Days in wrong format."); } Long
     * miliseconds = dayToMiliseconds(days); return new
     * Timestamp(t1.getTime() + miliseconds);
     */
    return newStamp;
  }

  public static Instant convertStringObjectToInstant(String strDate, String ms)
      throws ParseException {
    if (isNotNullEmpty(strDate)) {
      strDate = strDate.replace(' ', 'T').concat(ms);
      return Instant.parse(strDate);
    }
    return null;
  }

  public static String getUTCDate() {
    SimpleDateFormat pattern = new SimpleDateFormat(Constants.SDF_DATE);
    pattern.setTimeZone(TimeZone.getTimeZone("UTC"));
    return pattern.format(new Date());
  }

  public static Long getExecutionTime(Instant startTime, Instant endTime) {
    if (null != startTime && null != endTime)
      // return Duration.between(startTime, endTime);
      return ChronoUnit.MILLIS.between(startTime, endTime);
    else return 0L;
  }
}
