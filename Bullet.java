// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

public class Bullet extends GameObject {
  public static final int BULLET_LIFE = 50;
  public static final int LIFE_DRAIN  = 1;
  private int             lifeRemaining;

  public Bullet(double x, double y, double angle) {
    super(x, y, Math.cos(angle) * 14, Math.sin(angle) * 14, "circle");
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
