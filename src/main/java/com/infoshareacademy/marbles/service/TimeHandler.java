package com.infoshareacademy.marbles.service;

import com.infoshareacademy.marbles.game.MarbleState;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimeHandler {

  public static String getFormattedTime(Long time) {
    return String.format("%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(time),
        TimeUnit.MILLISECONDS.toMinutes(time) % 60,
        TimeUnit.MILLISECONDS.toSeconds(time) % 60);
  }

  public static Timeline createTimeline(MarbleState marbleState) {
    Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(1000),
        ae -> {
          Duration duration = ((KeyFrame) ae.getSource()).getTime();
          marbleState.getTimeProperty().addValue(Double.valueOf(duration.toMillis()).longValue());
        }));
    timeline.setCycleCount(Timeline.INDEFINITE);
    return timeline;
  }
}
