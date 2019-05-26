package com.infoshareacademy.marbles.domain;

import javafx.scene.control.Label;

public class TimerLabel {

  private static Label label;

  private TimerLabel(String s) {
    label = new Label(s);
  }

  public static Label getInstance() {
    if (label == null) {
      label = new Label("Your time:");
      init();
    }
    return label;
  }

  private static void init() {
    label.getStyleClass().add("leftPanelTitle");
  }
}
