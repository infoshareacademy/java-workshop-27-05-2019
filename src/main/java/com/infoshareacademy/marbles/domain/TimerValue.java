package com.infoshareacademy.marbles.domain;

import com.infoshareacademy.marbles.game.MarbleState;
import com.infoshareacademy.marbles.service.TimeHandler;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;

public class TimerValue {

  private static Label label;

  private TimerValue(String s) {
    label = new Label(s);
  }

  public static Label getInstance(MarbleState marbleState) {
    if (label == null) {
      label = new Label(String.valueOf(marbleState.getTimeProperty().getValue()));
      init(marbleState);
    }
    return label;
  }

  private static void init(MarbleState marbleState) {
    label.getStyleClass().addAll("leftPanelValue", "leftPanelTimerValue");
    label.textProperty().bind(Bindings
        .createStringBinding(
            () -> TimeHandler.getFormattedTime(marbleState.getTimeProperty().getValue()),
            marbleState.getTimeProperty().valueProperty()));
  }
}
