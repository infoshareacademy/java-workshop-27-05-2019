package com.infoshareacademy.marbles.service;

import static com.infoshareacademy.marbles.layout.MarblesPanel.BALL_FIELD_HEIGHT;
import static com.infoshareacademy.marbles.layout.MarblesPanel.BALL_FIELD_WIDTH;

import com.infoshareacademy.marbles.game.MarbleState;
import com.infoshareacademy.marbles.layout.ScorePopup;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class ButtonCreator {

  private static final Logger logger = Logger.getLogger(ButtonCreator.class.getName());

  public Button createStartButton(MarbleState marbleState, Timeline timeline) {
    Button startGame = new Button("Start game");
    startGame.setId("startGameButton");
    startGame.defaultButtonProperty().bind(Bindings.createBooleanBinding(() -> {
      if (marbleState.getGameStatusProperty().getStartedValue()) {
        startGame.setDisable(true);
        timeline.play();
      } else {
        startGame.setDisable(false);
        timeline.pause();

        if (marbleState.isEndOfGame()) {
          showScoreSaveConfirmation(marbleState);
        }
      }
      return true;
    }, marbleState.getGameStatusProperty().startedValueProperty()));
    startGame.setOnAction(actionEvent -> {
      marbleState.resetGame(marbleState.getTimeProperty());
      marbleState.getGameStatusProperty().setStartedValue(true);
    });

    return startGame;
  }

  public Button createPauseButton(MarbleState marbleState, Timeline timeline) {
    Button pauseGame = new Button("Pause game");
    pauseGame.setId("pauseGameButton");
    pauseGame.defaultButtonProperty().bind(Bindings.createBooleanBinding(() -> {
      if (marbleState.getGameStatusProperty().getStartedValue()) {
        pauseGame.setDisable(false);
      } else {
        pauseGame.setDisable(true);
      }
      if (marbleState.getGameStatusProperty().getEnabledValue()) {
        timeline.play();
      } else {
        timeline.pause();
      }
      return true;
    }, marbleState.getGameStatusProperty().enabledValueProperty()));
    pauseGame.setOnAction(actionEvent -> {
      if (marbleState.getGameStatusProperty().getEnabledValue()) {
        pauseGame.setText("Resume game");
        marbleState.getGameStatusProperty().setEnabledValue(false);
      } else {
        pauseGame.setText("Pause game");
        marbleState.getGameStatusProperty().setEnabledValue(true);
      }
    });
    return pauseGame;
  }

  public Button createShowScoresButton() {
    Button showScores = new Button("Show scores TOP 10");
    showScores.setId("showScoresButton");
    showScores.setOnAction(actionEvent -> showScoresHistory());
    return showScores;
  }

  public Button createMarbleGridButton() {
    Button button = new Button();
    setMarbleGridButtonStyle(button);
    return button;
  }

  public void setMarbleGridButtonStyle(Button button) {
    button.setPadding(Insets.EMPTY);
    button.setPrefWidth(BALL_FIELD_WIDTH);
    button.setPrefHeight(BALL_FIELD_HEIGHT);
    button.getStyleClass().add("rightPanelGridButton");
    BackgroundImage bi = new BackgroundImage(new Image(getClass().getClassLoader()
        .getResourceAsStream("bg.png"), 60, 60, false, true),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT);
    button.setBackground(new Background(bi));

  }

  private void showScoreSaveConfirmation(MarbleState marbleState) {
    new ScorePopup().createScoreConfirmationPopup().showAndWait()
        .ifPresent(type -> {
          if (type.getButtonData() == ButtonData.YES) {
            showScoreNameDialog(marbleState);
          }
        });
  }

  private void showScoreNameDialog(MarbleState marbleState) {
    Optional<String> result = new ScorePopup().createScoreNamePopup().showAndWait();
    result.ifPresent(name -> {
      try {
        new ScoresHandler()
            .saveScore(marbleState.getScoreProperty().getValue(), name,
                marbleState.getTimeProperty().getValue());
      } catch (IOException e) {
        logger.severe(e.getMessage());
      }
    });
  }

  private void showScoresHistory() {
    try {
      new ScorePopup().createScoresHistoryPopup(new ScoresHandler().loadScores()).showAndWait()
          .ifPresent(type -> {
            if (type.getButtonData() == ButtonData.OTHER) {
              try {
                new ScoresHandler().clearStats();
              } catch (IOException e) {
                logger.severe(e.getMessage());
              }
            }
          });
    } catch (IOException e) {
      logger.severe(e.getMessage());
    }
  }
}
