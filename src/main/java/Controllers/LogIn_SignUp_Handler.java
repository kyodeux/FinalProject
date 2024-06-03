package Controllers;

import Model.Animate.EasingStyle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogIn_SignUp_Handler {

    @FXML
    private Parent logIn;
    @FXML
    private Parent signUp;
    @FXML
    private AnchorPane background;
    @FXML
    private Node loadScreen;
    
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    
    public void pressedDetected(MouseEvent e) {
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }

    public void handleDrag(MouseEvent e) {
        if (stage == null) {
            stage = Main.getStage();
        }
        
        stage.setX(e.getScreenX() - xOffset);
        stage.setY(e.getScreenY() - yOffset);
    }

    public void onClosePressed(MouseEvent e) {
        System.exit(0);
    }

    public void openLogIn(MouseEvent e) {
        Platform.runLater(() -> {
            logIn.setVisible(true);
            signUp.setVisible(false);
        });
    }

    public void openSignUp(MouseEvent e) {
        Platform.runLater(() -> {
            signUp.setVisible(true);
            logIn.setVisible(false);
        });
    }

    public void onLogInRequest() {
        Platform.runLater(() -> {
            animateTransition();
        });
    }
    
    private void undoTransitionChanges(){
        logIn.setVisible(true);
        loadScreen.setVisible(false);
        background.setScaleX(1);
        background.setScaleY(1);
    }
    
    private void animateTransition() {
        signUp.setVisible(false);
        logIn.setVisible(false);
        
        double targetWidth = background.getWidth() - 100;
        double targetHeight = background.getWidth() - 100;
        
        double widthScale = targetWidth / background.getWidth();
        double heightScale = targetHeight / background.getHeight();
        
        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(background.scaleXProperty(), 1),
                        new KeyValue(background.scaleYProperty(), 1)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(background.scaleXProperty(), widthScale, EasingStyle.InOutBack),
                        new KeyValue(background.scaleYProperty(), heightScale, EasingStyle.InOutBack)
                ));
        
        animation.setOnFinished((ActionEvent t) -> {
           loadScreen.setVisible(true); 
        });
        animation.play();
    }
}
