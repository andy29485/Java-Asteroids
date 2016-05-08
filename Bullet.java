// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

public class Bullet extends GameObject {
  public static final double BULLET_SPEED = 7;
  public static final int    BULLET_LIFE  =
      (AsteroidsDriver.SIZE_X + AsteroidsDriver.SIZE_Y) / (int) BULLET_SPEED;
  public static final int    LIFE_DRAIN   = 1;
  private int                lifeRemaining;

  /**
   * create a new bullet
   *
   * @param x
   *          x position of bullet
   * @param y
   *          y position of bullet
   * @param angle
   *          in radians, used to calculate velocity of the bullet
   */
  public Bullet(double x, double y, double angle) {
    super(x, y, Math.cos(angle) * BULLET_SPEED, Math.sin(angle) * BULLET_SPEED,
        "circle");
    this.lifeRemaining = BULLET_LIFE;
  }

  /**
   * move bullet and reduce its life
   */
  @Override
  public void move() {
    super.move();
    this.lifeRemaining -= LIFE_DRAIN;
  }

  /**
   * check if bullet should be gone
   *
   * @return true if bullet should be removed from the game
   */
  public boolean isDone() {
    return this.lifeRemaining <= 0;
  }
}
