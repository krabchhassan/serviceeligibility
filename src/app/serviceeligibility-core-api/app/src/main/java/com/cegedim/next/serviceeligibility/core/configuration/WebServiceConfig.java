package com.cegedim.next.serviceeligibility.core.configuration;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.WSDL_API_PATH;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.utility.DateSerializer;
import com.cegedim.next.serviceeligibility.core.utility.error.SoapInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.ws.commons.schema.resolver.DefaultURIResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  private static final String WSDL = ".wsdl";

  private static final String SERVICE1 = "/services/consultationDroits";
  private static final String SERVICE2 = "/services/consultationDroitsV2";
  private static final String SERVICE3 = "/services/consultationDroitsV3";
  private static final String SERVICE4 = "/services/consultationDroitsV4";
  private static final String SERVICE_CARTE = "/services/carteDematerialisee";
  private static final String SERVICE_CARTE2 = "/services/carteDematerialiseeV2";
  private static final String BDD_PORT_NAME = "BaseDeDroit";

  private final BeyondPropertiesService beyondPropertiesService;

  public WebServiceConfig(BeyondPropertiesService beyondPropertiesService) {
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServletConsultation(
      ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(false);
    return new ServletRegistrationBean<>(
        servlet,
        SERVICE1,
        SERVICE2,
        SERVICE3,
        SERVICE4,
        SERVICE_CARTE,
        SERVICE_CARTE2,
        SERVICE1 + WSDL,
        SERVICE2 + WSDL,
        SERVICE3 + WSDL,
        SERVICE4 + WSDL,
        SERVICE_CARTE + WSDL,
        SERVICE_CARTE2 + WSDL);
  }

  private DefaultWsdl11Definition getWsdlFile(
      final String hostName,
      final String serviceName,
      final String portName,
      final boolean isCarte,
      int carteVersion) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName(portName);
    wsdl11Definition.setServiceName(serviceName);
    wsdl11Definition.setLocationUri(hostName + serviceName);
    if (isCarte) {
      if (carteVersion == 1) {
        wsdl11Definition.setSchemaCollection(contratXsd());
      } else if (carteVersion == 2) {
        wsdl11Definition.setSchemaCollection(contratXsdV2());
      }
    } else {
      wsdl11Definition.setSchemaCollection(benefXsd());
    }

    return wsdl11Definition;
  }

  @Bean
  public XsdSchemaCollection contratXsd() {
    CommonsXsdSchemaCollection xsds =
        new CommonsXsdSchemaCollection(new ClassPathResource("xsd/carteDemat/carteDemat.xsd"));
    xsds.setUriResolver(new DefaultURIResolver());
    xsds.setInline(true);
    return xsds;
  }

  @Bean
  public XsdSchemaCollection contratXsdV2() {
    CommonsXsdSchemaCollection xsds =
        new CommonsXsdSchemaCollection(new ClassPathResource("xsd/carteDemat/carteDematV2.xsd"));
    xsds.setUriResolver(new DefaultURIResolver());
    xsds.setInline(true);
    return xsds;
  }

  @Bean
  public XsdSchemaCollection benefXsd() {
    CommonsXsdSchemaCollection xsds =
        new CommonsXsdSchemaCollection(new ClassPathResource("benef.xsd"));
    xsds.setUriResolver(new DefaultURIResolver());
    xsds.setInline(true);
    return xsds;
  }

  @Bean(name = "consultationDroits")
  public Wsdl11Definition bddV1Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE1,
        BDD_PORT_NAME,
        false,
        0);
  }

  @Bean(name = "consultationDroitsV2")
  public Wsdl11Definition bddV2Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE2,
        BDD_PORT_NAME,
        false,
        0);
  }

  @Bean(name = "consultationDroitsV3")
  public Wsdl11Definition bddV3Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE3,
        BDD_PORT_NAME,
        false,
        0);
  }

  @Bean(name = "consultationDroitsV4")
  public Wsdl11Definition bddV4Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE4,
        BDD_PORT_NAME,
        false,
        0);
  }

  @Bean(name = "carteDematerialisee")
  public Wsdl11Definition cardV1Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE_CARTE,
        "carteDematerialisee",
        true,
        1);
  }

  @Bean(name = "carteDematerialiseeV2")
  public Wsdl11Definition cardV2Wsdl11Definition() {
    return getWsdlFile(
        beyondPropertiesService.getPropertyOrThrowError(WSDL_API_PATH),
        SERVICE_CARTE2,
        "carteDematerialiseeV2",
        true,
        2);
  }

  @Bean
  public SoapMessageFactory messageFactory() {
    return new SoapFactory();
  }

  @Autowired(required = true)
  public void configureJacksonObjectMapper(final ObjectMapper objectMapper) {
    SimpleModule module = new SimpleModule();
    module.addSerializer(XMLGregorianCalendar.class, new DateSerializer());
    objectMapper.registerModule(module);
  }

  @Override
  public void addInterceptors(List<EndpointInterceptor> interceptors) {
    interceptors.add(new SoapInterceptor());
  }
}
