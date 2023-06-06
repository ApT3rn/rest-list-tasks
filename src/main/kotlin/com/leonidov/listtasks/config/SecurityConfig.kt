package com.leonidov.listtasks.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@ComponentScan(basePackages = ["com.leonidov.listtasks"])
@EnableMongoRepositories("com.leonidov.listtasks.persistence")
class SecurityConfig {
}