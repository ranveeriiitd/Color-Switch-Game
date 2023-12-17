package Game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class EndScreen {

    @FXML
    Group circle;

    @FXML
    Label score, contLabel;

    @FXML
    ImageView crossButton, playButton, resumeButton;

    @FXML public void initialize() {
        rotate();
        score.setText(String.valueOf(Main.score));
        int num = Main.scoreCont/3;
        contLabel.setText("For " + num + " stars?");
        Main.scoreCont -= num;

        Image playImage = new Image(new File("ColorSwitch/src/Images/PlayButton.png").toURI().toString());
        playButton.setImage(playImage);
        Image resumeImage = new Image(new File("ColorSwitch/src/Images/ResumeButton.png").toURI().toString());
        resumeButton.setImage(resumeImage);
        Image exitImage = new Image(new File("ColorSwitch/src/Images/CrossButton.png").toURI().toString());
        crossButton.setImage(exitImage);
    }

    public void rotate() {
        Rotate rotation = new Rotate();
        circle.getTransforms().add(rotation);
        rotation.setPivotX(circle.getBoundsInLocal().getCenterX());
        rotation.setPivotY(circle.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), 400000)));
        timeline.play();
    }

    public void playAgain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }

    public void backButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }

    public void cont() throws IOException {
        Main.contGame = true;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }

}
