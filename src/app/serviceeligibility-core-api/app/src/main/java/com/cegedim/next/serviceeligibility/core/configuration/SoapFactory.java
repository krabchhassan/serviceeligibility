package com.cegedim.next.serviceeligibility.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.TransportInputStream;

public class SoapFactory implements SoapMessageFactory, InitializingBean {
  private static final String REQUEST_CONTEXT_ATTRIBUTE = "MyFactory";

  private static final Log logger = LogFactory.getLog(SoapFactory.class);

  private final SaajSoapMessageFactory soap11MessageFactory = new SaajSoapMessageFactory();
  private final SaajSoapMessageFactory soap12MessageFactory = new SaajSoapMessageFactory();

  private void setMessageFactoryForRequestContext(final SaajSoapMessageFactory mf) {
    RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
    assert attrs != null;
    attrs.setAttribute(REQUEST_CONTEXT_ATTRIBUTE, mf, RequestAttributes.SCOPE_REQUEST);
  }

  private SaajSoapMessageFactory getMessageFactoryForRequestContext() {
    RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
    assert attrs != null;
    SaajSoapMessageFactory saajSoapMessageFactory =
        (SaajSoapMessageFactory)
            attrs.getAttribute(REQUEST_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    assert saajSoapMessageFactory != null;
    return saajSoapMessageFactory;
  }

  @Override
  public SoapMessage createWebServiceMessage() {
    return getMessageFactoryForRequestContext().createWebServiceMessage();
  }

  @Override
  public SoapMessage createWebServiceMessage(final InputStream inputStream) throws IOException {
    setMessageFactoryForRequestContext(this.soap12MessageFactory);
    if (inputStream instanceof TransportInputStream transportInputStream
        && useSoap11(transportInputStream)) {
      setMessageFactoryForRequestContext(this.soap11MessageFactory);
    }

    SaajSoapMessageFactory mf = getMessageFactoryForRequestContext();
    if (mf == this.soap11MessageFactory) {
      logger.debug("Final soapMessageFactory? " + this.soap11MessageFactory);
    } else {
      logger.debug("Final soapMessageFactory? " + this.soap12MessageFactory);
    }
    return mf.createWebServiceMessage(inputStream);
  }

  @Override
  public void setSoapVersion(final SoapVersion version) {
    logger.debug("setSoapVersion called with: " + version + " -- ignoring");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.soap11MessageFactory.setSoapVersion(SoapVersion.SOAP_11);
    this.soap11MessageFactory.afterPropertiesSet();
    this.soap12MessageFactory.setSoapVersion(SoapVersion.SOAP_12);
    this.soap12MessageFactory.afterPropertiesSet();
  }

  private boolean useSoap11(final TransportInputStream transportInputStream) throws IOException {
    for (Iterator<String> headerNames = transportInputStream.getHeaderNames();
        headerNames.hasNext(); ) {
      String headerName = headerNames.next();
      logger.debug("found headerName: " + headerName);
      for (Iterator<String> headerValues = transportInputStream.getHeaders(headerName);
          headerValues.hasNext(); ) {
        String headerValue = headerValues.next();
        logger.debug("     headerValue? " + headerValue);
        // Something weird with case names
        if (headerName.toLowerCase().contains("content-type")) {
          logger.debug("Content Type  - " + headerValue);

          if (headerValue.trim().toLowerCase().contains("text/xml")) {
            logger.debug("Found text/xml in header.  Using SOAP 1.1");
            return true;
          }
        }
      }
    }
    return false;
  }
}
