package com.cegedim.beyond.serviceeligibility.common.organisation;

import com.cegedim.beyond.serviceeligibility.common.config.OrganisationWrapperConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({DummyConfiguration.class})
@ImportAutoConfiguration({OrganisationWrapperConfiguration.class})
public class DummyApplication {}
