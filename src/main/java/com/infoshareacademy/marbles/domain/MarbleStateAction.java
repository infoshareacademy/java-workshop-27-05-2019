package com.infoshareacademy.marbles.domain;

import javafx.util.Pair;

public class MarbleStateAction {

  // sourceMarble shows us where exactly the selected marble is located.
  private Pair<Integer, Integer> sourceMarble = new Pair<>(-1, -1);

  // targetMarble shows the coordinates of the field the marble would move on.
  private Pair<Integer, Integer> targetMarble = new Pair<>(-1, -1);

  public boolean isSourceMarbleSet() {
    return sourceMarble.getKey() != -1 && sourceMarble.getValue() != -1;
  }

  public boolean isCurrentSourceMarbleSet(int column, int row) {
    return sourceMarble.getKey() == column && sourceMarble.getValue() == row;
  }

  public boolean isTargetMarbleSet() {
    return targetMarble.getKey() != -1 && targetMarble.getValue() != -1;
  }

  private void resetSourceMarble() {
    sourceMarble = new Pair<>(-1, -1);
  }

  public void resetTargetMarble() {
    targetMarble = new Pair<>(-1, -1);
  }

  public void resetMarbleAction() {
    resetSourceMarble();
    resetTargetMarble();
  }

  public void setSourceMarble(int column, int row) {
    sourceMarble = new Pair<>(column, row);
  }

  public void setTargetMarble(int column, int row) {
    targetMarble = new Pair<>(column, row);
  }

  public Pair<Integer, Integer> getSourceMarble() {
    return sourceMarble;
  }

  public Pair<Integer, Integer> getTargetMarble() {
    return targetMarble;
  }
}
