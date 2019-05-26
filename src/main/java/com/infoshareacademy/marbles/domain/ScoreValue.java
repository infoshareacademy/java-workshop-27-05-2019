package com.infoshareacademy.marbles.domain;

import com.infoshareacademy.marbles.game.MarbleState;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;

public class ScoreValue {

  private static Label label;

  private ScoreValue(String s) {
    label = new Label(s);
  }

  public static Label getInstance(MarbleState marbleState) {
    if (label == null) {
      label = new Label(String.valueOf(marbleState.getScoreProperty().getValue()));
      init(marbleState);
    }
    return label;
  }

  private static void init(MarbleState marbleState) {
    label.textProperty().bind(Bindings.convert(marbleState.getScoreProperty().valueProperty()));
    label.getStyleClass().addAll("leftPanelValue", "leftPanelScoreValue");
  }
}
