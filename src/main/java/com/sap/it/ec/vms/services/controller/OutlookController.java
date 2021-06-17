package com.sap.it.ec.vms.services.controller;

import com.sap.it.ec.vms.services.connectivity.ConnectivityService;
import com.sap.it.ec.vms.services.connectivity.HttpResponseClient;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@io.swagger.v3.oas.annotations.tags.Tag(name = "Outlook Services")
public class OutlookController {

  private static final Logger LOGGER = LoggerFactory.getLogger(OutlookController.class);

  @Autowired ConnectivityService connectivityService;

  @GetMapping("/api/v1/hello-outlook-vms")
  @ResponseBody
  public String sayHello() {
    return "Hello I311690";
  }

  @GetMapping("/api/v1/backend")
  @ResponseBody
  public HttpResponseClient getBackendData(
      @RequestParam("dest") String dest,
      @RequestParam("path") String path,
      @RequestParam("media") String media)
      throws JSONException, URISyntaxException, IOException {
    LOGGER.info("info OutlookController : oadata --> getOdata");
    try {
      HttpResponseClient resp = connectivityService.getResponseBackend(path, dest, null, media);
      return (resp);
    } catch (Exception e) {

      LOGGER.error("error OutlookController : getOdata --> exception");
    }
    return null;
  }
}
