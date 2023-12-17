package Game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class LogIn {

    @FXML
    TextField textField;

    @FXML
    Group p1, p2;

    @FXML
    ImageView image;

    @FXML ImageView playButton, cancelButton;

    @FXML public void initialize() {
        rotateP1();
        rotateP2();
        Image colorSwitch = new Image(new File("ColorSwitch/src/Images/ColorSwitch.jpg").toURI().toString());
        image.setImage(colorSwitch);
        Image playImage = new Image(new File("ColorSwitch/src/Images/PlayButton.png").toURI().toString());
        playButton.setImage(playImage);
        Image cancelImage = new Image(new File("ColorSwitch/src/Images/CrossButton.png").toURI().toString());
        cancelButton.setImage(cancelImage);
    }

    public void rotateP1() {
        Rotate rotation = new Rotate();
        p1.getTransforms().add(rotation);
        rotation.setPivotX(p1.getBoundsInLocal().getCenterX());
        rotation.setPivotY(p1.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), 400000)));
        timeline.play();
    }

    public void rotateP2() {
        Rotate rotation = new Rotate();
        p2.getTransforms().add(rotation);
        rotation.setPivotX(p2.getBoundsInLocal().getCenterX());
        rotation.setPivotY(p2.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), -400000)));
        timeline.play();
    }

    public void cancelButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();

    }

    public void playButton() throws IOException {
        Main.name = textField.getText();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }
}
