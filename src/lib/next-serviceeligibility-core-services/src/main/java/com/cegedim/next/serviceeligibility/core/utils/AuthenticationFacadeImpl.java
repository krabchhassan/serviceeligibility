package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.beyond.spring.starter.security.user.UserContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public Object getPrincipal() {
    final Authentication authentication = getAuthentication();

    return authentication != null ? authentication.getPrincipal() : null;
  }

  @Override
  public String getAuthenticationUserName() {
    return getAuthenticationName();
  }

  private String getAuthenticationName() {
    return UserContextUtils.getUser(SecurityContextHolder.getContext().getAuthentication());
  }
}
