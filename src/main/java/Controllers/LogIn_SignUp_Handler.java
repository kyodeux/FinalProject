package Controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LogIn_SignUp_Handler {
    @FXML
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
}
