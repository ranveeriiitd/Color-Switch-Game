package Game;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Game {
    private final int[] levels = new int[10];

    private final String RED = "#FF0181";       //#1
    private final String YELLOW = "#FAE100";    //#2
    private final String PURPLE = "#900DFF";    //#3
    private final String BLUE = "#32DBF0";      //#4
    private int currentColor = 1;

    private int initialScore=-1;
    private int initialColor=-1;
    private double initialHeight=-1;

    @FXML Circle player;
    @FXML ScrollPane pane;
    @FXML Label score;
    @FXML SubScene subScene;
    int playerScore = 0;

    @FXML
    private Group star1, star2, star3, star4, star5, star6, circle1, circle2, circle3,
            square1, square2, nestedRing, plus1, plus2, color1, color2, color3, triangle;

    private final ArrayList<Group> stars = new ArrayList<>(6),
            colorSwitchers = new ArrayList<>(3),
            rest = new ArrayList<>();

    private final HashMap<Group, Boolean> visitedSwitchers = new HashMap<>(),
            visitedStars = new HashMap<>();

    private double playerStartPosition;
    Timeline playerDownTimeline;
    private boolean isGameOver;

    AnimationTimer gameOver;
    AnimationTimer collisionAnimation;

    AudioClip jumpSound;
    AudioClip gameOverSound;

    @FXML
    public void initialize() throws IOException {
        levels[0] = 20000;
        for (int i = 1; i < 10; i++) {
            levels[i] = (i+1) * levels[0];
        }
        visitedSwitchers.put(color1, false);
        visitedSwitchers.put(color2, false);
        visitedSwitchers.put(color3, false);
        visitedStars.put(star1, false);
        visitedStars.put(star2, false);
        visitedStars.put(star3, false);
        visitedStars.put(star4, false);
        visitedStars.put(star5, false);
        visitedStars.put(star6, false);

        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);
        stars.add(star6);
        colorSwitchers.add(color1);
        colorSwitchers.add(color2);
        colorSwitchers.add(color3);
        rest.add(circle1);
        rest.add(circle2);
        rest.add(circle3);
        rest.add(square1);
        rest.add(square2);
        rest.add(plus1);
        rest.add(plus2);
        rotate(400000);
        rotateTriangle(400000);
        rotateNestedRing(400000);

        playerStartPosition = player.getBoundsInParent().getCenterY();

        Translate translate = new Translate();
        playerDownTimeline = new Timeline(new KeyFrame(Duration.seconds(500), new KeyValue(translate.yProperty(), 120000)));
        player.getTransforms().addAll(translate);
        score.getTransforms().addAll(translate);
        subScene.getTransforms().addAll(translate);

        collisionAnimation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                Bounds playerPos = player.getBoundsInParent();
                if (playerPos.getCenterY() <= playerStartPosition) {
                    playerDownTimeline.play();
                } else {
                    playerDownTimeline.pause();
                }

                starAnimation(playerPos);
                colorSwitchAnimation(playerPos);
                if (playerPos.getCenterY() < 1500) {
                    Translate tsl = new Translate();
                    tsl.setY(4400);
                    player.getTransforms().addAll(tsl);
                    score.getTransforms().addAll(tsl);
                    subScene.getTransforms().addAll(tsl);
                    reInitialize();
                }
                if (collision()) {
                    isGameOver = true;
                }
            }
        };
        collisionAnimation.start();

        AnimationTimer updateScore = new AnimationTimer() {
            @Override
            public void handle(long l) {
                score.setText(String.valueOf(playerScore));
                Main.score = playerScore;
            }
        };
        updateScore.start();

        AnimationTimer scroll = new AnimationTimer() {
            @Override
            public void handle(long l) {
                scroll();
            }
        };
        scroll.start();

        gameOver = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    gameOver();
                } catch (IOException ignored) {
                }
            }
        };
        gameOver.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pauseScreen.fxml"));
        Parent root = fxmlLoader.load();
        subScene.setRoot(root);
        if(Main.loadGame) {
            loadGame();
            Main.loadGame = false;
        }
        else if(Main.contGame) {
            contGame();
            Main.contGame = false;
        }
        jumpSound = new AudioClip(new File("ColorSwitch\\src\\Sounds\\jump.wav").toURI().toString());
        gameOverSound = new AudioClip(new File("ColorSwitch\\src\\Sounds\\dead.wav").toURI().toString());

    }

    private void contGame() {
        initialScore = Main.scoreCont;
        initialColor = Main.colorCont;
        initialHeight = Main.heightCont;

        Translate translate = new Translate();
        translate.setY(initialHeight - playerStartPosition + 50);

        player.getTransforms().add(translate);
        score.getTransforms().add(translate);
        subScene.getTransforms().add(translate);
        playerScore = initialScore;
        switch (initialColor) {
            case 1:
                player.setStroke(Paint.valueOf(RED));
                break;
            case 2:
                player.setStroke(Paint.valueOf(YELLOW));
                break;
            case 3:
                player.setStroke(Paint.valueOf(PURPLE));
                break;
            case 4:
                player.setStroke(Paint.valueOf(BLUE));
                break;
        }
        player.setStrokeWidth(10);
        currentColor = initialColor;
        collisionAnimation.stop();
        playerDownTimeline.pause();
        for(Group g : visitedStars.keySet()) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                visitedStars.replace(g, true);
        }
        for(Group g : visitedSwitchers.keySet()) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                visitedSwitchers.replace(g, true);
        }
        for(Group g : stars) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                g.setVisible(false);
        }
        for(Group g : colorSwitchers) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                g.setVisible(false);
        }
    }

    private void loadGame() {
        initialScore = Main.score;
        initialColor = Main.color;
        initialHeight = Main.height;

        Translate translate = new Translate();
        translate.setY(initialHeight - playerStartPosition);

        player.getTransforms().add(translate);
        score.getTransforms().add(translate);
        subScene.getTransforms().add(translate);
        playerScore = initialScore;
        switch (initialColor) {
            case 1:
                player.setStroke(Paint.valueOf(RED));
                break;
            case 2:
                player.setStroke(Paint.valueOf(YELLOW));
                break;
            case 3:
                player.setStroke(Paint.valueOf(PURPLE));
                break;
            case 4:
                player.setStroke(Paint.valueOf(BLUE));
                break;
        }
        player.setStrokeWidth(10);
        currentColor = initialColor;
        collisionAnimation.stop();
        playerDownTimeline.pause();
        for(Group g : visitedStars.keySet()) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                visitedStars.replace(g, true);
        }
        for(Group g : visitedSwitchers.keySet()) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                visitedSwitchers.replace(g, true);
        }
        for(Group g : stars) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                g.setVisible(false);
        }
        for(Group g : colorSwitchers) {
            if(g.getBoundsInParent().getCenterY() > player.getBoundsInParent().getCenterY())
                g.setVisible(false);
        }

    }

    public void jump() {
        if(!subScene.isVisible()) {
            Translate translate = new Translate();
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), new KeyValue(translate.yProperty(), -150)));
            timeline.play();
            player.getTransforms().addAll(translate);
            score.getTransforms().addAll(translate);
            subScene.getTransforms().addAll(translate);
            playerDownTimeline.play();
            collisionAnimation.start();

//            jumpSound.play();
        }

    }

    private void rotate(int level) {
        for (Group group : rest) {
            Rotate rotation = new Rotate();
            group.getTransforms().add(rotation);
            rotation.setPivotX(group.getBoundsInLocal().getCenterX());
            rotation.setPivotY(group.getBoundsInLocal().getCenterY());
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), level)));
            timeline.play();
        }
    }

    private void rotateTriangle(int level) {
        Rotate rotation = new Rotate();
        triangle.getTransforms().add(rotation);
        Node pivot = triangle.getChildren().get(3);
        rotation.setPivotX(pivot.getBoundsInParent().getCenterX());
        rotation.setPivotY(pivot.getBoundsInParent().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), level)));
        timeline.play();
    }

    private void rotateNestedRing(int level) {
        Rotate rotation = new Rotate();
        nestedRing.getTransforms().add(rotation);
        rotation.setPivotX(nestedRing.getBoundsInLocal().getCenterX());
        rotation.setPivotY(nestedRing.getBoundsInLocal().getCenterY());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotation.angleProperty(), level)));
        timeline.play();
        Node node = nestedRing.getChildren().get(0);
        Rotate rotate = new Rotate();
        node.getTransforms().add(rotate);
        rotate.setPivotY(node.getBoundsInLocal().getCenterY());
        rotate.setPivotX(node.getBoundsInLocal().getCenterX());
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(4000), new KeyValue(rotate.angleProperty(), -2 * level)));
        tl.play();
    }

    private void starAnimation(Bounds playerBounds) {
        for (Group star : stars) {
            if (visitedStars.get(star)) continue;
            if (pane.localToParent(star.getBoundsInParent()).intersects(playerBounds)) {
                star.setVisible(false);
                playerScore++;
                visitedStars.replace(star, true);
            }
        }
    }

    private void colorSwitchAnimation(Bounds playerBounds) {
        for (Group g : colorSwitchers) {
            if (visitedSwitchers.get(g)) continue;
            if (pane.localToParent(g.getBoundsInParent()).intersects(playerBounds)) {
                g.setVisible(false);
                Random random = new Random();
                int limit = 4;
                if (g.getId().equals("color1")) {
                    limit = 3;
                }
                int val = random.nextInt(limit) + 1;
                if (g.getId().equals("color3")) val = 4;
                while (val == currentColor) {
                    val = random.nextInt(limit) + 1;
                }
                switch (val) {
                    case 1:
                        player.setStroke(Paint.valueOf(RED));
                        break;
                    case 2:
                        player.setStroke(Paint.valueOf(YELLOW));
                        break;
                    case 3:
                        player.setStroke(Paint.valueOf(PURPLE));
                        break;
                    case 4:
                        player.setStroke(Paint.valueOf(BLUE));
                        break;
                }
                player.setStrokeWidth(10);
                visitedSwitchers.replace(g, true);
                currentColor = val;
            }
        }
    }


    private void scroll() {
        double len = player.getParent().getBoundsInParent().getHeight();
        double pos = player.getBoundsInParent().getCenterY();
        pane.setVvalue(pos / len);
    }

    private String getCurrentColor() {
        switch (currentColor) {
            case 1:
                return RED.substring(1).toLowerCase();
            case 2:
                return YELLOW.substring(1).toLowerCase();
            case 3:
                return PURPLE.substring(1).toLowerCase();
        }
        return BLUE.substring(1).toLowerCase();
    }

    private boolean collision() {
        for (Group g : rest) {
            if (pane.localToParent(g.getBoundsInParent()).intersects(player.getBoundsInParent())) {
                for (Node node : g.getChildren()) {
                    Shape shape = (Shape) node;
                    if (pane.localToParent(g.localToParent(shape.getBoundsInParent())).intersects(player.getBoundsInParent())) {
                        if (Shape.intersect(shape, player).getBoundsInLocal().getWidth() != -1)
                            if (!shape.getStroke().toString().substring(2, 8).equals(getCurrentColor())) {
                                return true;
                            }
                    }
                }
            }
        }
        if (pane.localToParent(triangle.getBoundsInParent()).intersects(player.getBoundsInParent())) {
            for (Node node : triangle.getChildren()) {
                if(node instanceof Circle) continue;
                Shape shape = (Shape) node;
                if (pane.localToParent(triangle.localToParent(shape.getBoundsInParent())).intersects(player.getBoundsInParent()))
                    if (Shape.intersect(shape, player).getBoundsInLocal().getWidth() != -1)
                        if (!shape.getStroke().toString().substring(2, 8).equals(getCurrentColor())) {
                            return true;
                        }
            }
        }
        return false;
    }

    private void gameOver() throws IOException {
        if (isGameOver) {
            Main.heightCont = player.getBoundsInParent().getCenterY();
            Main.colorCont = currentColor;
            Main.scoreCont = playerScore;
            gameOverSound.play();
            gameOver.stop();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("endScreen.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = Main.primaryStage;
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("Color Switch");
            stage.show();
        }
    }

    int level = 1;
    private void reInitialize() {
        for(Group g : visitedStars.keySet()) {
            visitedStars.replace(g, false);
        }
        for(Group g : visitedSwitchers.keySet()) {
            visitedSwitchers.replace(g, false);
        }
        for(Group g : stars) {
            g.setVisible(true);
        }
        for(Group g : colorSwitchers) {
            g.setVisible(true);
        }
        rotate(levels[level]);
        rotateTriangle(levels[level]);
        rotateNestedRing(levels[level]);
        if(level < 9) level++;
    }

    public void pause() {
        collisionAnimation.stop();
        playerDownTimeline.pause();
        subScene.setOpacity(0.5);
        subScene.setVisible(true);
        Parent parent = subScene.getRoot();
        ImageView resumeButton = (ImageView) parent.getChildrenUnmodifiable().get(1);
        Image resumeImage = new Image(new File("src/Images/ResumeButton.png").toURI().toString());
        resumeButton.setImage(resumeImage);
        ImageView saveButton = (ImageView) parent.getChildrenUnmodifiable().get(2);
        Image saveImage = new Image(new File("src/Images/SaveButton.png").toURI().toString());
        saveButton.setImage(saveImage);

        resumeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                subScene.setVisible(false);
            }
        });
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    saveGame();
                } catch (IOException ignored) {
                }
            }
        });
    }

    public void saveGame() throws IOException {
        int color = currentColor;
        int score = playerScore;
        double height = player.getBoundsInParent().getCenterY();
        File file = new File("savedGames.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        String string = "";
        string += Main.name + " " + color + " " + score + " " + height + "\n";
        fileWriter.append(string);
        fileWriter.close();
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = Main.primaryStage;
        stage.setTitle("Color Switch");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }
}


