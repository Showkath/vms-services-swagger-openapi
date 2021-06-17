package com.sap.it.ec.vms.services.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** @author I311690 */
public class Constants {

  private Constants() {
    // private constructor
  }

  public static final String PROFILE_LOCAL = "local";
  public static final String PROFILE_NON_LOCAL = "!local";

  // Constants for Destinations
  public static final String DESTINATION_CRM = "int_ic";
  public static final String DESTINATION_CMS = "int_cms";
  public static final String DESTINATION_SSF = "int_ssf";
  public static final String DESTINATION_CALIDUS = "ext_cpq";

  // Constants for Logs
  public static final String VBE_LOGS_CUSTOMFIELD = "VBE_LOGS_CUSTOMFIELD";

  // Constants for backend connectivity
  public static final String MEDIATYPE_TEXT_XML = "text/xml; charset=utf-8";
  public static final String MEDIATYPE_JSON = "application/json; charset=utf-8";
  public static final String MEDIATYPE_FORM_URL_ENC =
      "application/x-www-form-urlencoded; charset=utf-8";
  public static final String MEDIATYPE_APP_PDF = "application/pdf";

  public static final String TYPE_TEXT_XML = "text/xml";
  public static final String TYPE_JSON = "application/json";
  public static final String TYPE_FORM = "application/x-www-form-urlencoded";
  public static final String TYPE_PDF = "application/pdf";
  public static final String PARAM = "Param";

  public static final String SET_COOKIE = "set-cookie";
  public static final String COOKIE = "Cookie";
  public static final String CONTENT_TYPE = "ContentType";
  public static final String X_CSRF_TOKEN = "X-CSRF-Token";
  public static final String FETCH = "Fetch";
  public static final String ACCEPT = "Accept";

  // Constants for path resources
  public static final String PATH_CREATE_OPPORTUNITY = "Opportunities";
  public static final String PATH_CREATE_SSFTICKET = "Generic_Api_HeaderSet";
  public static final String PATH1_GET_OPPORTUNITY = "Opportunities('";
  public static final String PATH2_GET_OPPORTUNITY =
      "')?$format=json&$expand=PartiesInvolved,Pricing,Attributes";

  public static final String SDF_DATETIME = "ddMMyyyyHHmmssSSS";
  public static final String SDF_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String SDF_DATE = "yyyy-MM-dd";
  public static final String SDF_DATE_WO_HYPHEN = "yyyyMMdd";
  public static final String ACTION_INSERT = "Insert";
  public static final String APPEND_START_MS = ".000Z";
  public static final String APPEND_END_MS = ".999Z";

  public enum CategoryId {
    VMS_CAT1,
    VMS_CAT2,
    VMS_CAT3
  }

  public static List<String> EXCL_PROD_TYPES =
      new ArrayList<>(Arrays.asList("CLOUD SETUP SERVICE", "SERVICE CATALOG"));
}
