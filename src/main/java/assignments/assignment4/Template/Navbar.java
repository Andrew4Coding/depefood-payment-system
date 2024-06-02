package assignments.assignment4.Template;

import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ImageView;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.MainApp;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Scenes.CustomerScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Navbar extends HBox {
    public static Text userName = new Text(CustomerSystemCLI.currentUserLoggedIn.getNama(), 20);
    public static Text userPhone = new Text(CustomerSystemCLI.currentUserLoggedIn.getEmail(), 15);
    public Navbar() throws FileNotFoundException {
        super();
        // Set Navbar COlor
        this.setPadding(new Insets(5, 5, 5, 5));
        this.setAlignment(Pos.CENTER_LEFT);

        // Set Navbar Width to Max
        this.prefWidthProperty().bind(this.widthProperty());

        // Set Text
        Text depeFoodTitle = new Text("DEPEFOOD", 30);
        depeFoodTitle.setFont(Font.font("Urbanist", FontWeight.BOLD, 30));

        this.getChildren().add(depeFoodTitle);

        // Set Space to Max
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().add(spacer);

        // Set Hbox to Contain User Info
        HBox userInfo = new HBox();
        userInfo.setSpacing(20);
        this.getChildren().add(userInfo);

        userInfo.setAlignment(Pos.CENTER);

        // User Detail Box
        VBox userDetail = new VBox();

        // Set User Name
        userName.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Set User Phone Number
        userPhone.setFont(Font.font("Manrope", FontWeight.MEDIUM, 15));
        userPhone.setStyle("-fx-fill: #95979D;");

        // Set Profile Picture
        Image image = new Image(new FileInputStream(MainApp.imageDirectory + "andrew-logo.png"));
        ImageView imageView = new ImageView(image);

        // Set Image Size
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        // Make Logout Button
        Button logoutButton = new Button("", Button.Variant.DESTRUCTIVE);
        logoutButton.setOnAction(e -> {
            if (CustomerSystemCLI.currentUserLoggedIn.getRole().equalsIgnoreCase("Customer")) {
                CustomerScene.clearMenu();
            }
            else {
                AdminScene.clearMenu();

            }
            MainApp.logout();
        });

        Image logOutIcon = new Image(new FileInputStream(MainApp.imageDirectory + "logout.png"));
        ImageView logOutImageView = new ImageView(logOutIcon);

        logOutImageView.setFitWidth(20);
        logOutImageView.setFitHeight(20);

        logoutButton.setGraphic(logOutImageView);

        userDetail.getChildren().addAll(userName, userPhone);
        userInfo.getChildren().addAll(userDetail, imageView, logoutButton);

        // Set Alignment to Right
        userDetail.setAlignment(Pos.CENTER_RIGHT);
    }
}
