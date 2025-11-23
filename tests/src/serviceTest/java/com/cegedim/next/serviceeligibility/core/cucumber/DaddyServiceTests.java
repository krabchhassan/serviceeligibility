package com.cegedim.next.serviceeligibility.core.cucumber;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("feature")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "com.cegedim.next.serviceeligibility.core.cucumber")
@CucumberContextConfiguration
@SpringBootTest(classes = TestSpringApplication.class)
public class DaddyServiceTests {}
