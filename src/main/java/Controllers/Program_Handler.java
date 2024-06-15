package Controllers;

import Model.Animate.EasingStyle;
import Model.Components.ImageCropper;
import Model.Components.InputChecker;
import Model.Components.Menu;
import Model.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class Program_Handler {

    private User user;
    private File profilePicFile;
    
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
    @FXML
    private Pane passwordChangeView;
    @FXML
    private Pane passwordChange;
    @FXML
    private Pane passwordChangeContent;
    @FXML
    private Pane passwordChangedMessage;
    @FXML
    private TextField oldPasswordInput;
    @FXML
    private TextField newPasswordInput;
    @FXML
    private TextField newPasswordConfirm;
    @FXML
    private ImageView totpQR;
    @FXML
    private Pane profileButton;
    @FXML
    private Label profileButtonLabel;
    @FXML
    private SVGPath profileButtonIcon;
    @FXML
    private Pane profileView;
    @FXML
    private Pane dashboardButton;
    @FXML
    private Label dashboardButtonLabel;
    @FXML
    private SVGPath dashboardButtonIcon;
    @FXML
    private Pane dashboardView;
    @FXML
    private Pane adminButton;
    @FXML
    private Label adminButtonLabel;
    @FXML
    private SVGPath adminButtonIcon;
    @FXML
    private Pane adminView;
    @FXML
    private Pane catalogButton;
    @FXML
    private Label catalogButtonLabel;
    @FXML
    private SVGPath catalogButtonIcon;
    @FXML
    private Pane catalogView;
    @FXML
    private Pane favoritesButton;
    @FXML
    private Label favoritesButtonLabel;
    @FXML
    private SVGPath favoritesButtonIcon;
    @FXML
    private Pane favoritesView;
    @FXML
    private Pane cartButton;
    @FXML
    private Label cartButtonLabel;
    @FXML
    private SVGPath cartButtonIcon;
    @FXML
    private Pane cartView;
    @FXML
    private Pane historyButton;
    @FXML
    private Label historyButtonLabel;
    @FXML
    private SVGPath historyButtonIcon;
    @FXML
    private Pane historyView;
    
    //</editor-fold>

    public void setup(User user) {
        this.user = user;
        profilePicFile = new File(System.getProperty("user.dir")
                + "\\assets\\users\\" + user.getUserName() + "\\profilePicture.png");
    }

    public void init() {
        //Basic setup
        // <editor-fold defaultstate="collapsed" desc="Basic Setup">
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
            displayNameCheck.setText(InputChecker.checkDisplayName(newDisplayName.getText()));
        });
        
        try{
            totpQR.setImage(generateQR(
                user.getAuthURI(),
                (int) totpQR.getFitWidth(),
                (int) totpQR.getFitHeight()
            ));
        }catch(WriterException | IOException e){}
        Rectangle clip2 = new Rectangle();
        clip2.setWidth(totpQR.getFitWidth());
        clip2.setHeight(totpQR.getFitWidth());
        clip2.setArcHeight(totpQR.getFitWidth() * .3);
        clip2.setArcWidth(totpQR.getFitWidth() * .3);
        
        totpQR.setClip(clip2);
        //</editor-fold>
        
        //Creating menus
        profileMenu = new Menu(
                profileButton,
                profileButtonLabel,
                profileButtonIcon,
                profileView
        );
        dashboardMenu = new Menu(
                dashboardButton,
                dashboardButtonLabel,
                dashboardButtonIcon,
                dashboardView
        );
        adminMenu = new Menu(
                adminButton,
                adminButtonLabel,
                adminButtonIcon,
                adminView
        );
        catalogMenu = new Menu(
                catalogButton,
                catalogButtonLabel,
                catalogButtonIcon,
                catalogView
        );
        favoritesMenu = new Menu(
                favoritesButton,
                favoritesButtonLabel,
                favoritesButtonIcon,
                favoritesView
        );
        cartMenu = new Menu(
                cartButton,
                cartButtonLabel,
                cartButtonIcon,
                cartView
        );
        historyMenu = new Menu(
                historyButton,
                historyButtonLabel,
                historyButtonIcon,
                historyView
        );
        profileMenu.select();
        selectedMenu = profileMenu;
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

                contentAnimation.setOnFinished((ActionEvent x) -> {
                    if (!user.forgotPassword){return;}
                    openPasswordChange();
                });
                contentAnimation.play();
            });
        });

        animation.play();
    }

    //pfp editor
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
        } catch (IOException e) {
        }

        closeProfilePicEditor();
    }

    //displayName change
    public void openDisplayNameChange() {
        openPopMenu(
                profileViewContent,
                displayNameChangeView,
                displayNameChange,
                displayNameChangeContent
        );
    }

    public void closeDisplayNameChange() {
        newDisplayName.setText("");
        displayNameCheck.setText("");
        closePopMenu(
                profileViewContent,
                displayNameChangeView,
                displayNameChange,
                displayNameChangeContent
        );
    }

    public void handleDisplayNameChangeRequest() {
        String current = newDisplayName.getText();
        if (current.isBlank()) {
            return;
        }
        if (!InputChecker.checkDisplayName(current).equals("")) {
            return;
        }

        user.setDisplayName(current);
        userDisplayName.setText(user.getDisplayName());
        userDisplayNameAlt.setText(user.getDisplayName());

        closeDisplayNameChange();
    }

    //password change
    private ScheduledExecutorService scheduler;

    private final Runnable onPasswordChanged = () -> {
        Platform.runLater(() -> {
            passwordChangedMessage.setVisible(false);
            ColorAdjust colorAdjust = (ColorAdjust) profileViewContent.getEffect();
            Timeline sizeAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(passwordChange.scaleXProperty(), 1),
                            new KeyValue(passwordChange.scaleYProperty(), 1)
                    ),
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(passwordChange.scaleXProperty(), .5, EasingStyle.OutSine),
                            new KeyValue(passwordChange.scaleYProperty(), .5, EasingStyle.OutSine)
                    )
            );
            
            Timeline brightnessAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(colorAdjust.brightnessProperty(), -.4)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(colorAdjust.brightnessProperty(), 0, EasingStyle.OutSine)
                ));
            
            sizeAnimation.play();
            brightnessAnimation.play();
            sizeAnimation.setOnFinished((ActionEvent e) -> {
                
                passwordChange.setLayoutX(255);
                passwordChange.setLayoutY(148);
                passwordChange.setPrefWidth(316);
                passwordChange.setPrefHeight(366);
                passwordChangeView.setVisible(false);
                profileViewContent.setEffect(null);
            });
        });
    };

    private void resetPasswordFields(){
        oldPasswordInput.setText("");
        oldPasswordInput.setStyle(null);
        newPasswordInput.setText("");
        newPasswordInput.setStyle(null);
        newPasswordConfirm.setText("");
        newPasswordConfirm.setStyle(null);
    }
    
    public void openPasswordChange() {
        oldPasswordInput.setDisable(user.forgotPassword);
        if (user.forgotPassword){
            oldPasswordInput.setText(user.getPassword());
        }
        openPopMenu(
                profileViewContent,
                passwordChangeView,
                passwordChange,
                passwordChangeContent
        );
    }

    public void closePasswordChange() {
        resetPasswordFields();
        
        closePopMenu(
                profileViewContent,
                passwordChangeView,
                passwordChange,
                passwordChangeContent
        );
    }

    public void handlePasswordChangeRequest() {
        oldPasswordInput.setStyle(null);
        newPasswordInput.setStyle(null);
        newPasswordConfirm.setStyle(null);

        boolean inputsFine = true;
        if (oldPasswordInput.getText().isBlank() && !user.forgotPassword) {
            oldPasswordInput.setStyle("-fx-border-color : #C17B61");
            inputsFine = false;
        }

        if (newPasswordInput.getText().isBlank()) {
            newPasswordInput.setStyle("-fx-border-color : #C17B61");
            inputsFine = false;
        }

        if (newPasswordConfirm.getText().isBlank()) {
            newPasswordConfirm.setStyle("-fx-border-color : #C17B61");
            inputsFine = false;
        }

        if (!inputsFine) {
            return;
        }
        if (!oldPasswordInput.getText().equals(user.getPassword()) && !user.forgotPassword) {
            oldPasswordInput.setStyle("-fx-border-color : #C17B61");
            return;
        }
        String newPassword = newPasswordInput.getText();

        if (!newPasswordConfirm.getText().equals(newPassword)) {
            newPasswordConfirm.setStyle("-fx-border-color : #C17B61");
            return;
        }
        
        resetPasswordFields();
        user.setPassword(newPassword);
        user.forgotPassword = false;
        
        scheduler = Executors.newScheduledThreadPool(1);
        showPasswordChangedMessage();
    }

    public void showPasswordChangedMessage() {
        passwordChangeContent.setVisible(false);
        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(passwordChange.prefWidthProperty(),
                                passwordChange.getPrefWidth()
                        ),
                        new KeyValue(passwordChange.prefHeightProperty(),
                                passwordChange.getHeight()
                        ),
                        new KeyValue(passwordChange.layoutXProperty(),
                                passwordChange.getLayoutX()
                        ),
                        new KeyValue(passwordChange.layoutYProperty(),
                                passwordChange.getLayoutY()
                        )
                ),
                new KeyFrame(Duration.millis(700),
                        new KeyValue(passwordChange.prefWidthProperty(),
                                334, EasingStyle.InOutBack
                        ),
                        new KeyValue(passwordChange.prefHeightProperty(),
                                151, EasingStyle.InOutBack
                        ),
                        new KeyValue(passwordChange.layoutXProperty(),
                                246, EasingStyle.InOutBack
                        ),
                        new KeyValue(passwordChange.layoutYProperty(),
                                255, EasingStyle.InOutBack
                        )
                )
        );

        animation.play();
        animation.setOnFinished((ActionEvent e) -> {
            passwordChangedMessage.setVisible(true);
            scheduler.schedule(onPasswordChanged, 1300, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
        });
    }

    //menu navigation
    private Menu selectedMenu;
    private Menu profileMenu;
    private Menu dashboardMenu;
    private Menu adminMenu;
    private Menu catalogMenu;
    private Menu favoritesMenu;
    private Menu cartMenu;
    private Menu historyMenu;
    
    private void switchMenu(Menu menu){
        if (selectedMenu != null){
            selectedMenu.deselect();
        }
        
        menu.select();
        selectedMenu = menu;
    }
    
    public void handleMenuSelection(MouseEvent e){
        Pane source = (Pane) e.getSource();
        if (source.equals(selectedMenu.getButton())){
            return;
        }
        
        if (source.equals(profileButton)){
            switchMenu(profileMenu);
        }else if(source.equals(dashboardButton)){
            switchMenu(dashboardMenu);
        }else if(source.equals(adminButton)){
            switchMenu(adminMenu);
        }else if(source.equals(catalogButton)){
            switchMenu(catalogMenu);
        }else if(source.equals(favoritesButton)){
            switchMenu(favoritesMenu);
        }else if(source.equals(cartButton)){
            switchMenu(cartMenu);
        }else if(source.equals(historyButton)){
            switchMenu(historyMenu);
        }
    }
    
    //TOTP System
    public Image generateQR(String data, int h, int w) throws WriterException, IOException {
        String charset = new String(data.getBytes("UTF-8"), "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(
                charset,
                BarcodeFormat.QR_CODE,
                w,
                h);

        BufferedImage swingImage = MatrixToImageWriter.toBufferedImage(matrix);
        Image image = SwingFXUtils.toFXImage(swingImage, null);
        
        return image;
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
        
        if (scheduler != null){scheduler.shutdown();}
        animation.play();
        animation.setOnFinished((ActionEvent e) -> {
            Main.backToLogin();
        });
    }
    
    public void openPopMenu(Pane menu, Pane view, Pane popMenu, Pane popMenuContent) {
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

    public void closePopMenu(Pane menu, Pane view, Pane popMenu, Pane popMenuContent) {
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
}
