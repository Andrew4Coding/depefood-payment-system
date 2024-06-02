package assignments.assignment4.Components.ExtendedComponents;

import assignments.assignment4.Components.ExtendedComponents.Text;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

public class InputWithTextField extends VBox {
    public Text label;
    public TextField textField;

    public InputWithTextField(String labelText) {
        super();
        this.setSpacing(10);

        // Setup Text
        label = new Text(labelText, 14);
        label.setFont(javafx.scene.text.Font.font("Manrope", FontWeight.BOLD, 14));


        textField = new TextField();
        textField.setPromptText(labelText);
        textField.setStyle(
                "-fx-background-color: #F6F6F6; -fx-border-color: #E8E8E8; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 20px;");
        textField.setFont(javafx.scene.text.Font.font("Manrope", FontWeight.MEDIUM, 12));

        // Set Textfield Max Width to Fill
        textField.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(label, textField);

        GridPane.setHgrow(this, javafx.scene.layout.Priority.ALWAYS);
    }

    public String getTextFieldText() {
        return textField.getText();
    }
}
