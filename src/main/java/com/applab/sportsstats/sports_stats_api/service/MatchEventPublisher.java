package com.applab.sportsstats.sports_stats_api.service;

import com.applab.sportsstats.sports_stats_api.entity.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service for publishing match score update events to GraphQL subscriptions.
 * Uses Reactor Sinks to manage real-time event streaming.
 */
@Slf4j
@Service
public class MatchEventPublisher {

    /**
     * Map of match-specific sinks for targeted subscriptions.
     * Key: matchId, Value: Sink for that specific match
     */
    private final ConcurrentMap<Long, Sinks.Many<Match>> matchSinks = new ConcurrentHashMap<>();

    /**
     * Global sink for all match updates (for general subscriptions)
     */
    private final Sinks.Many<Match> globalMatchSink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    /**
     * Publishes a match score update event to all subscribed clients.
     *
     * @param match The updated match with new scores
     */
    public void publishMatchScoreUpdate(Match match) {
        log.info("Publishing match score update for match ID: {}, homeScore: {}, awayScore: {}",
                match.getId(), match.getHomeTeamScore(), match.getAwayTeamScore());

        try {
            // Publish to global match updates
            globalMatchSink.tryEmitNext(match);

            // Publish to match-specific subscription if exists
            Sinks.Many<Match> matchSink = matchSinks.get(match.getId());
            if (matchSink != null) {
                matchSink.tryEmitNext(match);
            }

            log.debug("Successfully published match score update for match ID: {}", match.getId());
        } catch (Exception e) {
            log.error("Failed to publish match score update for match ID: {}", match.getId(), e);
        }
    }

    /**
     * Creates or gets a Flux stream for a specific match.
     * Used by subscription resolvers to listen to match-specific updates.
     *
     * @param matchId The ID of the match to subscribe to
     * @return Flux stream of match updates for the specified match
     */
    public Flux<Match> getMatchUpdatesFlux(Long matchId) {
        log.info("Creating subscription flux for match ID: {}", matchId);

        // Create a new sink for this match if it doesn't exist
        Sinks.Many<Match> matchSink = matchSinks.computeIfAbsent(matchId, id -> {
            log.debug("Creating new sink for match ID: {}", id);
            return Sinks.many()
                    .multicast()
                    .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
        });

        return matchSink.asFlux()
                .filter(match -> match.getId().equals(matchId))  // Extra safety filter
                .doOnSubscribe(subscription -> 
                    log.info("New subscription created for match ID: {}", matchId))
                .doOnCancel(() -> {
                    log.info("Subscription cancelled for match ID: {}", matchId);
                    // Clean up the sink if no more subscribers
                    if (matchSink.currentSubscriberCount() == 0) {
                        matchSinks.remove(matchId);
                        log.debug("Removed sink for match ID: {} (no more subscribers)", matchId);
                    }
                })
                .doOnError(error -> 
                    log.error("Error in match subscription for match ID: {}", matchId, error));
    }

    /**
     * Gets a Flux stream for all match updates (not filtered by match ID).
     * Useful for dashboard-style subscriptions that want all live updates.
     *
     * @return Flux stream of all match updates
     */
    public Flux<Match> getAllMatchUpdatesFlux() {
        log.info("Creating subscription flux for all match updates");

        return globalMatchSink.asFlux()
                .doOnSubscribe(subscription -> 
                    log.info("New subscription created for all match updates"))
                .doOnCancel(() -> 
                    log.info("Subscription cancelled for all match updates"))
                .doOnError(error -> 
                    log.error("Error in global match subscription", error));
    }

    /**
     * Gets the current number of active subscriptions for a specific match.
     *
     * @param matchId The match ID to check
     * @return Number of active subscriptions for the match
     */
    public int getSubscriberCount(Long matchId) {
        Sinks.Many<Match> matchSink = matchSinks.get(matchId);
        return matchSink != null ? matchSink.currentSubscriberCount() : 0;
    }

    /**
     * Gets the total number of active global subscriptions.
     *
     * @return Number of active global subscriptions
     */
    public int getGlobalSubscriberCount() {
        return globalMatchSink.currentSubscriberCount();
    }

    /**
     * Publishes a match status change event (e.g., SCHEDULED -> LIVE -> COMPLETED).
     *
     * @param match The match with updated status
     */
    public void publishMatchStatusUpdate(Match match) {
        log.info("Publishing match status update for match ID: {}, status: {}",
                match.getId(), match.getStatus());

        // Reuse the same publishing mechanism as score updates
        publishMatchScoreUpdate(match);
    }

    /**
     * Cleans up all sinks and subscriptions.
     * Useful for application shutdown or testing.
     */
    public void shutdown() {
        log.info("Shutting down MatchEventPublisher");

        try {
            // Complete all match-specific sinks
            matchSinks.values().forEach(sink -> {
                try {
                    sink.tryEmitComplete();
                } catch (Exception e) {
                    log.warn("Error completing match sink during shutdown", e);
                }
            });
            matchSinks.clear();

            // Complete global sink
            globalMatchSink.tryEmitComplete();

            log.info("MatchEventPublisher shutdown completed");
        } catch (Exception e) {
            log.error("Error during MatchEventPublisher shutdown", e);
        }
    }
}