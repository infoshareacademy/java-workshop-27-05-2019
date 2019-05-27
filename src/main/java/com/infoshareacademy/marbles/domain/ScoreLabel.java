package com.infoshareacademy.marbles.domain;

import javafx.scene.control.Label;

public class ScoreLabel {

  private static Label label;

  private ScoreLabel(String s) {
    label = new Label(s);
  }

  public static Label getInstance() {
    if (label == null) {
      label = new Label("Your score:");
      init();
    }
    return label;
  }

  private static void init() {
    label.getStyleClass().add("leftPanelTitle");
  }
}
