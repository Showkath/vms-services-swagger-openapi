package com.sap.it.ec.vms.services.connectivity;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

import com.sap.cloud.sdk.cloudplatform.resilience.ResilienceConfiguration;
import com.sap.cloud.sdk.cloudplatform.resilience.ResilienceDecorator;
import com.sap.it.ec.vms.services.common.Constants;
import com.sap.it.ec.vms.services.common.Utils;
import com.sap.it.ec.vms.services.config.VmsProperties;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectivityService {

  @Autowired VmsProperties props;

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectivityService.class);

  @Autowired CxSDKDestinationServiceUtils cxSDKDestinationServiceUtils;

  public HttpResponseClient getResponseBackend(
      String path, String destinationName, String payload, String mediaType) {

    HttpResponseClient responseToClient;
    responseToClient = this.callBackend(path.trim(), destinationName, payload, mediaType);
    LOGGER.info("ConnectivityService --> getResponseBackend");
    return responseToClient;
  }

  protected HttpResponseClient callBackend(
      String path, String destName, String payload, String mediaType) {

    LOGGER.info("CxSDKDestinationServiceUtils --> callBackend : {}", destName);
    Instant startTime = null;
    Instant endTime = null;
    try {
      URI baseUri = getBaseURI(destName);
      HttpClient httpClient = getHttpCLient(destName);
      LOGGER.info("VMS CxSDKDestinationServiceUtils --> callBackend : baseUri {}", baseUri);
      String serviceUrl = baseUri.toString() + path;
      LOGGER.info("VMS CxSDKDestinationServiceUtils --> callBackend : serviceUrl {}", serviceUrl);

      HttpUriRequest request = new HttpGet(serviceUrl.replace(" ", "%20"));
      // Check if it is a post call
      if (payload != null) {
        request =
            createRequestForPost(payload, mediaType, request, httpClient, serviceUrl, destName);
      }
      if (mediaType.equalsIgnoreCase(Constants.TYPE_TEXT_XML))
        request.addHeader(Constants.ACCEPT, Constants.TYPE_TEXT_XML);
      else request.addHeader(Constants.ACCEPT, Constants.TYPE_JSON);

      LOGGER.info("CxSDKDestinationServiceUtils --> callBackend : before execute");
      startTime = Instant.now();
      final HttpResponse response = executeBackend(httpClient, request);
      endTime = Instant.now();
      Long execTime = Utils.getExecutionTime(startTime, endTime);
      LOGGER.info("CxSDKDestinationServiceUtils --> callBackend : after execute");

      int backendResponseCode = response.getStatusLine().getStatusCode();

      String backendResponse = null;
      if (backendResponseCode < HTTP_BAD_REQUEST) {
        HttpEntity entity = response.getEntity();
        backendResponse = EntityUtils.toString(entity);
        LOGGER.info(
            "CxSDKDestinationServiceUtils --> callBackend : backendResponse:" + backendResponse);
      } else if (response.getEntity() != null) {
        LOGGER.info(
            "CxSDKDestinationServiceUtils --> callBackend : Error response:" + backendResponse);
        backendResponse = EntityUtils.toString(response.getEntity());
      }

      if (backendResponse == null) {
        LOGGER.info("CxSDKDestinationServiceUtils --> callBackend : BackendResponse is null");
        backendResponse = "Empty input stream from backend!";
      }
      LOGGER.info(
          "CxSDKDestinationServiceUtils --> callBackend : headers"
              + mapHeaders(response.getAllHeaders()));
      return new HttpResponseClient(
          backendResponseCode, backendResponse, mapHeaders(response.getAllHeaders()), execTime);

    } catch (Exception e) {
      LOGGER.error("CxSDKDestinationServiceUtils --> callBackend : Exception");
      endTime = Instant.now();
      Long execTime = Utils.getExecutionTime(startTime, endTime);
      return new HttpResponseClient(500, e.getMessage(), null, execTime);
    }
  }

  public Map<String, List<String>> mapHeaders(Header[] headers) {
    Map<String, List<String>> headerMap = new HashMap<>();
    // TODO : I311690 TBV check backend  response headers
    for (Header header : headers) {
      List<String> myList = new ArrayList<>();
      myList.add(header.getValue());
      headerMap.put(header.getName(), myList);
    }

    return headerMap;
  }

  public HttpPost createRequestForPost(
      String postString,
      String mediaType,
      HttpUriRequest request,
      HttpClient httpClient,
      String uri,
      String destName)
      throws Exception {
    HttpPost postRequest = new HttpPost(uri);

    if (!destName.equalsIgnoreCase(Constants.DESTINATION_CALIDUS)) {
      // fetch CSRF token for POST call except for callidus destination
      HttpUriRequest getRequest = request;
      getRequest.setHeader(Constants.X_CSRF_TOKEN, Constants.FETCH);
      HttpResponse response = executeBackend(httpClient, request);

      Map<String, List<String>> retrievedHeaders = mapHeaders(response.getAllHeaders());

      for (Map.Entry<String, List<String>> entry : retrievedHeaders.entrySet()) {
        String key = entry.getKey();
        boolean keyEqualsSetCookie = Constants.SET_COOKIE.equalsIgnoreCase(key);
        if (Constants.X_CSRF_TOKEN.equalsIgnoreCase(key) || keyEqualsSetCookie) {
          for (String value : entry.getValue()) {
            String newKey = key;
            String newValue = value;
            if (keyEqualsSetCookie) {
              newKey = Constants.COOKIE;
              newValue = value.substring(0, value.indexOf(';'));
            }
            postRequest.setHeader(newKey, newValue);
          }
        }
      }
    }

    // setRequest(mediaType, postRequest , postString);
    LOGGER.info("CxSDKDestinationServiceUtils --> callPost : post request is build");
    return postRequest;
  }

  public URI getBaseURI(String destName) {
    return cxSDKDestinationServiceUtils.getUri(destName);
  }

  public HttpClient getHttpCLient(String destName) {
    return cxSDKDestinationServiceUtils.getHttpClient(destName);
  }

  public HttpResponse executeBackend(HttpClient httpClient, HttpUriRequest request)
      throws Exception {
    ResilienceConfiguration configuration =
        ResilienceConfiguration.empty("config-id-vbe")
            .timeLimiterConfiguration(
                ResilienceConfiguration.TimeLimiterConfiguration.of(Duration.ofMillis(180 * 1000)));
    return ResilienceDecorator.executeCallable(
        () -> {
          return httpClient.execute(request);
        },
        configuration);
  }
}
