package com.applab.sportsstats.sports_stats_api.enums;

public enum MatchStatus {
  SCHEDULED("SCHEDULED"),
  LIVE("LIVE"),
  COMPLETED("COMPLETED"),
  CANCELLED("CANCELLED"),
  POSTPONED("POSTPONED");

  private String status;

  MatchStatus(String status) {
      this.status = status;
  }

  public String getStatus() {
      return status;
  }
}
