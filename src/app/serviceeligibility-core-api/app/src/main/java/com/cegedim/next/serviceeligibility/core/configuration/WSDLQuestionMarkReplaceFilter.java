package com.cegedim.next.serviceeligibility.core.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class WSDLQuestionMarkReplaceFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // making sure the base constructor doesn't do unnecessary operations
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    if (StringUtils.containsIgnoreCase(httpRequest.getQueryString(), "wsdl")) {
      request
          .getRequestDispatcher(httpRequest.getRequestURI() + ".wsdl")
          .forward(request, response);
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    // Making sure the base method doesn't do anything
  }
}
