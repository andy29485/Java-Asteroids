// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

@FunctionalInterface
public interface Switcher {
  /**
   * switches something according to given string
   *
   * @param str
   *          what to switch to
   */
  public void switchTo(String str);
}
