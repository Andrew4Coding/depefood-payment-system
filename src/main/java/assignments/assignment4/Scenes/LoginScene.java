package assignments.assignment4.Scenes;

import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.InputWithTextField;
import assignments.assignment4.Components.ExtendedComponents.Scene;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.Template.Card;
import assignments.assignment4.Template.Navbar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static assignments.assignment3.MainMenu.getUser;
import static assignments.assignment4.MainApp.*;

public class LoginScene {
    HBox mainLayout = new HBox();
    private Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

    // Input Field
    private InputWithTextField emailInput;
    private InputWithTextField passwordInput;

    public Scene initPage() {
        // Setup Left
        VBox leftSide = new VBox();
        leftSide.setStyle("-fx-background-color: #576BEA;");
        leftSide.setPadding(new Insets(50, 50, 50, 50));

        // Add Image to LeftSide
        try {
            Image image = new Image(new FileInputStream("assignment4/src/main/java/assignments/assignment4/Assets/logo2.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            // Set Image Cache
            imageView.setCache(true);

            leftSide.getChildren().add(imageView);


        } catch (FileNotFoundException ignored) {

        }

        // Setup Right Side
        VBox rightSide = new VBox();
        rightSide.setStyle("-fx-background-color: #F1F1F1;");

        leftSide.prefWidthProperty().bind(mainLayout.widthProperty().divide(2));
        rightSide.prefWidthProperty().bind(mainLayout.widthProperty().divide(2));

        // Add Login Form
        LoginForm loginForm = new LoginForm();
        rightSide.getChildren().add(loginForm);

        // Set Position Center
        rightSide.setAlignment(javafx.geometry.Pos.CENTER);

        mainLayout.getChildren().addAll(leftSide, rightSide);

        return new Scene(mainLayout, screenSize.getWidth(), screenSize.getHeight());
    }

    class LoginForm extends Card {
        LoginForm() {
            super();
            this.setMaxWidth(400);
            this.setAlignment(Pos.TOP_CENTER);

            // Sign In Title
            Text title = new Text("Sign In", 30);
            title.setFont(javafx.scene.text.Font.font("Manrope", FontWeight.BOLD, 30));

            // Name Input Text Field
            emailInput = new InputWithTextField("Email");
            emailInput.setMaxWidth(Double.MAX_VALUE);

            // Phone Number Input Text Field
            passwordInput = new InputWithTextField("Password");
            passwordInput.setMaxWidth(Double.MAX_VALUE);

            // Login Button
            Button loginButton = new Button("Login", Button.Variant.PRIMARY);
            loginButton.setOnAction(e -> handleLogin());
            loginButton.setMaxWidth(Double.MAX_VALUE);

            // Handle Enter Pressed Inside Email Input
            emailInput.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().toString().equals("ENTER")) {
                    handleLogin();
                }
            });

            // Handle Enter Pressed Inside Password Input
            passwordInput.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().toString().equals("ENTER")) {
                    handleLogin();
                }
            });

            // Add to VBox
            this.getChildren().addAll(title, emailInput, passwordInput, loginButton);
        }
    }

    public void handleLogin() {
        Platform.runLater(() -> {
            CustomerSystemCLI.currentUserLoggedIn = getUser(emailInput.getTextFieldText(),
                    passwordInput.getTextFieldText());
            if (CustomerSystemCLI.currentUserLoggedIn == null) {
                System.out.println("User tidak ditemukan.");
                alert();
                return;
            }

            Navbar.userName.setText(CustomerSystemCLI.currentUserLoggedIn.getNama());
            Navbar.userPhone.setText(CustomerSystemCLI.currentUserLoggedIn.getEmail());

            if (CustomerSystemCLI.currentUserLoggedIn.getRole().equalsIgnoreCase("customer")) {
                allScenes.clear();
                // Initiate new Customer Scene
                Scene customerScene = new CustomerScene().initPage();

                // Clear Order List
                CustomerScene.orderObservableList.clear();

                // Add User Order List
                CustomerScene.orderObservableList.addAll(CustomerSystemCLI.currentUserLoggedIn.getOrderHistory());

                setScene(customerScene);
                allScenes.put("CustomerScene", customerScene);
            } else if (CustomerSystemCLI.currentUserLoggedIn.getRole().equalsIgnoreCase("admin")) {
                allScenes.clear();
                // Initiate new Admin Scene
                Scene adminScene = new AdminScene().initPage();
                setScene(adminScene);
                allScenes.put("AdminScene", adminScene);
            }
        });
    }

    private void alert() {
        System.out.println("User tidak ditemukan.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid User");
        alert.setHeaderText(null); // Set to null if you don't want a header text
        alert.setContentText("User tidak ditemukan");

        // Show the alert
        alert.showAndWait();
    }
}
