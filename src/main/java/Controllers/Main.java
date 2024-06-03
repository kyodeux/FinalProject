package Controllers;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private static Stage stage;
    
    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        
        try {
            Parent login_signup = FXMLLoader.load(getClass().getResource("/View/login_signup.fxml"));            
            Scene scene = new Scene(login_signup);
           
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }
    public static Stage getStage(){
        return stage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
