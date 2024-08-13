package com.example.async.asyncexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
  @Bean
  public Executor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(30);
    executor.setThreadNamePrefix("Hs Async Pool-");
    executor.initialize();
    return executor;
  }

  @Bean(name = "threadPoolTaskExecutorDup")
  public Executor threadPoolTaskExecutorDup() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(30);
    executor.setThreadNamePrefix("Dup Async Pool-");
    executor.initialize();
    return executor;
  }
}
