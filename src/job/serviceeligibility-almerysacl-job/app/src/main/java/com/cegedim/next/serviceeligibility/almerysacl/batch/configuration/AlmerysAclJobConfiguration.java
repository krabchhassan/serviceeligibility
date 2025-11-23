package com.cegedim.next.serviceeligibility.almerysacl.batch.configuration;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OmuHelperConfiguration.class})
public class AlmerysAclJobConfiguration {}
