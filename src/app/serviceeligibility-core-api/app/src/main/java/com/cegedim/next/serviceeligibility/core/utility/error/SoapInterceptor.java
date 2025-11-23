package com.cegedim.next.serviceeligibility.core.utility.error;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class SoapInterceptor implements EndpointInterceptor {

  private static final String ACCESS_DENIED = "Access denied";

  @Override
  public boolean handleRequest(MessageContext messageContext, Object endpoint) {
    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext, Object endpoint) {
    return true;
  }

  @Override
  public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
    DefaultMessageContext soapMessageContext = (DefaultMessageContext) messageContext;
    SaajSoapMessage saajSoapMessage = (SaajSoapMessage) soapMessageContext.getResponse();
    if (saajSoapMessage.getSaajMessage() != null
        && saajSoapMessage.getSaajMessage().getSOAPBody() != null
        && ACCESS_DENIED.equals(
            saajSoapMessage.getSaajMessage().getSOAPBody().getFault().getFaultString())) {
      throw new AccessDeniedException(ACCESS_DENIED);
    }
    return true;
  }

  @Override
  public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) {
    // nothing to do yet
  }
}
