package com.example.async.asyncexample;

import com.example.async.asyncexample.client.AsyncService;
import com.example.async.asyncexample.client.HttpClientService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class AsyncExampleApplicationTests {

  @Autowired
  private AsyncService asyncService;

  @Autowired
  private HttpClientService httpClientService;
  private static final String URL = "https://jsonplaceholder.typicode.com/posts/%d";

  @Test
  public void executePostRequest() {
    Map<String, Object> params = Maps.newHashMap("title", "foo");
    List<CompletableFuture<Map<String, Object>>> futureRes = IntStream.range(0, 10)
        .mapToObj(cur -> httpClientService.sendPostRequest(String.format(URL, cur)))
        .collect(Collectors.toList());

    assertEquals(futureRes.size(), 10);
    List<Map<String, Object>> res = futureRes.stream().map(cur -> {
      try {
        return cur.get();
      } catch (ExecutionException | InterruptedException ex) {
        if (ex instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());

    assertEquals(res.size(), 10);
  }


  @Test
  public void executeCompletableFutureMethodChaining() {
    // forkjoinpool if executor not specified
    // threadpool if exists
    IntStream.range(0, 10).forEach(curIndex -> asyncService.doSomethingAsync());
  }

}
