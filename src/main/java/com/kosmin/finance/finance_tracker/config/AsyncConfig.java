package com.kosmin.finance.finance_tracker.config;

import java.util.Optional;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
  @Value("${spring.async.config.core-pool-size}")
  private int corePoolSize;

  @Value("${spring.async.config.max-pool-size}")
  private int maxPoolSize;

  @Value("${spring.async.config.queue-capacity}")
  private int queueCapacity;

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Optional.of(corePoolSize).orElse(5));
    executor.setMaxPoolSize(Optional.of(maxPoolSize).orElse(10));
    executor.setQueueCapacity(Optional.of(queueCapacity).orElse(100));
    executor.setThreadNamePrefix("AsyncThread-");
    executor.initialize();
    return executor;
  }

  @Override
  public Executor getAsyncExecutor() {
    return taskExecutor();
  }
}
