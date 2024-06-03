package Controllers;

import Model.Animate.EasingStyle;
import Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class Program_Handler {
    
    @FXML
    private AnchorPane background;
    @FXML
    private Label welcomeUserType;
    @FXML
    private Label welcomeMessage;
    @FXML
    private FlowPane welcomeMessage1;
    
    private User user;
    
    public void setup(User user){
        this.user = user;
    }

    public void playWelcomeAnimation(){
        welcomeMessage.setText("Bienvenido @" + user.getUserName());
        if (user.isAdmin()){
            welcomeUserType.setText("Admin");
            welcomeUserType.setStyle("-fx-text-fill : #DADE0A;");
        }else{
            welcomeUserType.setText("Usuario");
            welcomeUserType.setStyle("-fx-text-fill : #1A47EC;");
        }
        
        double targetWidth = 300;
        double targetHeight = 300;
        
        double widthScale = targetWidth / background.getWidth();
        double heightScale = targetHeight / background.getHeight();
        
        background.setScaleX(widthScale);
        background.setScaleY(heightScale);
        
        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(background.scaleXProperty(), widthScale),
                        new KeyValue(background.scaleYProperty(), heightScale)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(background.scaleXProperty(), 1, EasingStyle.InOutBack),
                        new KeyValue(background.scaleYProperty(), 1, EasingStyle.InOutBack)
                ));

        animation.setOnFinished((ActionEvent t) -> {
            welcomeMessage.setOpacity(0);
            welcomeMessage1.setOpacity(0);
            welcomeMessage.setVisible(true);
            welcomeMessage1.setVisible(true);
            
            Timeline welcomeAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(welcomeMessage.opacityProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(4000),
                            new KeyValue(welcomeMessage.opacityProperty(), 1, EasingStyle.OutBounce)
                    ),
                    new KeyFrame(Duration.millis(5000),
                            new KeyValue(welcomeMessage.opacityProperty(), 1)
                    ),
                    new KeyFrame(Duration.millis(5500),
                            new KeyValue(welcomeMessage.opacityProperty(), 0, EasingStyle.OutSine)
                    )
            );
            
            Timeline welcomeAnimation1 = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(welcomeMessage1.opacityProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(1500),
                            new KeyValue(welcomeMessage1.opacityProperty(), 0)
                    ),
                    new KeyFrame(Duration.millis(5000),
                            new KeyValue(welcomeMessage1.opacityProperty(), 1, EasingStyle.OutElastic)
                    ),
                    new KeyFrame(Duration.millis(5500),
                            new KeyValue(welcomeMessage1.opacityProperty(), 0, EasingStyle.OutSine)
                    )
            );
            
            welcomeAnimation.play();
            welcomeAnimation1.play();
            
            welcomeAnimation1.setOnFinished((ActionEvent e) -> {
                AnchorPane parent = (AnchorPane) welcomeMessage.getParent();
                parent.getChildren().remove(welcomeMessage);
                parent.getChildren().remove(welcomeMessage1);
            });
        });
        
        animation.play();
    }
    public void onPressed(MouseEvent e){
        Main.backToLogin();
    }
}
