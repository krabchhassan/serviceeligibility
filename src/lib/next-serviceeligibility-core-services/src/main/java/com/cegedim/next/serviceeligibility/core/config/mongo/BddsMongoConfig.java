package com.cegedim.next.serviceeligibility.core.config.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.cegedim.next.serviceeligibility.core")
public class BddsMongoConfig {}
