package com.infoshareacademy.marbles.layout;

import com.infoshareacademy.marbles.domain.Score;
import com.infoshareacademy.marbles.service.TimeHandler;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class ScorePopup {

  public Alert createScoreConfirmationPopup() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("End of game");
    alert.setContentText("Do you want to save your score?");
    ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
    ButtonType noButton = new ButtonType("No", ButtonData.NO);
    alert.getButtonTypes().setAll(okButton, noButton);
    return alert;
  }

  public TextInputDialog createScoreNamePopup() {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Save your score");
    dialog.setHeaderText("Game has ended. You can save your score.");
    dialog.setContentText("Please enter your name:");

    return dialog;
  }

  public Alert createScoresHistoryPopup(
      List<Score> scores) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Best results");
    alert.setHeaderText(null);
    ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
    ButtonType clearButton = new ButtonType("Clear All Stats", ButtonData.OTHER);
    alert.getButtonTypes().setAll(okButton, clearButton);

    StringBuilder sb = new StringBuilder();

    scores.forEach(s -> {
      sb.append(s.getName() + " " + TimeHandler.getFormattedTime(s.getTime()) + " " + s.getScore()
          + "\n");
    });

    alert.setContentText(sb.toString());

    return alert;
  }
}
