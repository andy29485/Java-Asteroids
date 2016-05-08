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
  private List<Asteroid> asteroids; // list of asteroid objects on screen
  private AnimationTimer timer;     // timer that runs the game
  private Switcher       s;         // object that allows switching between pane
  private Player         p;         // player ship object
  private int            lives;     // number of lives
  private int            level;     // current level
  private long           score;     // points player has scored
  private long           lastFire;  // time since last shot was fired
  private long           nanoNow;   // current time(used for fire rate)
  private boolean        canFire;   // true if 'reload sequence' was complete
  private Text           info;      // score, lives, and level info
  private double         dm;        // change in player movement
  private double         da;        // change in player rotation

  /**
   * create a new game pane with default values
   *
   * @param s
   *          switcher in for changing panes
   */
  public GamePane(Switcher s) {
    // sets background to black - should be in a css file
    this.setStyle("-fx-background-color: #000000");

    // creates a font to be used with the info text - should be in css file
    Font font = Font.font("Monospaced", 13);

    // Set initial values to variables - creates objects used
    this.lastFire = 0;
    this.nanoNow = 0;
    this.canFire = true;
    this.s = s;
    this.p = new Player();
    this.asteroids = new ArrayList<Asteroid>();
    this.lives = 4;
    this.level = 0;
    this.score = 0;
    this.dm = 0;
    this.da = 0;
    this.info = new Text(AsteroidsDriver.SIZE_X - 300, 22, "");// top left
                                                               // corner

    // should be in a css file, but I don't have much time
    // sets style for the info text
    info.setFont(font);
    info.setStyle("-fx-stroke: #FFFFFF;-fx-fill: #FFFFFF");

    // adds player ship and info text to the pane
    p.add(this);
    this.getChildren().add(info);

    // start the game
    this.timer = new GameTimer();
  }

  /**
   * resets the game
   *
   * opposite of end game. not to be confused with nextLevel
   */
  public void reset() {
    // reset in case player wants to play again
    this.lastFire = 0;
    this.nanoNow = 0;
    this.canFire = true;
    this.lives = 4;
    this.level = 0;
    this.score = 0;
    this.dm = 0;
    this.da = 0;
    this.timer.start();
    while(this.asteroids.size() > 0) {
      this.asteroids.get(0).remove(this);
      this.asteroids.remove(0);
    }
  }

  /**
   * moves the players ship
   *
   * well, not exactly move but rather set the players velocities according to
   * the accelerations determined by the keyboard presses
   */
  public void move() {

    double maxSpeed = 11; // ship should not be able to be infinitely fast
    double dy = p.getdy();
    double dx = p.getdx();

    if(dm != 0) {
      dy += Math.sin(p.getAngle()) * dm;
      dx += Math.cos(p.getAngle()) * dm;

      if(dy > -maxSpeed && dy < maxSpeed)
        p.setdy(dy);
      if(dx > -maxSpeed && dx < maxSpeed)
        p.setdx(dx);
    }
    if(da != 0)
      p.setAngle(p.getAngle() / Math.PI * 180 + da);
    p.move();// this actually moves the ship
  }

  /**
   * 'reload' when no shots are available
   *
   * @param now
   *          current time in nanoseconds
   */
  public void reload(long now) {
    this.nanoNow = now;
    long firerate = 300000000;// number of nanoseconds it takes to 'reload'

    if(!this.canFire && (now > this.lastFire + firerate) ||
       this.p.getBullets().size() == 0) {
      this.canFire = true;
      this.lastFire = now;
    }
  }

  /**
   * all asteroids are cleared, reset stuff and increase the score
   */
  public void nextLevel() {
    this.score += 100 * this.level;// increases score(end level bonus)
    this.level++;// increases level
    this.p.reset();// puts player back in the center

    // remove stray bullets
    while(p.getBullets().size() > 0) {
      p.getBullets().get(0).remove(this);
      p.getBullets().remove(0);
    }

    // make `level+1` asteroids at the start of the level
    for(int i = 0; i <= this.level; i++) {
      Asteroid tmp = new Asteroid();
      this.asteroids.add(tmp);
      tmp.add(GamePane.this);
      while(tmp.checkCollision(p))
        tmp.move();
    }
  }

  /**
   * game has ended routines
   *
   * stop refreshing, check if score needs to be saved, and switch over to the
   * scoreboard
   */
  public void endGame() {
    this.timer.stop();// stop the game loop
    this.info.setText(
        String.format("Lives %d | Level %d | Score: %,d", lives, level, score));
    ScoresPane.save(score);// save the score
    this.reset();// reset the game
    this.s.switchTo("scores");// switch to scoreboard
  }

  /**
   * player crashed, life lost, asteroid destroyed, new ship created in center
   * of map
   */
  public void crash() {
    p.reset();
    lives--;
    for(int j = 0; j < asteroids.size(); j++) {
      Asteroid tmp = asteroids.get(j);
      if(tmp.checkCollision(p)) {
        tmp.remove(GamePane.this);
        asteroids.remove(tmp);
        j--;
      }
    }
  }

  /**
   * set accelerations and shoot should the keys be pressed
   *
   * @param event
   *          the key event that specifies which key was pressed
   */
  public void onKeyPress(KeyEvent event) {
    double moveStep = .3;
    double turnStep = 4.3;

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
    if((event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.Z) &&
       canFire) {
      p.shoot(GamePane.this);
      lastFire = nanoNow;
      canFire = false;
    }
  }

  /**
   * reset accelerations on release
   *
   * @param event
   *          the key event that specifies which key was released
   */
  public void onKeyRelease(KeyEvent event) {
    if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
      dm = 0;
    }
    if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
      da = 0;
    }
  }

  /**
   * timer that manages the game cycles
   */
  private class GameTimer extends AnimationTimer {

    /**
     * what happens at each cycle of the game
     */
    @Override
    public void handle(long now) {
      // complete player movement calculations
      move();

      p.moveBullets(GamePane.this);// move bullets

      // Allows player to shoot again when no bullets remain on the screen,
      // or timer has run out(guns have been 'reloaded')
      reload(now);

      info.setText(String.format("Lives %d | Level %d | Score: %,d", lives,
          level, score));

      // if no asteroids are remaining, the level has ended
      if(asteroids.size() == 0) {
        nextLevel();
      }

      // if no lives are remaining, reset a few things and go to the score
      // screen
      if(lives <= 0) {
        endGame();
      }

      // For each asteroid
      for(int i = 0; i < asteroids.size(); i++) {
        // To save a few get calls, save asteroid in a variable
        Asteroid a = asteroids.get(i);

        a.move();// move asteroid

        // An asteroid has hit the player
        if(a.checkCollision(p)) {
          crash();
        }

        for(int j = 0; j < p.getBullets().size(); j++) {
          Bullet b = p.getBullets().get(j);
          if(a.checkCollision(b)) {// check for bullet collisions
            a.remove(GamePane.this);// remove asteroid
            b.remove(GamePane.this);// remove bullet
            asteroids.remove(a);
            p.getBullets().remove(b);
            i--;
            j--;

            score += a.getScore();// increase score

            for(Asteroid tmp_a : a.destroy()) {// split if needed
              asteroids.add(tmp_a);
              tmp_a.add(GamePane.this);
            }
          }
        }
      }
    }
  }
}
