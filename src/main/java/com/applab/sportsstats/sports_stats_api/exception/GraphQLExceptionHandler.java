package com.applab.sportsstats.sports_stats_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GraphQLExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @GraphQlExceptionHandler
    public GraphQLError handleIllegalArgumentException(IllegalArgumentException ex, DataFetchingEnvironment env) {
        logger.error("IllegalArgumentException in GraphQL: {}", ex.getMessage(), ex);
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .errorType(graphql.ErrorType.ValidationError)
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleConstraintViolationException(ConstraintViolationException ex, DataFetchingEnvironment env) {
        logger.error("ConstraintViolationException in GraphQL: {}", ex.getMessage(), ex);
        String violations = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .reduce((v1, v2) -> v1 + ", " + v2)
                .orElse("Validation failed");
        
        return GraphqlErrorBuilder.newError()
                .message("Validation error: " + violations)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .errorType(graphql.ErrorType.ValidationError)
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGenericException(Exception ex, DataFetchingEnvironment env) {
        logger.error("Unexpected error in GraphQL: {}", ex.getMessage(), ex);
        return GraphqlErrorBuilder.newError()
                .message("An unexpected error occurred: " + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .errorType(graphql.ErrorType.DataFetchingException)
                .build();
    }
}