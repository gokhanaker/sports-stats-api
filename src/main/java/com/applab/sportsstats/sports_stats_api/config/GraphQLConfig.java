package com.applab.sportsstats.sports_stats_api.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)  // This expects OffsetDateTime
                .scalar(ExtendedScalars.GraphQLLong);
    }

    // (Optional) Ensure JavaTimeModule registered if needed elsewhere
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
