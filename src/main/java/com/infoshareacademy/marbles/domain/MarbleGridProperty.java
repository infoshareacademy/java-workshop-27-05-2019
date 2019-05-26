package com.infoshareacademy.marbles.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MarbleGridProperty {

  private SimpleIntegerProperty[][] value;

  public MarbleGridProperty(int cols, int rows) {
    value = new SimpleIntegerProperty[cols][rows];
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        value[i][j] = new SimpleIntegerProperty(this, "value" + i + j);
        setValue(0, i, j);
      }
    }
  }

  public Integer getValue(int col, int row) {
    return value[col][row].get();
  }

  public void setValue(Integer value, int col, int row) {
    this.value[col][row].set(value);
  }

  public IntegerProperty valueProperty(int col, int row) {
    return value[col][row];
  }
}
