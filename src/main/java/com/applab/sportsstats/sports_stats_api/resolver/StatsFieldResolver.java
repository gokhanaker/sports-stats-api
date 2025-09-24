package com.applab.sportsstats.sports_stats_api.resolver;

import com.applab.sportsstats.sports_stats_api.entity.Stats;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StatsFieldResolver {

    @SchemaMapping(typeName = "Stats", field = "fieldGoalPercentage")
    public double fieldGoalPercentage(Stats stats) {
        return stats.getFieldGoalPercentage();
    }

    @SchemaMapping(typeName = "Stats", field = "threePointPercentage")
    public double threePointPercentage(Stats stats) {
        return stats.getThreePointPercentage();
    }

    @SchemaMapping(typeName = "Stats", field = "freeThrowPercentage")
    public double freeThrowPercentage(Stats stats) {
        return stats.getFreeThrowPercentage();
    }
}
