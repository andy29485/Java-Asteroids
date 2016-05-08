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

  /**
   * create a polygon that does not move by default
   *
   */
  public GameObject() {
    this(0, 0, 0, 0, "polygon");
  }

  /**
   *
   * @param x
   *          horizontal position of the game object
   * @param y
   *          vertical position of the game object
   * @param dx
   *          horizontal displacement of game object per cycle
   * @param dy
   *          vertical displacement of game object per cycle
   * @param shape
   *          type of shape that the object is(circle or polygon)
   */
  public GameObject(double x, double y, double dx, double dy, String shape) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.angle = 0;
    this.shapes = new Shape[4];// 4 shapes to allow wrap around(min needed)

    for(int i = 0; i < this.shapes.length; i++) {
      if(shape.equalsIgnoreCase("polygon"))
        this.shapes[i] = new Polygon();
      if(shape.equalsIgnoreCase("circle"))// for bullets
        this.shapes[i] = new Circle(1.5);
      else
        this.shapes[i] = new Polygon();
      this.shapes[i].setStroke(Color.WHITE);// this is asteroids this is the
                                            // colour used in the game
      this.shapes[i].setFill(Color.TRANSPARENT);// only want an outline
    }
  }

  /**
   * add all 4 of the shapes to the pane
   *
   * @param p
   *          javafx pane to add object to
   */
  public void add(Pane p) {
    for(Shape shape : this.shapes) {
      p.getChildren().add(shape);
    }
  }

  /**
   * remove all 4 of the shapes from the pane
   *
   * @param p
   *          javafx pane to add object to
   */
  public void remove(Pane p) {
    for(Shape shape : this.shapes) {
      p.getChildren().remove(shape);
    }
  }

  /**
   * rotate shapes
   *
   * @param angle
   *          in degrees
   */
  public void setAngle(double angle) {
    this.angle = angle;

    while(this.angle > 360)
      this.angle -= 360;
    while(this.angle < 0)
      this.angle += 360;
  }

  /**
   * @return rotation angle as radians
   */
  public double getAngle() {
    return this.angle * Math.PI / 180;
  }

  /**
   * @return rotation angle as degrees
   */
  public double getAngleDeg() {
    return this.angle;
  }

  /**
   * moves all 4 objects - wraps around if needed
   */
  public void move() {
    double frameX = AsteroidsDriver.SIZE_X;
    double frameY = AsteroidsDriver.SIZE_Y;

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

    // magic used to determine in which quadrant the object is so that only 4
    // shapes were needed(as opposed to 9)
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

  /**
   * change horizontal position of Game Object
   *
   * @param x
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   *
   * @return horizontal position of Game Object
   */
  public double getX() {
    return this.x;
  }

  /**
   * change vertical position of Game Object
   *
   * @param y
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   *
   * @return vertical position of Game Object
   */
  public double getY() {
    return this.y;
  }

  /**
   * modify the change in vertical positions per cycle
   *
   * @param dx
   */
  public void setdy(double dy) {
    this.dy = dy;
  }

  /**
   * @return change in vertical positions per cycle
   */
  public double getdy() {
    return this.dy;
  }

  /**
   * modify the change in horizontal positions per cycle
   *
   * @param dx
   */
  public void setdx(double dx) {
    this.dx = dx;
  }

  /**
   * @return change in horizontal positions per cycle
   */
  public double getdx() {
    return this.dx;
  }

  /**
   * Used to modify aspects of the GameObj shapes
   *
   * @return the four shape obj that represent this GameObject
   */
  public Shape[] getShapes() {
    return this.shapes;
  }

  /**
   * @param obj
   * @return true if this object and obj are intersecting
   */
  public boolean checkCollision(GameObject obj) {
    for(int i = 0; i < obj.getShapes().length; i++) {
      if(dist(this.shapes[0], obj.getShapes()[i]) < 85 &&
         !Shape.intersect(this.shapes[0], obj.getShapes()[i]).getLayoutBounds()
             .isEmpty())
        return true;
    }
    return false;
  }

  /**
   * @param a
   * @param b
   * @return the distance between game objects a and b
   */
  public static double dist(Shape a, Shape b) {
    return Math.sqrt(
        ((a.getLayoutX() - b.getLayoutX()) *
         (a.getLayoutX() - b.getLayoutX())) +
                     ((a.getLayoutY() - b.getLayoutY()) *
                      (a.getLayoutY() - b.getLayoutY())));
  }
}
