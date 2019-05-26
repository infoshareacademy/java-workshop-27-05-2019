package com.infoshareacademy.marbles.domain;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class TimeProperty {

  private SimpleLongProperty value = new SimpleLongProperty(this, "value");

  public TimeProperty(Long value) {
    this.value.set(value);
  }

  public Long getValue() {
    return value.get();
  }

  public void setValue(Long value) {
    this.value.set(value);
  }

  public void addValue(Long value) {
    setValue(getValue() + value);
  }

  public LongProperty valueProperty() {
    return value;
  }
}
