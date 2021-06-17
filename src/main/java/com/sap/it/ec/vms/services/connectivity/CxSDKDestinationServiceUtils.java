package com.sap.it.ec.vms.services.connectivity;

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
/**
 * SAP Cloud SDK to get HTTP Client
 *
 * @author I311690 (showkath.naseem@sap.com)
 * @author ()
 * @since 01/06/2021
 */
import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultHttpClientFactory;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientFactory;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.it.ec.vms.services.config.VmsProperties;
import java.net.URI;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CxSDKDestinationServiceUtils {

  @Autowired VmsProperties props;

  private static final Logger LOGGER = LoggerFactory.getLogger(CxSDKDestinationServiceUtils.class);

  public URI getUri(String destName) {
    URI uri = getHttpDestination(destName).getUri();
    LOGGER.info("VMS CxSDKDestinationServiceUtils --> getUri : Destination service Uri: {}", uri);
    return uri;
  }

  public HttpClient getHttpClient(String destName) {
    // Overriding the default timeout value
    HttpClientFactory customFactory =
        DefaultHttpClientFactory.builder().timeoutMilliseconds(180 * 1000).build();
    HttpClientAccessor.setHttpClientFactory(customFactory);
    HttpClient client = HttpClientAccessor.getHttpClient(getHttpDestination(destName));
    LOGGER.info("VMS CxSDKDestinationServiceUtils --> getHttpClient : httpclient returned");
    return client;
  }

  public HttpDestination getHttpDestination(String destName) {
    Destination destination = null;
    LOGGER.info("VMS CxSDKDestinationServiceUtils --> getHttpDestination : {}", destName);
    destination = DestinationAccessor.getDestination(destName);

    return destination.asHttp();
  }
}
