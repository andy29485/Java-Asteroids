// Name: Andriy Zasypkin
// Date: 2016-04-03
// Final Project

package final_project;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ScoresPane extends GridPane {
  public static final String SCORE_FILE = "scores.db";// raf file with scores
  public Switcher            s;                       // pane switcher obj

  /**
   * create a grid pane with padding between the elements - for the scores
   *
   * @param s
   */
  public ScoresPane(Switcher s) {
    super();

    this.s = s;
    this.setPadding(new Insets(40, 40, 40, 40));

    this.setHgap(30);
    this.setVgap(10);
  }

  /**
   * reload the score pane
   */
  public void reset() {
    this.getChildren().clear();

    // column headers
    this.add(new Label("Top Scores"), 0, 0);
    this.add(new Label("Name"), 0, 1);
    this.add(new Label("Score"), 1, 1);
    this.add(new Label("Date"), 2, 1);

    // load the scores
    List<Score> scores = load();
    int i = 1;

    // for each score
    for(Score score : scores) {
      i++;
      if(score.name.length() > 0) // if there is a name - put it there
        this.add(new Label(score.name), 0, i);
      else {// otherwise ask for a name
        TextField tmpf = new TextField();
        tmpf.setPromptText("Enter Name");
        this.add(tmpf, 0, i);

        int j = i;

        // button to save name
        Button tmpb = new Button();
        tmpb.setText("Save");
        tmpb.setOnAction(e -> {
          score.name = tmpf.getText();
          if(score.name.length() > 0) {
            this.getChildren().remove(tmpb);
            this.getChildren().remove(tmpf);
            this.add(new Label(score.name), 0, j);
            save(scores);
          }
        });
        this.add(tmpb, 3, i);
      }
      // add the score and date label in
      this.add(new Label(String.format("%,d", score.score)), 1, i);
      this.add(new Label(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          .format(new Date(score.date))), 2, i);
    }

    // add navigation buttons
    Button exit = new Button();
    exit.setText("Exit");
    exit.setOnAction(e -> System.exit(0));
    this.add(exit, 1, i + 2);

    Button play = new Button();
    play.setText("Play Again");
    play.setOnAction(e -> s.switchTo("game"));
    this.add(play, 2, i + 2);

    Button menu = new Button();
    menu.setText("Main Menu");
    menu.setOnAction(e -> s.switchTo("menu"));
    this.add(menu, 3, i + 2);

  }

  /**
   * save a score into the file - if deemed worthy
   *
   * @param score
   *          the point value of the score that is to be evaluated and possibly
   *          saved
   */
  public static void save(long score) {
    List<Score> scores = load();// load current scores

    // check if worthy
    if(scores.get(scores.size() - 1).score < score || scores.size() < 10) {
      String name = "";// name will be given by user later
      long date = System.currentTimeMillis();

      int i = scores.size();
      // find place to insert new score
      while(--i >= 0 && scores.get(i).score < score)
        ;

      Score tmp = new Score();
      tmp.name = name;
      tmp.date = date;
      tmp.score = score;

      // insert new score
      scores.add(i + 1, tmp);

      // save scores to files
      save(scores);
    }
  }

  /**
   * save scores to file
   *
   * @param scores
   *          list of score objects containing names, dates and score values
   */
  public static void save(List<Score> scores) {
    try {
      // open file and save each score into it
      RandomAccessFile raf = new RandomAccessFile(SCORE_FILE, "rw");
      for(int i = 0; i < (scores.size() < 10 ? scores.size() : 10); i++) {
        raf.writeLong(scores.get(i).score);
        raf.writeLong(scores.get(i).date);
        raf.writeUTF(scores.get(i).name);
      }
      raf.close();
    }
    catch(Exception e) {}
  }

  /**
   * load scores from file, or generate default
   *
   * @return scores as a list of Score objects
   */
  public static List<Score> load() {
    List<Score> scores = new ArrayList<Score>();
    java.io.File f = new java.io.File(SCORE_FILE);

    // make sure that the file with the scores exists and is a file
    if(f.exists() && !f.isDirectory()) {
      try {
        RandomAccessFile raf = new RandomAccessFile(SCORE_FILE, "r");
        while(raf.getFilePointer() < raf.length()) {
          Score tmp = new Score();
          tmp.score = raf.readLong();
          tmp.date = raf.readLong();
          tmp.name = raf.readUTF();
          scores.add(tmp);
        }
        raf.close();
      }
      catch(Exception e) {}
    }
    else {// return default list if file !exist

      for(int i = 10; i > 0; i--) {
        Score tmp = new Score();
        tmp.name = "AZ";
        tmp.score = i * 1000;
        tmp.date = System.currentTimeMillis();
        scores.add(tmp);
      }
      save(scores);// save (default)scores
    }
    return scores;
  }

  public static class Score { // basically just a struct for a score
    public String name;
    public long   score;
    public long   date;
  }
}
