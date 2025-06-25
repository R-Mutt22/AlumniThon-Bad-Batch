package com.bad.batch.integration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.bad.batch.model.entities")
@EnableJpaRepositories(basePackages = "com.bad.batch.repository")
public class TestConfiguration {
}
