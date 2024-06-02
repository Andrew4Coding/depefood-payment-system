package assignments.assignment4.Template;

import assignments.assignment4.Components.ExtendedComponents.Text;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Card extends VBox {
    public Card() {
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-color: #ECEFF7; -fx-border-width: 1; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        this.setSpacing(20);

        // Set Height to Fill
        this.prefHeightProperty().bind(this.heightProperty());
    }

    public Card(String title) {
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-color: #ECEFF7; -fx-border-width: 1; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        this.setSpacing(20);
        this.prefWidthProperty().bind(this.widthProperty());

        Text titleText = new Text(title, 20);
        titleText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        this.getChildren().add(titleText);
    }
}
