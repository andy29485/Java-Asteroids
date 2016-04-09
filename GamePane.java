// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GamePane extends Pane {
  private List<Astroid>  astroids;
  private AnimationTimer timer;
  private Switcher       s;
  private Player         p;
  private int            lives;
  private int            level;
  private long           score;
  private long           lastFire;
  private long           nanoNow;
  private boolean        canFire;
  private Text           info;
  private double         dm;
  private double         da;

  public GamePane(Switcher s) {
    super();
    this.setStyle("-fx-background-color: #000000");

    Font font = Font.font("Monospaced", 13);

    this.lastFire = 0;
    this.nanoNow = 0;
    this.canFire = true;
    this.s = s;
    this.p = new Player();
    this.astroids = new ArrayList<Astroid>();
    this.lives = 4;
    this.level = 0;
    this.score = 0;
    this.dm = 0;
    this.da = 0;
    this.info = new Text(AstroidsDriver.SIZE_X - 300, 22, "");// top left corner

    p.add(this);
    this.getChildren().add(info);

    info.setFont(font);
    info.setStyle("-fx-stroke: #FFFFFF");

    this.timer = new GameTimer();
  }

  public void reset() {
    this.lastFire = 0;
    this.nanoNow = 0;
    this.canFire = true;
    this.lives = 4;
    this.level = 0;
    this.score = 0;
    this.dm = 0;
    this.da = 0;
    this.timer.start();
    while(this.astroids.size() > 0) {
      this.astroids.get(0).remove(this);
      this.astroids.remove(0);
    }
  }

  public void move() {
    double maxSpeed = 11;
    double dy = p.getdy();
    double dx = p.getdx();

    if(dm != 0) {
      dy += Math.sin(p.getAngle()) * dm;
      dx += Math.cos(p.getAngle()) * dm;
      if(dy > -1 * maxSpeed && dy < maxSpeed && dx > -1 * maxSpeed &&
         dx < maxSpeed) {
        p.setdy(dy);
        p.setdx(dx);
      }
    }
    if(da != 0)
      p.setAngle(p.getAngle() / Math.PI * 180 + da);
  }

  public void reload(long now) {
    this.nanoNow = now;

    if(!this.canFire && (now > this.lastFire + 300000000) ||
       this.p.getBullets().size() == 0) {
      this.canFire = true;
      this.lastFire = now;
    }
  }

  public void onKeyPress(KeyEvent event) {
    double moveStep = .3;
    double turnStep = 5;
    if(event.getCode() == KeyCode.UP) {
      dm = moveStep;
    }
    if(event.getCode() == KeyCode.DOWN) {
      dm = moveStep * -1;
    }
    if(event.getCode() == KeyCode.LEFT) {
      da = turnStep * -1;
    }
    if(event.getCode() == KeyCode.RIGHT) {
      da = turnStep;
    }
    if(event.getCode() == KeyCode.SPACE && canFire) {
      p.shoot(GamePane.this);
      lastFire = nanoNow;
      canFire = false;
    }
  }

  public void nextLevel() {
    this.score += 100 * this.level;
    this.level++;
    this.p.reset();
    // make `level+1` asteroids at the start of the level
    for(int i = 0; i <= this.level; i++) {
      Astroid tmp = new Astroid();
      this.astroids.add(tmp);
      tmp.add(GamePane.this);
    }
  }

  public void endGame() {
    this.timer.stop();
    this.info.setText(
        String.format("Lives %d | Level %d | Score: %,d", lives, level, score));
    ScoresPane.save(score);
    this.reset();
    this.s.switchTo("scores");
  }

  public void crash() {
    p.reset();
    lives--;
    for(int j = 0; j < astroids.size(); j++) {
      Astroid tmp = astroids.get(j);
      if(tmp.checkCollision(p)) {
        tmp.remove(GamePane.this);
        astroids.remove(tmp);
        j--;
      }
    }
  }

  public void onKeyRelease(KeyEvent event) {
    if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
      dm = 0;
    }
    if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
      da = 0;
    }
  }

  private class GameTimer extends AnimationTimer {
    @Override
    public void handle(long now) {
      // complete player movement calculations
      move();

      // Allows player to shoot again when no bullets remain on the screen,
      // or timer has run out(guns have been 'reloaded')
      reload(now);

      info.setText(String.format("Lives %d | Level %d | Score: %,d", lives,
          level, score));

      // if no asteroids are remaining, the level has ended
      if(astroids.size() == 0) {
        nextLevel();
      }

      // if no lives are remaining, reset a few things and go to the score
      // screen
      if(lives <= 0) {
        endGame();
      }

      // For each asteroid
      for(int i = 0; i < astroids.size(); i++) {
        // To save a few get calls, save asteroid in a variable
        Astroid a = astroids.get(i);

        a.move();// move asteroid

        // An asteroid has hit the player
        if(a.checkCollision(p)) {
          crash();
        }

        for(int j = 0; j < p.getBullets().size(); j++) {
          Bullet b = p.getBullets().get(j);
          if(a.checkCollision(b)) {
            a.remove(GamePane.this);
            b.remove(GamePane.this);
            astroids.remove(a);
            p.getBullets().remove(b);
            i--;
            j--;

            score += a.getScore();

            for(Astroid tmp_a : a.destroy()) {
              astroids.add(tmp_a);
              tmp_a.add(GamePane.this);
            }
          }
        }
      }
      p.move(GamePane.this);
    }
  }
}
