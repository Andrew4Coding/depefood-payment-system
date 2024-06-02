package assignments.assignment4.Components.ExtendedComponents;

import javafx.scene.image.Image;

public class ImageView extends javafx.scene.image.ImageView {
    public ImageView(Image image) {
        super(image);
        this.setPreserveRatio(true);

        this.setSmooth(true);

        this.setCache(true);
    }
}
