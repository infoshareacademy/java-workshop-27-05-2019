package com.infoshareacademy.marbles.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreProperty {

  private SimpleIntegerProperty value = new SimpleIntegerProperty(this, "value");

  public ScoreProperty(Integer value) {
    this.value.set(value);
  }

  public Integer getValue() {
    return value.get();
  }

  public void setValue(Integer value) {
    this.value.set(value);
  }

  public void addValue(Integer value) {
    setValue(getValue() + value);
  }

  public IntegerProperty valueProperty() {
    return value;
  }
}
