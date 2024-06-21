package Controllers;

import Model.Animate.EasingStyle;
import Model.Components.InputChecker;
import Model.User;
import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
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
    @FXML
    private TextField signup_User;
    @FXML
    private PasswordField signup_Password;
    @FXML
    private Text userNameCheck;
    @FXML
    private AnchorPane passwordRecovery;
    @FXML
    private TextField passCodeInput;
    
    private String lastPassCodeInput = "";
    private boolean setupDone = false;
    private User indexedUser;

    
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    private void resetInput() {
        login_User.setText("");
        login_Password.setText("");
        login_User.setStyle("");
        login_Password.setStyle("");

        signup_User.setText("");
        signup_Password.setText("");
        signup_User.setStyle("");
        signup_Password.setStyle("");
    }

    public void handleLogin() {
        login_User.setStyle(null);
        login_Password.setStyle(null);

        if (login_User.getText().isBlank() || login_Password.getText().isBlank()) {
            return;
        }

        String user = login_User.getText();
        try {
            indexedUser = (User) Main.userCatalog.get(user);
        } catch (JSONException e) {}

        if (indexedUser == null) {
            login_User.setStyle("-fx-border-color : #C17B61");

            return;
        }

        if (!indexedUser.getPassword().equals(login_Password.getText())) {
            login_Password.setStyle("-fx-border-color : #C17B61");

            return;
        }

        Platform.runLater(() -> {
            animateTransition();
        });
    }

    public void handleSignup() throws URISyntaxException {
        signup_User.setStyle(null);
        signup_Password.setStyle(null);
        userNameCheck.setText("");
        
        if (signup_User.getText().isBlank()) {
            signup_User.setStyle("-fx-border-color : #C17B61");
        }

        if (signup_Password.getText().isBlank()) {
            signup_Password.setStyle("-fx-border-color : #C17B61");
        }

        if (signup_User.getText().isBlank() || signup_Password.getText().isBlank()) {
            return;
        }

        String checkResult = InputChecker.checkUsername(signup_User.getText());
        
        if (!checkResult.equals("")){
            userNameCheck.setText(checkResult);
            return;
        }
        
        String user = signup_User.getText();
        String password = signup_Password.getText();
        String userName = user;

        try {
            indexedUser = (User) Main.userCatalog.get(user);
        } catch (JSONException e) {
        }

        if (indexedUser != null) {
            signup_User.setStyle("-fx-border-color : #C17B61");

            return;
        }

        
        byte[] secret = SecretGenerator.generate();
        TOTPGenerator totp = new TOTPGenerator.Builder(secret)
                        .withHOTPGenerator(builder -> {
                            builder.withPasswordLength(6);
                            builder.withAlgorithm(HMACAlgorithm.SHA512);
                        })
                        .withPeriod(java.time.Duration.ofSeconds(30))
                        .build();
        
        User newUser = new User(
                user, 
                password, 
                userName, 
                totp.getURI("FurinaGang", user).toString()
        );
        indexedUser = newUser;

        Main.userList.push(newUser);
        Main.userCatalog.put(newUser.getUserName(), newUser);

        //Crating default profile picture
        File userDir = new File(System.getProperty("user.dir")
                + "/assets/users/" + newUser.getUserName());

        if (!userDir.exists()) {
            userDir.mkdir();
        }

        File outputFile = new File(userDir.getAbsolutePath() + "\\profilePicture.png");
        Image image = new Image(getClass().getResource("/Assets/defaultProfilePic.png").toExternalForm());

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        } catch (IOException e) {
        }

        Platform.runLater(() -> {
            animateTransition();
        });
    }

    public void openPassRecovery(){
        login_User.setStyle(null);
        
        if (login_User.getText().isBlank()) {
            login_User.setStyle("-fx-border-color : #C17B61");
            return;
        }

        String user = login_User.getText();
        try {
            indexedUser = (User) Main.userCatalog.get(user);
        } catch (JSONException e) {}
        
        if (indexedUser == null){
            login_User.setStyle("-fx-border-color : #C17B61");

            return;
        }
        
        passwordRecovery.setVisible(true);
        logIn.setVisible(false);
        
        if (setupDone){return;}
        setupDone = true;
        
        passCodeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            String passCode = passCodeInput.getText();
            
            if (lastPassCodeInput.equals(passCode)){return;}
                
            if (!InputChecker.checkPassCode(passCode)){
                 passCodeInput.setText(lastPassCodeInput);
            }else{
                lastPassCodeInput = passCode;
            }
        });
    }
    
    public void closePassRecovery(){
        logIn.setVisible(true);
        passwordRecovery.setVisible(false);
    }
    
    public void verifyPassCode() throws URISyntaxException{
        String passCode = passCodeInput.getText();
        passCodeInput.setStyle(null);
        if (passCode.length() < 6){
            passCodeInput.setStyle("-fx-border-color : #C17B61;"
                    + "-fx-alignment : center;");
            return;
        }
        
        TOTPGenerator totp = TOTPGenerator.fromURI(new URI(indexedUser.getAuthURI()));
        
        if (!passCode.equals(totp.now())){
            passCodeInput.setStyle("-fx-border-color : #C17B61;"
                    + "-fx-alignment : center;");
            return;}
        passwordRecovery.setVisible(false);
        indexedUser.forgotPassword = true;
        Platform.runLater(() -> {
            animateTransition();
        });
    }
    
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
        resetInput();
        Platform.runLater(() -> {
            logIn.setVisible(true);
            signUp.setVisible(false);
        });
    }

    public void openSignUp(MouseEvent e) {
        resetInput();
        Platform.runLater(() -> {
            signUp.setVisible(true);
            logIn.setVisible(false);
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
            scheduler.schedule(task, 1300, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
        });

        animation.play();
    }
}
