package assignments.assignment4.Components.ExtendedComponents;

public class Text extends javafx.scene.text.Text {
    public Text(String text) {
        super(text);
        super.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14px;");

    }

    public Text(String text, double fontSize) {
        super(text);
        super.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: " + fontSize + "px;");
    }
}
