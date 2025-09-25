package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import com.applab.sportsstats.sports_stats_api.service.MatchEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

/**
 * GraphQL Subscription resolver for real-time updates.
 * Handles WebSocket-based subscriptions for live sports data.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class SubscriptionResolver {

    private final MatchEventPublisher matchEventPublisher;

    /**
     * Subscription for live match score updates for a specific match.
     * Clients can subscribe to this to receive real-time score updates.
     *
     * Example GraphQL subscription:
     * subscription {
     *   matchScoreUpdate(matchId: "1") {
     *     id
     *     homeTeamScore
     *     awayTeamScore
     *     status
     *     homeTeam { name }
     *     awayTeam { name }
     *   }
     * }
     *
     * @param matchId The ID of the match to subscribe to
     * @return Flux stream of match updates for the specified match
     */
    @SubscriptionMapping
    public Flux<Match> matchScoreUpdate(@Argument String matchId) {
        log.info("Starting matchScoreUpdate subscription for match ID: {}", matchId);

        try {
            Long matchIdLong = Long.parseLong(matchId);
            
            // Validate that the matchId is provided
            if (matchIdLong <= 0) {
                log.warn("Invalid match ID provided for subscription: {}", matchId);
                return Flux.error(new IllegalArgumentException(
                    "Invalid match ID. Must be a positive number."));
            }

            log.debug("Creating subscription flux for match ID: {}", matchIdLong);
            
            return matchEventPublisher.getMatchUpdatesFlux(matchIdLong)
                    .doOnNext(match -> log.debug(
                        "Sending match update to subscriber - Match ID: {}, Home: {}, Away: {}",
                        match.getId(), match.getHomeTeamScore(), match.getAwayTeamScore()))
                    .doOnComplete(() -> log.info(
                        "Match subscription completed for match ID: {}", matchIdLong))
                    .doOnError(error -> log.error(
                        "Error in match subscription for match ID: {}", matchIdLong, error));

        } catch (NumberFormatException e) {
            log.error("Invalid match ID format for subscription: {}", matchId, e);
            return Flux.error(new IllegalArgumentException(
                "Match ID must be a valid number: " + matchId));
        }
    }

    /**
     * Subscription for all live match updates across all matches.
     * Useful for dashboard applications that want to show all live sports data.
     *
     * Example GraphQL subscription:
     * subscription {
     *   liveMatchUpdates {
     *     id
     *     homeTeamScore
     *     awayTeamScore
     *     status
     *     homeTeam { name }
     *     awayTeam { name }
     *   }
     * }
     *
     * @return Flux stream of all match updates
     */
    @SubscriptionMapping
    public Flux<Match> liveMatchUpdates() {
        log.info("Starting liveMatchUpdates subscription for all matches");

        return matchEventPublisher.getAllMatchUpdatesFlux()
                .doOnNext(match -> log.debug(
                    "Sending live match update to global subscriber - Match ID: {}, Status: {}",
                    match.getId(), match.getStatus()))
                .doOnComplete(() -> log.info("Global live match subscription completed"))
                .doOnError(error -> log.error("Error in global live match subscription", error));
    }

    /**
     * Subscription for match status changes (SCHEDULED -> LIVE -> COMPLETED).
     * Clients can subscribe to this to be notified when matches start, end, etc.
     *
     * Example GraphQL subscription:
     * subscription {
     *   matchStatusUpdate(matchId: "1") {
     *     id
     *     status
     *     matchDate
     *     homeTeam { name }
     *     awayTeam { name }
     *   }
     * }
     *
     * @param matchId The ID of the match to subscribe to
     * @return Flux stream of match status updates for the specified match
     */
    @SubscriptionMapping
    public Flux<Match> matchStatusUpdate(@Argument String matchId) {
        log.info("Starting matchStatusUpdate subscription for match ID: {}", matchId);

        try {
            Long matchIdLong = Long.parseLong(matchId);
            
            if (matchIdLong <= 0) {
                log.warn("Invalid match ID provided for status subscription: {}", matchId);
                return Flux.error(new IllegalArgumentException(
                    "Invalid match ID. Must be a positive number."));
            }

            return matchEventPublisher.getMatchUpdatesFlux(matchIdLong)
                    .doOnNext(match -> log.debug(
                        "Sending match status update to subscriber - Match ID: {}, Status: {}",
                        match.getId(), match.getStatus()))
                    .doOnComplete(() -> log.info(
                        "Match status subscription completed for match ID: {}", matchIdLong))
                    .doOnError(error -> log.error(
                        "Error in match status subscription for match ID: {}", matchIdLong, error));

        } catch (NumberFormatException e) {
            log.error("Invalid match ID format for status subscription: {}", matchId, e);
            return Flux.error(new IllegalArgumentException(
                "Match ID must be a valid number: " + matchId));
        }
    }
}