package com.infoshareacademy.marbles.layout;

import static com.infoshareacademy.marbles.game.MarbleState.GRID_FIELD_HEIGHT;
import static com.infoshareacademy.marbles.game.MarbleState.GRID_FIELD_WIDTH;

import com.infoshareacademy.marbles.domain.MarbleStateAction;
import com.infoshareacademy.marbles.game.MarbleState;
import com.infoshareacademy.marbles.game.PathSolver;
import com.infoshareacademy.marbles.service.ButtonCreator;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import javafx.util.Pair;

public class RightPanel extends MarblesPanel {

  private FadeTransition ft = new FadeTransition(Duration.millis(800));
  private int pathElementNo;

  public GridPane build(MarbleState marbleState) {

    ButtonCreator buttonCreator = new ButtonCreator();

    ColumnConstraints cc = new ColumnConstraints();
    cc.setPrefWidth(BALL_FIELD_WIDTH);
    getColumnConstraints().add(cc);

    RowConstraints rc = new RowConstraints();
    rc.setPrefHeight(BALL_FIELD_HEIGHT);
    getRowConstraints().add(rc);

    for (int col = 0; col < GRID_FIELD_WIDTH; col++) {
      for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {

        int finalRow = row;
        int finalColumn = col;

        Button button = buttonCreator.createMarbleGridButton();
        setActionForMarbleGridButton(button, marbleState, col, row);
        add(button, col, row);

        marbleState.getMarbleGrid().valueProperty(col, row).addListener(
            (ChangeListener) (observableValue, oldValue, newValue) -> {
              Button button1 = (Button) getNodeByRowColumnIndex(finalRow, finalColumn,
                  RightPanel.this);
              if (Integer.valueOf(String.valueOf(newValue)) != 0) {
                button1.setGraphic(
                    getImageViewForButton(Integer.valueOf(String.valueOf(newValue))));
              } else {
                button1.setGraphic(null);
              }
            });
      }
    }
    return this;
  }

  private List<Pair<Integer, Integer>> findPath(MarbleStateAction marbleStateAction,
      MarbleState marbleState) {

    PathSolver solver = new PathSolver();
    int startColumn = marbleStateAction.getSourceMarble().getKey();
    int startRow = marbleStateAction.getSourceMarble().getValue();
    int endColumn = marbleStateAction.getTargetMarble().getKey();
    int endRow = marbleStateAction.getTargetMarble().getValue();
    return solver.execute(startColumn, startRow, endColumn, endRow, marbleState.getMarbleGrid());
  }

  private void repaint(MarbleState marbleState,
      MarbleStateAction marbleStateAction) {

    List<Pair<Integer, Integer>> path = findPath(marbleStateAction, marbleState);
    if (!path.isEmpty()) {
      pathElementNo = 0;
      Timeline animationTimeline = new Timeline(new KeyFrame(
          Duration.millis(100),
          ae -> repaint(marbleState, pathElementNo++, path)));
      animationTimeline.setCycleCount(path.size() - 1);
      animationTimeline.play();

      animationTimeline.setOnFinished(actionEvent -> {
        Platform.runLater(() -> {
          marbleState.changeMarbleGrid();
          marbleStateAction.resetMarbleAction();
        });
      });
    } else {
      ft.play();
      marbleStateAction.resetTargetMarble();
    }
  }

  private void repaint(MarbleState marbleState, int i,
      List<Pair<Integer, Integer>> path) {

    ButtonCreator buttonCreator = new ButtonCreator();

    int sourceColumn = path.get(i).getKey();
    int sourceRow = path.get(i).getValue();
    int targetColumn = path.get(i + 1).getKey();
    int targetRow = path.get(i + 1).getValue();

    Button source = (Button) getNodeByRowColumnIndex(sourceRow, sourceColumn, this);
    Button target = (Button) getNodeByRowColumnIndex(targetRow, targetColumn, this);

    getChildren().remove(source);
    getChildren().remove(target);

    add(source, targetColumn, targetRow);
    add(target, sourceColumn, sourceRow);

    setActionForMarbleGridButton(source, marbleState, targetColumn, targetRow);
    setActionForMarbleGridButton(target, marbleState, sourceColumn, sourceRow);

    buttonCreator.setMarbleGridButtonStyle(source);
    buttonCreator.setMarbleGridButtonStyle(target);

    if (source.getGraphic() != null) {
      source.getGraphic().setOpacity(1);
    }

  }

  public void setActionForMarbleGridButton(Button button, MarbleState marbleState,
      int column, int row) {
    button.setOnAction(actionEvent -> {
      MarbleStateAction marbleStateAction = marbleState.getMarbleStateAction(column, row);
      if (marbleStateAction != null) {
        ft.stop();
        if (button.getGraphic() != null) {
          button.getGraphic().setOpacity(1);
          ft.setNode(button.getGraphic());
          ft.setFromValue(0.0);
          ft.setToValue(1.0);
          ft.setCycleCount(Timeline.INDEFINITE);
        }

        if (marbleStateAction.isSourceMarbleSet() && marbleStateAction.isTargetMarbleSet()) {
          ft.stop();
          repaint(marbleState, marbleStateAction);
        } else if (marbleStateAction.isSourceMarbleSet() && !marbleStateAction
            .isTargetMarbleSet()) {
          ft.play();
        } else {
          ft.stop();
        }
      }
    });
  }
}
