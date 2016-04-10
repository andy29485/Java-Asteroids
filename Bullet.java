// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

public class Bullet extends GameObject {
  public static final double BULLET_SPEED = 7;
  public static final int    BULLET_LIFE  =
      (AstroidsDriver.SIZE_X + AstroidsDriver.SIZE_Y) / (int) BULLET_SPEED;
  public static final int    LIFE_DRAIN   = 1;
  private int                lifeRemaining;

  public Bullet(double x, double y, double angle) {
    super(x, y, Math.cos(angle) * BULLET_SPEED, Math.sin(angle) * BULLET_SPEED,
        "circle");
    this.lifeRemaining = BULLET_LIFE;
  }

  @Override
  public void move() {
    super.move();
    this.lifeRemaining -= LIFE_DRAIN;
  }

  public boolean isDone() {
    return this.lifeRemaining <= 0;
  }
}
