// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AstroidsDriver extends Application {
  public static final int   SIZE_X = 700;
  public static final int   SIZE_Y = 700;

  private static Stage      mainStage;

  private static Scene      m_scene;
  private static Scene      g_scene;
  private static Scene      s_scene;

  private static MenuPane   m_pane;
  private static GamePane   g_pane;
  private static ScoresPane s_pane;

  @Override
  public void start(Stage stage) {
    stage.setTitle("Astroids");
    mainStage = stage;

    PaneSwitcher ps = new PaneSwitcher();

    m_pane = new MenuPane(ps);
    g_pane = new GamePane(ps);
    s_pane = new ScoresPane(ps);

    m_scene = new Scene(m_pane, SIZE_X, SIZE_Y);
    g_scene = new Scene(g_pane, SIZE_X, SIZE_Y);
    s_scene = new Scene(s_pane, SIZE_X, SIZE_Y);

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

    ps.switchTo("menu");

    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

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
