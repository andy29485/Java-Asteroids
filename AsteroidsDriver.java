// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AsteroidsDriver extends Application {
  public static final int   SIZE_X = 700; // the size of the game window
  public static final int   SIZE_Y = 700;

  private static Stage      mainStage;    // the stage

  private static Scene      m_scene;      // menu scene
  private static Scene      g_scene;      // game scene
  private static Scene      s_scene;      // score scene

  private static MenuPane   m_pane;       // menu pane
  private static GamePane   g_pane;       // game pane
  private static ScoresPane s_pane;       // score pane

  /**
   * set up the window and create all the panes
   */
  @Override
  public void start(Stage stage) {
    stage.setTitle("Astroids");
    mainStage = stage;

    PaneSwitcher ps = new PaneSwitcher();// create object that will allow the
                                         // switching of panes

    // create panes and scenes for each pane
    m_pane = new MenuPane(ps);
    g_pane = new GamePane(ps);
    s_pane = new ScoresPane(ps);

    m_scene = new Scene(m_pane, SIZE_X, SIZE_Y);
    g_scene = new Scene(g_pane, SIZE_X, SIZE_Y);
    s_scene = new Scene(s_pane, SIZE_X, SIZE_Y);

    // attempt to load css from jar for the menu pane
    try {
      String css = AsteroidsDriver.class.getResource("/final_project/menu.css")
          .toExternalForm();
      m_scene.getStylesheets().clear();
      m_scene.getStylesheets().add(css);
    }
    catch(RuntimeException e) {
      System.out.println("could not load menu.css");
    }

    // attempt to load css from jar for the score pane
    try {
      String css = AsteroidsDriver.class.getResource("/final_project/score.css")
          .toExternalForm();
      s_scene.getStylesheets().clear();
      s_scene.getStylesheets().add(css);
    }
    catch(RuntimeException e) {
      System.out.println("could not load score.css");
    }

    // allow player interactions when in game
    g_scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        g_pane.onKeyPress(event);
      }
    });
    g_scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        g_pane.onKeyRelease(event);
      }
    });

    // start off by going to the main menu
    ps.switchTo("menu");

    stage.show();
  }

  /**
   * just in case(also it seems jars need a main method)
   *
   * @param args
   *          the command line args passed to javafx
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * class that allows switching on panes given a string
   *
   * @author Andriy Zasypkin
   *
   */
  static class PaneSwitcher implements Switcher {
    @Override
    public void switchTo(String pane) {
      if(pane.equalsIgnoreCase("menu")) {
        mainStage.setScene(m_scene);
      }
      else if(pane.equalsIgnoreCase("game")) {
        g_pane.reset();
        mainStage.setScene(g_scene);
      }
      else if(pane.equalsIgnoreCase("scores")) {
        s_pane.reset();
        mainStage.setScene(s_scene);
      }
    }
  }
}
