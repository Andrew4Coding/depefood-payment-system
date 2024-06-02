package assignments.assignment4.Components.ExtendedComponents;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Button extends javafx.scene.control.Button {
    String style;

    public enum Variant {
        PRIMARY, SECONDARY, DESTRUCTIVE, TRANSPARENT
    }

    public Button(String text, Variant variant) {
        super(text);
        this.style = "-fx-font-family: Manrope; ;  -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 20px 20px; -fx-background-radius: 10px;";
        this.setFont(Font.font("Manrope", FontWeight.MEDIUM, 14));
        this.setStyle(style + "-fx-background-color: #576BEA; -fx-text-fill: white;");

        if (variant.equals(Variant.PRIMARY)) {
            this.setStyle(style + "-fx-background-color: #576BEA; -fx-text-fill: white;");

            // Add Hover Effect
            this.setOnMouseEntered(e -> {
                this.setStyle(style + "-fx-background-color: #4B5BE7; -fx-text-fill: white;");
            });

            // Add On Mouse Left
            this.setOnMouseExited(e -> {
                this.setStyle(style + "-fx-background-color: #576BEA; -fx-text-fill: white;");
            });
        } else if (variant.equals(Variant.SECONDARY)) {
            this.setStyle(style + "-fx-background-color: #F6F8FF; -fx-text-fill: #576BEA;");

            // Add Hover Effect
            this.setOnMouseEntered(e -> {
                this.setStyle(style + "-fx-background-color: #ECEFF7; -fx-text-fill: #576BEA;");
            });

            // Add On Mouse Left
            this.setOnMouseExited(e -> {
                this.setStyle(style + "-fx-background-color: #F6F8FF; -fx-text-fill: #576BEA;");
            });

        } else if (variant.equals(Variant.DESTRUCTIVE)) {
            this.setStyle(style + "-fx-background-color: #FF5C5C; -fx-text-fill: white;");

            // Add Hover Effect
            this.setOnMouseEntered(e -> {
                this.setStyle(style + "-fx-background-color: #FF4A4A; -fx-text-fill: white;");
            });

            // Add On Mouse Left
            this.setOnMouseExited(e -> {
                this.setStyle(style + "-fx-background-color: #FF5C5C; -fx-text-fill: white;");
            });
        }
        else if (variant.equals(Variant.TRANSPARENT)) {
            this.setStyle(style + "-fx-background-color: transparent; -fx-text-fill: #576BEA;");

            // Add Hover Effect
            this.setOnMouseEntered(e -> {
                this.setStyle(style + "-fx-background-color: #F6F8FF; -fx-text-fill: #576BEA;");
            });

            // Add On Mouse Left
            this.setOnMouseExited(e -> {
                this.setStyle(style + "-fx-background-color: transparent; -fx-text-fill: #576BEA;");
            });
        }

    }
}
