package Model.Components;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.shape.Rectangle;

public class ImageCropper {

    private final ImageView viewport;
    private final ImageView backgroundView;

    private Image loadedImage;
    private double imageWidth;
    private double imageHeight;
    private double resolution;
    private double ratio;
    private String fitType;

    private double scale = 1;

    public ImageCropper(ImageView viewport, ImageView backgroundView) {
        this.viewport = viewport;
        this.backgroundView = backgroundView;

        ColorAdjust effect = new ColorAdjust();
        effect.setBrightness(-.7);
        backgroundView.setEffect(effect);

        Rectangle clip = new Rectangle();
        clip.setWidth(viewport.getFitWidth());
        clip.setHeight(viewport.getFitWidth());
        clip.setArcHeight(viewport.getFitWidth());
        clip.setArcWidth(viewport.getFitWidth());

        viewport.setClip(clip);

        viewport.setOnScroll(event -> {
            double delta = event.getDeltaY();
            double prevWidth = (fitType.equals("x") ? imageWidth * ratio : imageWidth) * scale;
            double prevHeight = (fitType.equals("y") ? imageHeight * ratio : imageHeight) * scale;

            if (delta > 0) {
                scale = clamp(scale -= .05, .2, 1);

            } else {
                scale = clamp(scale += .05, .2, 1);
            }

            double resizedWidth = (fitType.equals("x") ? imageWidth * ratio : imageWidth) * scale;
            double resizedHeight = (fitType.equals("y") ? imageHeight * ratio : imageHeight) * scale;

            x = clamp(
                    x -= (resizedWidth - prevWidth) / 2,
                    0,
                    imageWidth - resizedWidth
            );
            y = clamp(
                    y -= (resizedHeight - prevHeight) / 2,
                    0,
                    imageHeight - resizedHeight
            );

            redraw();
        });

        viewport.setOnMousePressed(event -> {
            lastKnownX = event.getX();
            lastKnownY = event.getY();
        });

        viewport.setOnMouseDragged(event -> {
            double step = dragSpeed * baseSpeed * scale * resolution;

            deltaX = (event.getX() - lastKnownX) * step;
            deltaY = (event.getY() - lastKnownY) * step;

            lastKnownX = event.getX();
            lastKnownY = event.getY();

            x = clamp(x - deltaX, 0, imageWidth - (fitType.equals("x") ? imageWidth * ratio : imageWidth) * scale);
            y = clamp(y - deltaY, 0, imageHeight - (fitType.equals("y") ? imageHeight * ratio : imageHeight) * scale);

            redraw();
        });
    }

    public void setup(Image image) {
        loadedImage = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        resolution = imageWidth * imageHeight;

        x = 0;
        y = 0;
        scale = 1;

        if (imageHeight >= imageWidth) {
            ratio = imageWidth / imageHeight;
            fitType = "y";
        } else {
            ratio = imageHeight / imageWidth;
            fitType = "x";
        }

        redraw();
    }

    private void redraw() {
        WritableImage cropped = getCroppedImage();

        viewport.setImage(cropped);
        backgroundView.setImage(cropped);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public WritableImage getCroppedImage() {
        return new WritableImage(
                loadedImage.getPixelReader(),
                (int) x,
                (int) y,
                (int) ((fitType.equals("x") ? imageWidth * ratio : imageWidth) * scale),
                (int) ((fitType.equals("y") ? imageHeight * ratio : imageHeight) * scale)
        );
    }

    //DragSystem
    private double lastKnownX;
    private double lastKnownY;

    private double deltaX;
    private double deltaY;

    private double x = 0;
    private double y = 0;
    private final double dragSpeed = 2;
    private double baseSpeed = Math.pow(10, -6);
}
