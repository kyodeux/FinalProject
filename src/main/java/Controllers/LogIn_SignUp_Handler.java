package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LogIn_SignUp_Handler {
    @FXML
    private Parent logIn;
    @FXML
    private Parent signUp;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    public void pressedDetected(MouseEvent e){
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }
    public void handleDrag(MouseEvent e){
        Node node = (Node) e.getSource();
        Scene scene = node.getScene();
        Stage window = (Stage) scene.getWindow();
        
        window.setX(e.getScreenX() - xOffset);
        window.setY(e.getScreenY() - yOffset);
    }
    public void onClosePressed(MouseEvent e){
        System.exit(0);
    }
    public void openLogIn(MouseEvent e){
        Platform.runLater(() -> {
            logIn.setVisible(true);
            signUp.setVisible(false);
        });
    }
    public void openSignUp(MouseEvent e){
        
        Platform.runLater(() -> {
            signUp.setVisible(true);
            logIn.setVisible(false);
        });
    }
}
