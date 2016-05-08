// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Asteroid extends GameObject {
  private int    size;
  private double dangle;

  /**
   * create an asteroid of size 5
   */
  public Asteroid() {
    this(5);
  }

  /**
   * create asteroid in a random location in pane
   *
   * @param size
   *          the size of the asteroid to create
   */
  public Asteroid(int size) { // create asteroid in a random location in pane
    this(Math.random() * (AsteroidsDriver.SIZE_X - 50) + 10,
        Math.random() * (AsteroidsDriver.SIZE_Y - 50) + 10, size);
  }

  /**
   * constructor that actually does the work
   *
   * @param x
   *          position x
   * @param y
   *          position y
   * @param size
   *          size the asteroid takes on [2, 5]
   */
  public Asteroid(double x, double y, int size) {
    // create a slow moving polygon game object
    super(x, y, Math.random() * 3 - 1.5, Math.random() * 3 - 1.5, "polygon");

    // set size and spin
    this.size = size;
    this.dangle = Math.random() * 4 - 2;

    // make asteroid look rugged (using randomness)
    Double[] points = new Double[(int) (Math.random() * 5 + this.size + 7)];
    for(int i = 0; i < points.length / 2; i++) {
      double angle = i * Math.PI * 4.0 / points.length;
      points[i * 2] =
          Math.cos(angle) * (Math.random() * 5 + 3) * this.size + 30;
      points[i * 2 + 1] =
          Math.sin(angle) * (Math.random() * 5 + 3) * this.size + 30;
    }
    for(Shape tmp : this.getShapes())
      ((Polygon) tmp).getPoints().addAll(points);
  }

  /**
   * create 2 new asteroids when destroyed that were formed from the debris of
   * this asteroids
   *
   *
   * @return 2 new smaller asteroids or null of this asteroid was too small to
   *         leave debris
   */
  public Asteroid[] destroy() {
    if(this.size < 3) // unless it's a small asteroid that leaves no debris
      return new Asteroid[0];

    // create 2 new smaller asteroids in the same location
    Asteroid[] debris = new Asteroid[2];
    debris[0] = new Asteroid(this.getX(), this.getY(), this.size - 1);
    debris[1] = new Asteroid(this.getX(), this.getY(), this.size - 1);

    return debris;
  }

  /**
   * @return the point value of the asteroid
   */
  public int getScore() {
    return this.size * 10;
  }

  /**
   * move and spin asteroid
   */
  @Override
  public void move() {
    super.move();
    this.setAngle(this.getAngleDeg() + this.dangle);
  }
}
