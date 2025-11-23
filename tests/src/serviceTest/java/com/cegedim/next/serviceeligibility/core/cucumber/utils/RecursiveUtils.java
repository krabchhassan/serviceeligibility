package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RecursiveUtils {

  public static <T> T defaultRecursive(Callable<T> call, Predicate<T> validation) throws Exception {
    return recursiveCall(3, call, validation, 3, 0);
  }

  public static <T> T recursiveCall(
      long delaySeconds, Callable<T> call, Predicate<T> validation, int maxRetry) throws Exception {
    return recursiveCall(delaySeconds, call, validation, maxRetry, 0);
  }

  private static <T> T recursiveCall(
      long seconds, Callable<T> call, Predicate<T> validation, int maxRetry, int retryCount)
      throws Exception {
    try {
      T resultat = call.call();
      if (validation.test(resultat)) {
        return resultat;
      }
    } catch (Exception e) {
      // Ignore les exceptions lors de l appel
      log.debug(e.getMessage());
    }

    if (retryCount < maxRetry) {
      TimeUnit.SECONDS.sleep(seconds);
      return recursiveCall(seconds, call, validation, maxRetry, retryCount + 1);
    }
    throw new Exception(String.format("max retry %d", maxRetry));
  }
}
