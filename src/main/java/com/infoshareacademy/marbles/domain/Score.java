package com.infoshareacademy.marbles.domain;

public class Score {

  private String name;
  private Long time;
  private Integer score;

  public Score(String name, Long time, Integer score) {
    this.name = name;
    this.time = time;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public Long getTime() {
    return time;
  }

  public Integer getScore() {
    return score;
  }
}
