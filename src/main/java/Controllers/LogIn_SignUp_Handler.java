package Controllers;

import Model.Animate.EasingStyle;
import Model.User;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONException;

public class LogIn_SignUp_Handler {

    @FXML
    private Parent logIn;
    @FXML
    private Parent signUp;
    @FXML
    private AnchorPane background;
    @FXML
    private Node loadScreen;
    @FXML
    private TextField login_User;
    @FXML
    private PasswordField login_Password;
    
    private User indexedUser;
    
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
        login_User.setStyle(null);
        login_Password.setStyle(null);
        
        if (login_User.getText().isBlank() || login_Password.getText().isBlank()){return;}
        
        String user = login_User.getText();
        try{
            indexedUser = (User) Main.userCatalog.get(user);
        }catch(JSONException e){}

        if (indexedUser == null){
            login_User.setStyle("-fx-border-color : #C17B61");
            
            return;
        }
        
        if (!indexedUser.getPassword().equals(login_Password.getText())){
            login_Password.setStyle("-fx-border-color : #C17B61");
            
            return;
        }
        
        Platform.runLater(() -> {
            animateTransition();
        });
    }
    
    private void animateTransition() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
          Platform.runLater(() -> {
            Main.openProgram();
          });
        };

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
            Main.setupProgram(indexedUser);
            scheduler.schedule(task, 1300 , TimeUnit.MILLISECONDS);
            scheduler.shutdown();
        });
        
        animation.play();
    }
}
