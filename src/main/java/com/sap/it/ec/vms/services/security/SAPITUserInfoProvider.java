package com.sap.it.ec.vms.services.security;

import com.sap.cloud.security.xsuaa.token.SpringSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
/*
 * @auth : I311690
 * @since: 01-06-2021
 */
@Component
public class SAPITUserInfoProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(SAPITUserInfoProvider.class);

  public SAPITUserInfoProvider() {}

  public static String getAppToken() {
    return SpringSecurityContext.getToken().getAppToken();
  }

  public static String getTenantId() {
    return SpringSecurityContext.getToken().getSubaccountId();
  }

  public static String getLogonName() {
    return SpringSecurityContext.getToken().getLogonName();
  }

  public static String getEmail() {
    return SpringSecurityContext.getToken().getEmail();
  }

  public static String getSubdomain() {
    return SpringSecurityContext.getToken().getSubdomain();
  }

  public static String getSubaccountId() {
    return SpringSecurityContext.getToken().getSubaccountId();
  }

  public static String getClientId() {
    return SpringSecurityContext.getToken().getClientId();
  }
}
