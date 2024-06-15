package Model.Components;

import Model.Animate.EasingStyle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class Menu {

    private Label buttonLabel;
    private SVGPath buttonIcon;
    private Pane button;
    private Pane view;
    private Timeline animation;

    public Menu(Pane button,
            Label buttonLabel,
            SVGPath buttonIcon,
            Pane view
    ) {
        this.button = button;
        this.buttonLabel = buttonLabel;
        this.buttonIcon = buttonIcon;
        this.view = view;

        animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(view.layoutXProperty(), 100),
                        new KeyValue(view.layoutYProperty(), 20),
                        new KeyValue(view.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(view.layoutXProperty(), 0, EasingStyle.OutCirc),
                        new KeyValue(view.layoutYProperty(), 0, EasingStyle.OutCirc),
                        new KeyValue(view.opacityProperty(), 1, EasingStyle.OutSine)
                )
        );
    }

    public Pane getButton(){
        return button;
    }
    
    public void select() {
        view.setVisible(true);
        
        animation.play();
        button.setStyle("-fx-background-color : #123F93;");
        buttonIcon.setStyle("-fx-fill : white;");
        buttonLabel.setStyle("-fx-text-fill : white;"
                + "-fx-font-family : \"Corbel\";"
                + "-fx-font-weight : bold;"
                + "-fx-font-size : 18px;");
    }

    public void deselect() {
        view.setVisible(false);
        
        button.setStyle(null);
        buttonIcon.setStyle(null);
        buttonLabel.setStyle(null);
        view.setVisible(false);
    }
}
