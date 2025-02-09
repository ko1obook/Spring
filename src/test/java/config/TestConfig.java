package config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "ru.education.jpa")
@ComponentScan(basePackages = "ru.education.service.impl")
@EntityScan(basePackages = "ru.education.entity")
public class TestConfig {
}
