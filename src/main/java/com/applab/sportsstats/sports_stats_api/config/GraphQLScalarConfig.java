package com.applab.sportsstats.sports_stats_api.config;

import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GraphQLScalarConfig {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    @Bean
    public GraphQLScalarType dateScalar() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("ISO-8601 Date scalar (yyyy-MM-dd)")
                .coercing(new SimpleCoercing<>(
                        s -> LocalDate.parse(s, DATE_FORMAT),
                        v -> ((LocalDate) v).format(DATE_FORMAT)
                ))
                .build();
    }

    @Bean
    public GraphQLScalarType dateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("ISO-8601 DateTime scalar (yyyy-MM-dd'T'HH:mm:ss[.SSS]XX)")
                .coercing(new SimpleCoercing<>(
                        s -> LocalDateTime.parse(s, DATE_TIME_FORMAT),
                        v -> ((LocalDateTime) v).format(DATE_TIME_FORMAT)
                ))
                .build();
    }

    // Generic small coercing helper
    private static class SimpleCoercing<T> implements graphql.schema.Coercing<T, String> {
        private final java.util.function.Function<String, T> parse;
        private final java.util.function.Function<T, String> serialize;

        SimpleCoercing(java.util.function.Function<String, T> parse, java.util.function.Function<T, String> serialize) {
            this.parse = parse;
            this.serialize = serialize;
        }

        @Override
        public String serialize(Object dataFetcherResult) {
            return serialize.apply((T) dataFetcherResult);
        }

        @Override
        public T parseValue(Object input) {
            return parseLiteral(input);
        }

        @Override
        public T parseLiteral(Object input) {
            return parse.apply(input.toString());
        }
    }
}
