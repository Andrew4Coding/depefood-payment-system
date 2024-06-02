package assignments.assignment4.Components.ExtendedComponents;

public class Scene extends javafx.scene.Scene {
    String styleSheet = "https://fonts.googleapis.com/css2?family=Manrope:wght@200..800&display=swap";

    public Scene(javafx.scene.Parent root, double width, double height) {
        super(root, width, height);
        this.getStylesheets().add(styleSheet);
        this.setFill(javafx.scene.paint.Color.web("#ffffff"));
    }
}
