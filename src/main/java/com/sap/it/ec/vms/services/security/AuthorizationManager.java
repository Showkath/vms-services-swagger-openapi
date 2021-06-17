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
@Component("auth")
public class AuthorizationManager {

  private static final Logger log = LoggerFactory.getLogger(AuthorizationManager.class);

  // used by SecurityConfig @auth.isDCIUser()
  public Boolean isDCIUser() {
    String userId = SpringSecurityContext.getToken().getLogonName().toUpperCase();
    boolean result = userId.startsWith("D") || userId.startsWith("C") || userId.startsWith("I");
    log.info("Checking if user " + userId + " is D/C/I user. Result: " + result);
    return result;
  }
}
