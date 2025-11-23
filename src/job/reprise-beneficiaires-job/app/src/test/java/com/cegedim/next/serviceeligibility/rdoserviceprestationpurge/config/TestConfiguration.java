package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {}
