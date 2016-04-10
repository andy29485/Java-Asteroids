// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Astroid extends GameObject {
  private int    size;
  private double dangle;

  public Astroid() {
    this(5);
  }

  public Astroid(int size) {
    this(Math.random() * (AstroidsDriver.SIZE_X - 50) + 10,
        Math.random() * (AstroidsDriver.SIZE_Y - 50) + 10, size);
  }

  public Astroid(double x, double y, int size) {
    super(x, y, Math.random() * 3 - 1.5, Math.random() * 3 - 1.5, "polygon");

    this.size = size;
    this.dangle = Math.random() * 4 - 2;

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

  public Astroid[] destroy() {
    if(this.size < 3)
      return new Astroid[0];

    Astroid[] debris = new Astroid[2];
    debris[0] = new Astroid(this.getX(), this.getY(), this.size - 1);
    debris[1] = new Astroid(this.getX(), this.getY(), this.size - 1);

    return debris;
  }

  public int getScore() {
    return this.size * 10;
  }

  @Override
  public void move() {
    super.move();
    this.setAngle(this.getAngleDeg() + this.dangle);
  }
}
