package com.infoshareacademy.marbles;

import com.infoshareacademy.marbles.game.MarbleState;
import com.infoshareacademy.marbles.layout.LeftPanel;
import com.infoshareacademy.marbles.layout.RightPanel;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AppFxLauncher extends Application {

  @Override
  public void start(Stage stage) {

    GridPane parent = new GridPane();
    Group root = new Group();
    Scene scene = new Scene(root, 790, 540);
    scene.getStylesheets().add("style.css");

    MarbleState marbleState = new MarbleState();

    parent.add(new LeftPanel().build(marbleState), 0, 0);
    parent.add(new RightPanel().build(marbleState), 1, 0);
    parent.setId("main");
    root.getChildren().add(parent);

    stage.setTitle("Marbles Game 1.0");
    stage.getIcons().add(new Image(
        getClass().getClassLoader()
            .getResourceAsStream("1.png")));
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  //  Please don't start it directly since FX is not separated module!
  //  Please use com.infoshareacademy.marbles.Main to run app
  static void run(String[] args) {
    launch(args);
  }
}
