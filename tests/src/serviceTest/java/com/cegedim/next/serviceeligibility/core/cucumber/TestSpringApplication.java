package com.cegedim.next.serviceeligibility.core.cucumber;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = "com.cegedim.next.serviceeligibility.core")
@EnableRetry
public class TestSpringApplication {}
