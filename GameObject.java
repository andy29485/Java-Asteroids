// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class GameObject {
  private double  x;
  private double  y;
  private double  dx;
  private double  dy;
  private double  angle;
  private Shape[] shapes;

  public GameObject() {
    this(0, 0, 0, 0, "polygon");
  }

  public GameObject(double x, double y, double dx, double dy, String shape) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.angle = 0;
    this.shapes = new Shape[4];

    for(int i = 0; i < this.shapes.length; i++) {
      if(shape.equalsIgnoreCase("polygon"))
        this.shapes[i] = new Polygon();
      if(shape.equalsIgnoreCase("circle"))
        this.shapes[i] = new Circle(1.5);
      else
        this.shapes[i] = new Polygon();
      this.shapes[i].setStroke(Color.WHITE);
      this.shapes[i].setFill(Color.TRANSPARENT);
    }
  }

  public void add(Pane p) {
    for(Shape shape : this.shapes) {
      p.getChildren().add(shape);
    }
  }

  public void remove(Pane p) {
    for(Shape shape : this.shapes) {
      p.getChildren().remove(shape);
    }
  }

  public void setAngle(double angle) {
    this.angle = angle;

    while(this.angle > 360)
      this.angle -= 360;
    while(this.angle < 0)
      this.angle += 360;
  }

  public double getAngle() {
    return this.angle * Math.PI / 180;
  }

  public double getAngleDeg() {
    return this.angle;
  }

  public void move() {
    double frameX = AstroidsDriver.SIZE_X;
    double frameY = AstroidsDriver.SIZE_Y;

    this.x += this.dx;
    this.y += this.dy;

    if(this.x < 0) {
      this.x += frameX;
    }
    else if(this.x > frameX) {
      this.x -= frameX;
    }

    if(this.y < 0) {
      this.y += frameY;
    }
    else if(this.y > frameY) {
      this.y -= frameY;
    }
    this.shapes[0].setRotate(this.angle);
    this.shapes[1].setRotate(this.angle);
    this.shapes[2].setRotate(this.angle);
    this.shapes[3].setRotate(this.angle);

    this.shapes[0].setLayoutX(this.x);
    this.shapes[0].setLayoutY(this.y);

    if(this.x > frameX / 2) {
      this.shapes[1].setLayoutX(this.x - frameX);
      this.shapes[2].setLayoutX(this.x);
      this.shapes[3].setLayoutX(this.x - frameX);
    }
    else {
      this.shapes[1].setLayoutX(this.x + frameX);
      this.shapes[2].setLayoutX(this.x);
      this.shapes[3].setLayoutX(this.x + frameX);
    }

    if(this.y > frameY / 2) {
      this.shapes[1].setLayoutY(this.y);
      this.shapes[2].setLayoutY(this.y - frameY);
      this.shapes[3].setLayoutY(this.y - frameY);
    }
    else {
      this.shapes[1].setLayoutY(this.y);
      this.shapes[2].setLayoutY(this.y + frameY);
      this.shapes[3].setLayoutY(this.y + frameY);
    }
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getX() {
    return this.x;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getY() {
    return this.y;
  }

  public void setdy(double dy) {
    this.dy = dy;
  }

  public double getdy() {
    return this.dy;
  }

  public void setdx(double dx) {
    this.dx = dx;
  }

  public double getdx() {
    return this.dx;
  }

  public Shape[] getShapes() {
    return this.shapes;
  }

  public boolean checkCollision(GameObject obj) {
    for(int i = 0; i < obj.getShapes().length; i++) {
      if(dist(this.shapes[0], obj.getShapes()[i]) < 100 &&
         !Shape.intersect(this.shapes[0], obj.getShapes()[i]).getLayoutBounds()
             .isEmpty())
        return true;
    }
    return false;
  }

  public static double dist(Shape a, Shape b) {
    return Math.sqrt(
        ((a.getLayoutX() - b.getLayoutX()) *
         (a.getLayoutX() - b.getLayoutX())) +
                     ((a.getLayoutY() - b.getLayoutY()) *
                      (a.getLayoutY() - b.getLayoutY())));
  }
}
