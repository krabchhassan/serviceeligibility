package com.cegedim.next.serviceeligibility.core.utils;

import org.springframework.security.core.Authentication;

/** This interface defines methods allowing to get authentication informations. */
public interface AuthenticationFacade {

  /**
   * Allows to get an {@link Authentication} for HTTP request.
   *
   * @return an instance of {@link Authentication}.
   */
  Authentication getAuthentication();

  /**
   * Allows to get a principal for HTTP request.
   *
   * @return an instance of {@link Object}.
   */
  Object getPrincipal();

  /**
   * Allows to get the authentication user name if authenticated else <code>Anonymous</code>
   *
   * @return an instance of {@link String}.
   */
  String getAuthenticationUserName();
}
