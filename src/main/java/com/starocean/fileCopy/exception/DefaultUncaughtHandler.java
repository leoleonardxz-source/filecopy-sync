package com.starocean.fileCopy.exception;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DefaultUncaughtHandler {
  private final ExceptionCenter exceptionCenter;

  public DefaultUncaughtHandler(ExceptionCenter exceptionCenter) {
    this.exceptionCenter = exceptionCenter;
  }

  @PostConstruct
  public void init() {
    Thread.setDefaultUncaughtExceptionHandler((thread, ex) ->
        exceptionCenter.handle("uncaught", ex, "thread=" + thread.getName())
    );
  }
}

