package Game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    protected static Stage primaryStage;

    protected static boolean loadGame;
    protected static boolean contGame;

    protected static String name;
    protected static int score=0;
    protected static int color=-1;
    protected static double height=-1;

    protected static int scoreCont;
    protected static int colorCont;
    protected static double heightCont;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        primaryStage.setTitle("Color Switch");
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
