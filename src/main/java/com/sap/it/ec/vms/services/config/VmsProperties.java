package com.sap.it.ec.vms.services.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "config")
public class VmsProperties {
  /*
  // @Value("${config.reload}")
  // boolean reload;

  // @Value("${config.delete}")
   //boolean delete;

   @Value("${config.timeoutInSeconds}")
   int timeoutInSeconds;

   @Value("${config.deleteFromObjectStore}")
   long deleteFromObjectStore;

   @Value("${config.retryCount}")
   int retryCount;

   @Value("${config.retryInterval}")
   int retryInterval;

   @Value("${config.waitTimeInMinutes}")
   int waitTimeInMinutes;

   @Value("${config.corePoolSize}")
   int corePoolSize;

   @Value("${config.maxPoolSize}")
   int maxPoolSize;
   */
}
