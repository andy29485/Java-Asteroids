// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Player extends GameObject {
  private List<Bullet> bullets; // bullets associated with the ship

  /**
   * create a player at the starting position
   */
  public Player() {
    super();
    this.bullets = new ArrayList<Bullet>();
    for(Shape tmp : this.getShapes()) { // set shape of ship to triangle like
                                        // thing
      ((Polygon) tmp).getPoints()
          .addAll(new Double[] {0.0, 0.0, 20.0, 5.0, 0.0, 10.0, 6.0, 5.0});
    }
    this.setX(AsteroidsDriver.SIZE_X / 2);// set location to centre of pane
    this.setY(AsteroidsDriver.SIZE_Y / 2);
  }

  /**
   * shoot a bullet
   *
   * Add a bullet to the list of bullets and to the pane in front of the
   * player's bow.
   *
   * @param p
   *          panel that the bullet will be put on
   */
  public void shoot(Pane p) {
    // shoot a bullet, see rotational matrix for bullet positioning
    Bullet b = new Bullet(this.getX() + Math.cos(this.getAngle()) * 10 + 10,
        this.getY() + Math.sin(this.getAngle()) * 10 + 5, this.getAngle());
    b.add(p);// add bullet to pane
    bullets.add(b);// add bullet to list of bullets
  }

  /**
   * @return list of bullets associated with the player
   */
  public List<Bullet> getBullets() {
    return bullets;
  }

  /**
   * put player back into the starting position
   */
  public void reset() {
    this.setX(AsteroidsDriver.SIZE_X / 2);
    this.setY(AsteroidsDriver.SIZE_Y / 2);
    this.setdx(0);
    this.setdy(0);
    this.setAngle(0);
  }

  /**
   * move all the bullets accosted with the player
   *
   * bullets will be removed if their time is up
   * 
   * @param p
   *          pane on which bullets exist(needed for removal)
   */
  public void moveBullets(Pane p) {// move all the bullets as needed
    for(int i = 0; i < bullets.size(); i++) {
      Bullet b = bullets.get(i);
      if(b.isDone()) { // if they have been flying for too long, they disappear
        b.remove(p);
        this.bullets.remove(b);
        i--;
      }
      else
        b.move();
    }
  }
}
