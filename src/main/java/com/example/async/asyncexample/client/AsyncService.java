package com.example.async.asyncexample.client;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Service
public class AsyncService {

  @Autowired
  private final Executor threadPoolTaskExecutor;

  @Async("threadPoolTaskExecutorDup")
  public CompletableFuture<String> doSomethingAsync() {
    return CompletableFuture.supplyAsync(() -> {
      log.info("Current Executing Thread is {}", Thread.currentThread().getName());
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "Success";
    }, threadPoolTaskExecutor).exceptionally(e -> {
      return "";
    }).thenApplyAsync(res -> {
      return "success";
    }, threadPoolTaskExecutor);
  }
}
