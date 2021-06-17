package com.sap.it.ec.vms.services.connectivity;

import java.util.List;
import java.util.Map;

public class HttpResponseClient {
  private int responseCode;
  private String responseString;
  private Map<String, List<String>> header;
  private Long totalExecutionTime;

  public HttpResponseClient() {}

  public HttpResponseClient(
      int responseCode,
      String responseString,
      Map<String, List<String>> header,
      Long totalExecutionTime) {
    this.responseCode = responseCode;
    this.responseString = responseString;
    this.header = header;
    this.totalExecutionTime = totalExecutionTime;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public String getResponseString() {
    return responseString;
  }

  public Map<String, List<String>> getHeader() {
    return header;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public void setResponseString(String responseString) {
    this.responseString = responseString;
  }

  public void setHeader(Map<String, List<String>> header) {
    this.header = header;
  }

  public Long getTotalExecutionTime() {
    return totalExecutionTime;
  }

  public void setTotalExecutionTime(Long totalExecutionTime) {
    this.totalExecutionTime = totalExecutionTime;
  }
}
