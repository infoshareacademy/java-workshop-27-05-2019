package com.infoshareacademy.marbles.domain;

public class Marble {

  private int column;
  private int row;
  private int number;

  public Marble(int column, int row, int number) {
    this.column = column;
    this.row = row;
    this.number = number;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
