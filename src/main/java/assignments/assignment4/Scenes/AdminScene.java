package assignments.assignment4.Scenes;

import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ImageView;
import assignments.assignment4.Components.ExtendedComponents.Scene;
import assignments.assignment4.MainApp;
import assignments.assignment4.Pages.Restaurant.AddRestaurantMenu;
import assignments.assignment4.Pages.Restaurant.RestaurantDetailMenu;
import assignments.assignment4.Template.Navbar;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AdminScene{
    public static Navbar navbar;

    public static VBox mainContainer = new VBox();
    public static AddRestaurantMenu addRestaurantMenu = new AddRestaurantMenu();
    public static AddRestaurantMenu editRestaurantMenu = new AddRestaurantMenu();

    public static RestaurantDetailMenu restaurantDetailMenu = new RestaurantDetailMenu("");

    public static AdminSceneDashboard adminSceneDashboard = new AdminSceneDashboard();

    public AdminScene() {
        try {
            navbar = new Navbar();
        } catch (FileNotFoundException ignored) {
        }
    }
    public Scene initPage()  {
        mainContainer = new VBox();
        mainContainer.setPadding(new Insets(50, 80, 50, 80));
        
        // Setup Navbar
        try {
            navbar = new Navbar();
            mainContainer.getChildren().add(navbar);
        } catch (FileNotFoundException e) {
        }


        adminSceneDashboard = new AdminSceneDashboard();

        // Setup Main Container
        adminSceneDashboard.prefWidthProperty().bind(mainContainer.widthProperty());
        mainContainer.getChildren().add(adminSceneDashboard);

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        return new Scene(mainContainer, screenSize.getWidth(), screenSize.getHeight());
    }

    public static void clearMenu() {
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(navbar);
    }
}

class AdminSceneDashboard extends HBox {
    AdminSceneDashboard () {
        super();

        // Add Restaurant List
        RestaurantListView restaurantListView = new RestaurantListView();

        // Add Restaurant Button
        Button addRestaurantButton = new Button("Add Restaurant", Button.Variant.PRIMARY);
        addRestaurantButton.setOnAction(e -> {
            AdminScene.clearMenu();
            AdminScene.addRestaurantMenu = new AddRestaurantMenu();
            AdminScene.mainContainer.getChildren().add(AdminScene.addRestaurantMenu);
        });

        // Set Icon
        try {
            Image plusIcon = new Image(new FileInputStream(MainApp.imageDirectory + "plus.png"));
            ImageView plusIconView = new ImageView(plusIcon);

            plusIconView.setFitHeight(16);
            plusIconView.setFitWidth(16);

            addRestaurantButton.setGraphic(plusIconView);
        } catch (FileNotFoundException e) {
            return;
        }

        restaurantListView.getChildren().add(addRestaurantButton);

        // Divide View into Two
        restaurantListView.prefWidthProperty().bind(this.widthProperty().divide(2));

        // Add Restaurant List View to Main Container
        this.getChildren().add(restaurantListView);
    }
}