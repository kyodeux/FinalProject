package Controllers;

import Model.Animate.EasingStyle;
import Model.Components.ImageCropper;
import Model.Components.UsernameChecker;
import Model.User;
import java.io.File;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class Program_Handler {

    private User user;

    // <editor-fold defaultstate="collapsed" desc="Setup FXML variables">
    @FXML
    private AnchorPane background;
    @FXML
    private AnchorPane content;
    @FXML
    private Label welcomeUserType;
    @FXML
    private Label welcomeMessage;
    @FXML
    private FlowPane welcomeMessage1;
    @FXML
    private Label userDisplayName;
    @FXML
    private Label userName;
    @FXML
    private ImageView profilePicture;
    @FXML
    private ImageView profilePictureIcon;
    @FXML
    private ImageView viewport;
    @FXML
    private ImageView backgroundView;
    @FXML
    private Label userNameIcon;
    @FXML
    private Label userTypeIcon;
    @FXML
    private Label userDisplayNameAlt;
    @FXML
    private Label userNameAlt;
    @FXML
    private Pane profileView;
    @FXML
    private Pane profileViewContent;
    @FXML
    private Pane profilePicEditorView;
    @FXML
    private Pane profilePicEditor;
    @FXML
    private Pane profilePicEditorContent;
    @FXML
    private Pane displayNameChangeView;
    @FXML
    private Pane displayNameChange;
    @FXML
    private Pane displayNameChangeContent;
    @FXML
    private TextField newDisplayName;
    @FXML
    private Text displayNameCheck;
    
    //</editor-fold>
    
    private File profilePicFile;

    public void setup(User user) {
        this.user = user;
        profilePicFile = new File(System.getProperty("user.dir")
                + "\\assets\\users\\" + user.getUserName() + "\\profilePicture.png");
    }

    public void init() {
        userDisplayName.setText(user.getDisplayName());
        userName.setText("@" + user.getUserName());

        userDisplayNameAlt.setText(user.getDisplayName());
        userNameAlt.setText(user.getUserName());
        
        Image pfp = new Image(profilePicFile.toURI().toString());
        profilePicture.setImage(pfp);
        profilePictureIcon.setImage(pfp);

        Rectangle clip = new Rectangle();
        clip.setWidth(profilePicture.getFitWidth());
        clip.setHeight(profilePicture.getFitWidth());
        clip.setArcHeight(profilePicture.getFitWidth());
        clip.setArcWidth(profilePicture.getFitWidth());
        profilePicture.setClip(clip);

        Rectangle clip1 = new Rectangle();
        clip1.setWidth(profilePictureIcon.getFitWidth());
        clip1.setHeight(profilePictureIcon.getFitWidth());
        clip1.setArcHeight(profilePictureIcon.getFitWidth());
        clip1.setArcWidth(profilePictureIcon.getFitWidth());
        profilePictureIcon.setClip(clip1);

        userNameIcon.setText(user.getUserName());
        if (user.isAdmin()) {
            userTypeIcon.setText("Admin");
            userTypeIcon.setStyle("-fx-text-fill : #DADE0A;");
        } else {
            userTypeIcon.setText("Usuario");
            userTypeIcon.setStyle("-fx-text-fill : #1A47EC;");
        }

        cropper = new ImageCropper(viewport, backgroundView);
        
        newDisplayName.textProperty().addListener((observable, oldValue, newValue) -> {
            displayNameCheck.setText(
                UsernameChecker.check(newDisplayName.getText())
            );
        });
    }

    public void playWelcomeAnimation() {
        content.setVisible(false);
        content.setLayoutX(100);
        content.setLayoutY(20);
        content.setOpacity(0);

        welcomeMessage.setText("Bienvenido @" + user.getDisplayName());
        if (user.isAdmin()) {
            welcomeUserType.setText("Admin");
            welcomeUserType.setStyle("-fx-text-fill : #DADE0A;");
        } else {
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

                content.setVisible(true);
                Timeline contentAnimation = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(content.layoutXProperty(), content.getLayoutX()),
                                new KeyValue(content.layoutYProperty(), content.getLayoutY()),
                                new KeyValue(content.opacityProperty(), 0)
                        ),
                        new KeyFrame(Duration.millis(500),
                                new KeyValue(content.layoutXProperty(), 0, EasingStyle.OutQuad),
                                new KeyValue(content.layoutYProperty(), 0, EasingStyle.OutQuad),
                                new KeyValue(content.opacityProperty(), 1, EasingStyle.OutSine)
                        )
                );

                contentAnimation.play();
            });
        });

        animation.play();
    }
    
    //Pfp Editor
    
    private ImageCropper cropper;

    public void handlePfpEdit() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(Main.getStage());

        if (file != null) {
            Image image = new Image(file.toURI().toString());

            cropper.setup(image);
            openProfilePicEditor();
        }
    }

    public void openProfilePicEditor() {
        openPopMenu(
                profileViewContent, 
                profilePicEditorView, 
                profilePicEditor, 
                profilePicEditorContent
        );
    }

    public void closeProfilePicEditor() {
        closePopMenu(
                profileViewContent, 
                profilePicEditorView, 
                profilePicEditor, 
                profilePicEditorContent
        );
    }

    public void updatePfp() {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(cropper.getCroppedImage(), null), "png", profilePicFile);

            Image pfp = new Image(profilePicFile.toURI().toString());
            profilePicture.setImage(pfp);
            profilePictureIcon.setImage(pfp);
        } catch (IOException e) {}

        closeProfilePicEditor();
    }

    //--displayName Editor
    
    public void openDisplayNameChange(){
        openPopMenu(
                profileViewContent,
                displayNameChangeView,
                displayNameChange,
                displayNameChangeContent
        );
    }
    
    public void closeDisplayNameChange(){
        newDisplayName.setText("");
        displayNameCheck.setText("");
        closePopMenu(
                profileViewContent,
                displayNameChangeView,
                displayNameChange,
                displayNameChangeContent
        );
    }
    
    public void handleDisplayNameChangeRequest(){
        String current = newDisplayName.getText();
        if (current.isBlank()){return;}
        if (!UsernameChecker.check(current).equals("")){return;}
        
        user.setDisplayName(current);
        userDisplayName.setText(user.getDisplayName());
        userDisplayNameAlt.setText(user.getDisplayName());
        
        closeDisplayNameChange();
    }
    
    public void openPopMenu(Pane menu, Pane view, Pane popMenu, Pane popMenuContent){
        ColorAdjust colorAdjust = new ColorAdjust();
        menu.setEffect(colorAdjust);
        view.setVisible(true);

        Timeline brightnessAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(colorAdjust.brightnessProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(colorAdjust.brightnessProperty(), -.4, EasingStyle.OutSine)
                ));
        Timeline sizeAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(popMenu.scaleXProperty(), .5),
                        new KeyValue(popMenu.scaleYProperty(), .5)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(popMenu.scaleXProperty(), 1, EasingStyle.OutSine),
                        new KeyValue(popMenu.scaleYProperty(), 1, EasingStyle.OutSine)
                )
        );

        brightnessAnimation.play();
        sizeAnimation.play();

        sizeAnimation.setOnFinished((ActionEvent e) -> {
            popMenuContent.setVisible(true);
        });
    }
    public void closePopMenu(Pane menu, Pane view, Pane popMenu, Pane popMenuContent){
        popMenuContent.setVisible(false);
        ColorAdjust colorAdjust = (ColorAdjust) menu.getEffect();

        Timeline brightnessAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(colorAdjust.brightnessProperty(), -.4)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(colorAdjust.brightnessProperty(), 0, EasingStyle.OutSine)
                ));
        Timeline sizeAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(popMenu.scaleXProperty(), 1),
                        new KeyValue(popMenu.scaleYProperty(), 1)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(popMenu.scaleXProperty(), .5, EasingStyle.OutSine),
                        new KeyValue(popMenu.scaleYProperty(), .5, EasingStyle.OutSine)
                )
        );

        brightnessAnimation.play();
        sizeAnimation.play();

        sizeAnimation.setOnFinished((ActionEvent e) -> {
            view.setVisible(false);
            menu.setEffect(null);
        });
    }
    
    //Logic
    public void backToLogin() {
        double targetWidth = 400;
        double targetHeight = 535;

        double widthScale = targetWidth / background.getWidth();
        double heightScale = targetHeight / background.getHeight();

        content.setVisible(false);

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(background.scaleXProperty(), background.getScaleX()),
                        new KeyValue(background.scaleYProperty(), background.getScaleX())
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(background.scaleXProperty(), widthScale, EasingStyle.OutSine),
                        new KeyValue(background.scaleYProperty(), heightScale, EasingStyle.OutSine)
                ));
        
        animation.play();
        animation.setOnFinished((ActionEvent e) -> {
            Main.backToLogin();
        });
        
    }
}
