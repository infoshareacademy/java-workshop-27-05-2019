package com.infoshareacademy.marbles.game;

import com.infoshareacademy.marbles.domain.GameStatusProperty;
import com.infoshareacademy.marbles.domain.Marble;
import com.infoshareacademy.marbles.domain.MarbleGridProperty;
import com.infoshareacademy.marbles.domain.MarbleStateAction;
import com.infoshareacademy.marbles.domain.ScoreProperty;
import com.infoshareacademy.marbles.domain.TimeProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarbleState {

  private static final int MARBLES_WIN_COUNT = 5;
  private static final int APPENDED_MARBLES_COUNT = 3;
  private static final int INITIAL_MARBLES_COUNT = 3;
  private static final int MARBLES_TYPES_COUNT = 4;
  public static final int GRID_FIELD_WIDTH = 9;
  public static final int GRID_FIELD_HEIGHT = 9;

  private MarbleGridProperty marbleGrid = new MarbleGridProperty(
      GRID_FIELD_WIDTH, GRID_FIELD_HEIGHT);

  private boolean isWinStatus = false;
  private boolean isEndOfGame = false;
  private ScoreProperty scoreProperty = new ScoreProperty(0);
  private GameStatusProperty gameStatusProperty = new GameStatusProperty(false, false);
  private TimeProperty timeProperty = new TimeProperty(0L);

  private MarbleStateAction marbleStateAction = new MarbleStateAction();

  public MarbleState() {
    for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
      for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
        marbleGrid.setValue(0, column, row);
      }
    }
  }

  public void resetGame(TimeProperty timeProperty) {
    initMarbleGrid();
    scoreProperty.setValue(0);
    timeProperty.setValue(0L);
  }

  public void initMarbleGrid() {

    List<Marble> marbles = randMarbles(INITIAL_MARBLES_COUNT, false);

    for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
      for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {

        marbleGrid.setValue(0, column, row);

        Marble marble = getFitMarble(marbles, column, row);
        if (marble != null) {
          marbleGrid.setValue(marble.getNumber(), column, row);
        }
      }
    }
  }

  private void appendNewMarblesToGrid() {
    List<Marble> marbles = randMarbles(APPENDED_MARBLES_COUNT, true);
    marbles.forEach(
        marble -> marbleGrid.setValue(marble.getNumber(), marble.getColumn(), marble.getRow()));
  }

  public MarbleStateAction getMarbleStateAction(int column, int row) {
    if (!gameStatusProperty.getEnabledValue()) {
      return null;
    }

    if (!marbleStateAction.isSourceMarbleSet() && marbleGrid.getValue(column, row) != 0) {
      marbleStateAction.setSourceMarble(column, row);
    } else if (marbleStateAction.isCurrentSourceMarbleSet(column, row)) {
      marbleStateAction.resetMarbleAction();
    } else if (marbleStateAction.isSourceMarbleSet() && !marbleStateAction.isTargetMarbleSet()
        && marbleGrid.getValue(column, row) == 0) {
      marbleStateAction.setTargetMarble(column, row);
    } else {
      return null;
    }
    return marbleStateAction;
  }

  public void changeMarbleGrid() {
    if (marbleStateAction.isSourceMarbleSet() && marbleStateAction.isTargetMarbleSet()) {
      int sourceColumn = marbleStateAction.getSourceMarble().getKey();
      int sourceRow = marbleStateAction.getSourceMarble().getValue();
      int targetColumn = marbleStateAction.getTargetMarble().getKey();
      int targetRow = marbleStateAction.getTargetMarble().getValue();

      if (marbleGrid.getValue(targetColumn, targetRow) == 0
          && marbleGrid.getValue(sourceColumn, sourceRow) != 0) {

        // Move the marble to the target field.
        marbleGrid.setValue(marbleGrid.getValue(sourceColumn, sourceRow), targetColumn, targetRow);

        // Clear the source field.
        marbleGrid.setValue(0, sourceColumn, sourceRow);

        // Check if there are marbles of the same color in row.
        validateWin();

        // If there was no row, user gets no points and new random marbles arrive.
        while (!isWinStatus) {
          appendNewMarblesToGrid();
          validateWin();

          if (!isWinStatus) {
            break;
          }
        }

        return;
      }

      // Reset the marble state action (selection is unset to).
      marbleStateAction.resetMarbleAction();
    }
  }

  private void validateWin() {
    List<Marble> marbles = new ArrayList<>();
    isWinStatus = false;

    int value = marbleGrid.getValue(0, 0);

    for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
      for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
        findWinMarbles(column, row, value, marbles);
        value = marbleGrid.getValue(column, row);
      }

      if (marbles.size() >= MARBLES_WIN_COUNT) {
        calculateScore(marbles);
      }
    }

    marbles.clear();
    value = marbleGrid.getValue(0, 0);
    for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
      for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
        findWinMarbles(column, row, value, marbles);
        value = marbleGrid.getValue(column, row);
      }

      if (marbles.size() >= MARBLES_WIN_COUNT) {
        calculateScore(marbles);
      }
    }

    marbles.clear();
    value = marbleGrid.getValue(0, 0);
    for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
      for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
        for (int tempColumn = column, tempRow = row;
            tempColumn < GRID_FIELD_WIDTH && tempRow < GRID_FIELD_HEIGHT;
            tempColumn++, tempRow++) {
          findWinMarbles(tempColumn, tempRow, value, marbles);
          value = marbleGrid.getValue(tempColumn, tempRow);
        }

        if (marbles.size() >= MARBLES_WIN_COUNT) {
          calculateScore(marbles);
        }
      }
    }

    marbles.clear();
    value = marbleGrid.getValue(0, 0);
    for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
      for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
        for (int tempColumn = column, tempRow = row;
            tempColumn >= 0 && tempRow < GRID_FIELD_HEIGHT;
            tempColumn--, tempRow++) {
          findWinMarbles(tempColumn, tempRow, value, marbles);
          value = marbleGrid.getValue(tempColumn, tempRow);
        }

        if (marbles.size() >= MARBLES_WIN_COUNT) {
          calculateScore(marbles);
        }
      }
    }
  }

  private void calculateScore(List<Marble> marbles) {
    int partialScore = marbles.size();
    if (partialScore > 0) {
      isWinStatus = true;
      scoreProperty.addValue(partialScore);
    }
    marbles.forEach(marble -> marbleGrid.setValue(0, marble.getColumn(), marble.getRow()));
    marbles.clear();
  }

  private void findWinMarbles(int column, int row, int value, List<Marble> marbles) {

    if (value != marbleGrid.getValue(column, row) || value == 0) {
      if (marbles.size() >= MARBLES_WIN_COUNT) {
        calculateScore(marbles);
      }
      marbles.clear();
    }
    marbles.add(new Marble(column, row, value));
  }

  private Marble getFitMarble(List<Marble> marbles, int column, int row) {
    for (Marble marble : marbles) {
      if (marble.getColumn() == column && marble.getRow() == row) {
        return marble;
      }
    }
    return null;
  }

  private List<Marble> randMarbles(int count, boolean isAppend) {
    List<Marble> marbles = new ArrayList<>();
    if (isAppend) {
      collectAllMarbles(marbles);
    }

    for (int i = 0; i < count; i++) {
      Marble marble = randMarble();
      while (getFitMarble(marbles, marble.getColumn(), marble.getRow()) != null) {
        marble = randMarble();
      }
      marbles.add(marble);

      // The user has lost if there is no place left for new marbles.
      if (marbles.size() == GRID_FIELD_HEIGHT * GRID_FIELD_WIDTH) {
        isEndOfGame = true;
        gameStatusProperty.setStartedValue(false);
        break;
      }
    }
    return marbles;
  }

  private void collectAllMarbles(List<Marble> marbles) {
    for (int column = 0; column < GRID_FIELD_WIDTH; column++) {
      for (int row = 0; row < GRID_FIELD_HEIGHT; row++) {
        if (marbleGrid.getValue(column, row) != 0) {
          marbles.add(new Marble(column, row, marbleGrid.getValue(column, row)));
        }
      }
    }
  }

  private Marble randMarble() {

    Random random = new Random();
    return new Marble(Math.abs(random.nextInt()) % GRID_FIELD_WIDTH,
        Math.abs(random.nextInt()) % GRID_FIELD_HEIGHT,
        (Math.abs(random.nextInt()) % MARBLES_TYPES_COUNT) + 1);
  }

  public MarbleGridProperty getMarbleGrid() {
    return marbleGrid;
  }

  public ScoreProperty getScoreProperty() {
    return scoreProperty;
  }

  public GameStatusProperty getGameStatusProperty() {
    return gameStatusProperty;
  }

  public boolean isEndOfGame() {
    return isEndOfGame;
  }

  public TimeProperty getTimeProperty() {
    return timeProperty;
  }
}
