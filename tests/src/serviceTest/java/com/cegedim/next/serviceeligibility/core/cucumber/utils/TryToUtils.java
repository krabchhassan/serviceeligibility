package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public class TryToUtils {

  /**
   * Permet de catch toutes exceptions ou assertions error et de les ignore si tryTo est valorise
   */
  public static Optional<HttpClientErrorException> tryTo(Runnable runnable, Boolean tryTo) {
    try {
      runnable.run();
    } catch (Throwable e) {
      if (tryTo == null) {
        log.info(e.getMessage(), e);
        throw e;
      } else {
        if (e instanceof HttpClientErrorException) {
          return Optional.of((HttpClientErrorException) e);
        }
      }
    }
    return Optional.empty();
  }
}
