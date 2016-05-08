// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MenuPane extends FlowPane {
  /**
   * create pane with 3 buttons that allow: - game to start - open scoreboard -
   * exit applications
   * 
   * @param s
   */
  public MenuPane(Switcher s) {
    super();

    // vertical layout to make menu look nicer
    VBox vb = new VBox();

    // game start button
    Button playButton = new Button();
    playButton.setText("Play");
    playButton.setOnAction(e -> s.switchTo("game"));

    // scoreboard button
    Button scoresButton = new Button();
    scoresButton.setText("Scores");
    scoresButton.setOnAction(e -> s.switchTo("scores"));

    // exit button
    Button exitButton = new Button();
    exitButton.setText("Exit");
    exitButton.setOnAction(e -> System.exit(0));

    // add button to vertical box
    vb.getChildren().add(playButton);
    vb.getChildren().add(scoresButton);
    vb.getChildren().add(exitButton);
    vb.setAlignment(Pos.CENTER);

    // add vertical box to pane
    this.getChildren().add(vb);
    this.setAlignment(Pos.CENTER);
  }
}
