package com.cegedim.next.serviceeligibility.core.configuration;

import com.cegedim.beyond.serviceeligibility.common.config.OrganisationWrapperConfiguration;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.RDOServicePrestationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({S3Config.class, OrganisationWrapperConfiguration.class})
public class ApiConfiguration {

  private final OrganisationServiceWrapper organisationServiceWrapper;

  private final MongoTemplate mongoTemplate;

  private final BeyondPropertiesService beyondPropertiesService;

  public ApiConfiguration(
      OrganisationServiceWrapper organisationServiceWrapper,
      MongoTemplate mongoTemplate,
      BeyondPropertiesService beyondPropertiesService) {
    this.organisationServiceWrapper = organisationServiceWrapper;
    this.mongoTemplate = mongoTemplate;
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public OcService ocService() {
    return new OcService(organisationServiceWrapper);
  }

  @Bean
  public RDOServicePrestationService rdoServicePrestationService() {
    return new RDOServicePrestationService(
        rdoServicePrestationDAO(), assureRdoServicePrestationsMapper(), beyondPropertiesService);
  }

  @Bean
  public RDOServicePrestationDAO rdoServicePrestationDAO() {
    return new RDOServicePrestationDAOImpl(mongoTemplate);
  }

  @Bean
  public AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }
}
