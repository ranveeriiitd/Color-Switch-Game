package Game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MainScreen {

    @FXML
    Group o1, o2, nestedRing;

    @FXML
    ImageView playButton, resumeButton, exitButton;

    @FXML
    public void initialize() {
        rotateo1();
        rotateo2();
        rotateNestedRing();

        Image playImage = new Image(new File("ColorSwitch/src/Images/PlayButton.png").toURI().toString());
        playButton.setImage(playImage);
        Image resumeImage = new Image(new File("ColorSwitch/src/Images/ResumeButton.png").toURI().toString());
        resumeButton.setImage(resumeImage);
        Image exitImage = new Image(new File("ColorSwitch/src/Images/ExitButton.png").toURI().toString());
        exitButton.setImage(exitImage);

    }

    public void rotateo1() {
        Rotate rotation = new Rotate();
        o1.getTransforms().add(rotation);
        rotation.setPivotX(o1.getBoundsInLocal().getCenterX());
        rotation.setPivotY(o1.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), 400000)));
        timeline.play();
    }

    public void rotateo2() {
        Rotate rotation = new Rotate();
        o2.getTransforms().add(rotation);
        rotation.setPivotX(o2.getBoundsInLocal().getCenterX());
        rotation.setPivotY(o2.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), -400000)));
        timeline.play();
    }

    public void rotateNestedRing() {
        Rotate rotation = new Rotate();
        nestedRing.getTransforms().add(rotation);
        rotation.setPivotX(nestedRing.getBoundsInLocal().getCenterX());
        rotation.setPivotY(nestedRing.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), 300000)));
        timeline.play();
        Node node = nestedRing.getChildren().get(0);
        Rotate rotate = new Rotate();
        node.getTransforms().add(rotate);
        rotate.setPivotY(node.getBoundsInLocal().getCenterY());
        rotate.setPivotX(node.getBoundsInLocal().getCenterX());
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotate.angleProperty(), -600000)));
        tl.play();
    }

    public void newGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Login");
        stage.show();
    }

    public void resumeGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedScreen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Load Game");
        stage.show();
    }

    public void exit() {
        Main.primaryStage.close();
    }
}
