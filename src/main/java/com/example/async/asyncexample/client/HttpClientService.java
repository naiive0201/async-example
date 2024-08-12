package com.example.async.asyncexample.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class HttpClientService {
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  public HttpClientService() {
    this.httpClient = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper();
  }

  @Async
  public CompletableFuture<Map<String, Object>> sendPostRequest(String url) {
    log.info("current working thread: {}", Thread.currentThread().getName());
    //final String jsonBody = this.convertMap2Json(body);
    Map<String, Object> res = Collections.emptyMap();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .GET()
        .build();

    try {
      String httpRes =  httpClient.send(request, HttpResponse.BodyHandlers.ofString())
          .body();
      res = this.convertJson2Map(httpRes);

    } catch (IOException | InterruptedException ex) {
      if (ex instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      CompletableFuture.failedFuture(ex);
    }

    return CompletableFuture.completedFuture(res);
  }

  private String convertMap2Json(Map<String, Object> bodyMap) {
    try {
      return this.objectMapper.writeValueAsString(bodyMap);
    } catch (JsonProcessingException ignored) {
    }

    return "";
  }

  private Map<String, Object> convertJson2Map(String jsonStr) {
      try {
        return this.objectMapper.readValue(jsonStr, Map.class);
      } catch (JsonProcessingException ignored) {

      }
      return Collections.emptyMap();
  }

}
