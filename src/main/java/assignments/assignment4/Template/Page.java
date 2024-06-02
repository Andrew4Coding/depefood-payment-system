package assignments.assignment4.Template;

import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Scenes.CustomerScene;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class Page extends VBox {
    public VBox header = new VBox();
    public HBox mainBox = new HBox();

    public Page(String pageName, boolean isHasBackButton, Node backButtonPage) {
        this.setSpacing(20);
        this.prefWidthProperty().bind(this.widthProperty());

        // Header Setup
        header.setSpacing(20);
        header.prefWidthProperty().bind(this.widthProperty());

        // Back Button and Title Container
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(20);

        // Back Button
        if (isHasBackButton) {
            // Create Back Button
            Button backButton = new Button("", Button.Variant.TRANSPARENT);

            // Add Icon Image to Button
            backButton.setGraphic(new Text("\u2190", 20));


            backButton.setOnAction(e -> {
                if (CustomerSystemCLI.currentUserLoggedIn.getRole().equalsIgnoreCase("admin")) {
                    AdminScene.clearMenu();
                    AdminScene.mainContainer.getChildren().add(backButtonPage);
                } else {
                    CustomerScene.clearMenu();
                    CustomerScene.mainContainer.getChildren().add(backButtonPage);
                }
            });

            container.getChildren().add(backButton);
        }

        // Create Title
        Text title = new Text(pageName, 30);
        title.setFont(Font.font("Manrope", FontWeight.BOLD, 30));
        container.getChildren().add(title);

        header.getChildren().add(container);
        this.getChildren().add(header);

        // Main Box
        mainBox.prefWidthProperty().bind(this.widthProperty());

        // Maximize mainbox
        mainBox.prefHeightProperty().bind(this.heightProperty());

        mainBox.setSpacing(50);

        this.getChildren().add(mainBox);
    }
}
