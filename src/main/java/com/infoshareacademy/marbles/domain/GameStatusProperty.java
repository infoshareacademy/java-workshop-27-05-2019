package com.infoshareacademy.marbles.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameStatusProperty {

  private SimpleBooleanProperty started = new SimpleBooleanProperty(this, "startedValue");
  private SimpleBooleanProperty enabled = new SimpleBooleanProperty(this, "enabledValue");

  public GameStatusProperty(Boolean isStarted, Boolean isEnabled) {
    this.started.set(isStarted);
    this.enabled.set(isEnabled);
  }

  public Boolean getStartedValue() {
    return started.get();
  }

  public Boolean getEnabledValue() {
    return enabled.get();
  }

  public void setStartedValue(Boolean isStarted) {
    this.started.set(isStarted);
    this.enabled.set(isStarted);
  }

  public void setEnabledValue(Boolean isEnabled) {
    this.enabled.set(isEnabled);
  }

  public BooleanProperty startedValueProperty() {
    return started;
  }

  public BooleanProperty enabledValueProperty() {
    return enabled;
  }
}
