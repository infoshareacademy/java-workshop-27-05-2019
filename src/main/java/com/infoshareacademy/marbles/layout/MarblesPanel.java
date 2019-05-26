package com.infoshareacademy.marbles.layout;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MarblesPanel extends GridPane {

  public static final int BALL_FIELD_WIDTH = 60;
  public static final int BALL_FIELD_HEIGHT = 60;

  protected Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
    ObservableList<Node> children = gridPane.getChildren();
    for (Node node : children) {
      if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
        return node;
      }
    }
    return null;
  }

  protected ImageView getImageViewForButton(int name) {
    Image marbleImage = new Image(
        getClass().getClassLoader()
            .getResourceAsStream(name + ".png"));
    ImageView imageView = new ImageView(marbleImage);
    imageView.setFitWidth(BALL_FIELD_WIDTH - 20);
    imageView.setFitHeight(BALL_FIELD_HEIGHT - 20);
    return imageView;
  }
}
