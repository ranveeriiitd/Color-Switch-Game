package Game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;

public class SavedScreen {

    @FXML
    ListView<String> list;

    @FXML
    ImageView crossButton, playButton;

    @FXML
    Group p1, p2;

    @FXML
    RadioButton r1, r2, r3, r4 ,r5;

    private ArrayList<String> data = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {
        rotateP1();
        rotateP2();
        Image crossImage = new Image(new File("ColorSwitch/src/Images/CrossButton.png").toURI().toString());
        crossButton.setImage(crossImage);
        Image playImage = new Image(new File("ColorSwitch/src/Images/PlayButton.png").toURI().toString());
        playButton.setImage(playImage);

        File file = new File("savedGames.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int size=0;
        while (br.readLine() != null) {
            size++;
        }
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        for (int i = 0; i < size; i++) {
            line = br.readLine();
            if(i == 0) continue;
            if(size - i <= 5) {
                if(line == null) break;
                data.add(line);
                String[] strings = line.split(" ");
                String name = strings[0];
                list.getItems().add(name);
            }
        }

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

    public void playButton() throws IOException {
        Main.loadGame = true;
        if(r1.isSelected()) {
            String[] strings = data.get(0).split(" ");
            Main.color = Integer.parseInt(strings[1]);
            Main.score = Integer.parseInt(strings[2]);
            Main.height = Double.parseDouble(strings[3]);
        }
        if(r2.isSelected()) {
            String[] strings = data.get(1).split(" ");
            Main.color = Integer.parseInt(strings[1]);
            Main.score = Integer.parseInt(strings[2]);
            Main.height = Double.parseDouble(strings[3]);
        }
        if(r3.isSelected()) {
            String[] strings = data.get(2).split(" ");
            Main.color = Integer.parseInt(strings[1]);
            Main.score = Integer.parseInt(strings[2]);
            Main.height = Double.parseDouble(strings[3]);
        }
        if(r4.isSelected()) {
            String[] strings = data.get(3).split(" ");
            Main.color = Integer.parseInt(strings[1]);
            Main.score = Integer.parseInt(strings[2]);
            Main.height = Double.parseDouble(strings[3]);
        }
        if(r5.isSelected()) {
            String[] strings = data.get(4).split(" ");
            Main.color = Integer.parseInt(strings[1]);
            Main.score = Integer.parseInt(strings[2]);
            Main.height = Double.parseDouble(strings[3]);
        }
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }

    public void backButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = Main.primaryStage;
        stage.setScene(new Scene(root, 400, 600));
        stage.setTitle("Color Switch");
        stage.show();
    }
}
