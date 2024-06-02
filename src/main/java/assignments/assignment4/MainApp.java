package assignments.assignment4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import assignments.assignment2.Menu;
import assignments.assignment2.Restaurant;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.Components.ExtendedComponents.Scene;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Scenes.CustomerScene;
import assignments.assignment4.Scenes.LoginScene;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static assignments.assignment3.MainMenu.*;

public class MainApp extends Application {
    public static Stage window;
    public static Map<String, Scene> allScenes = new HashMap<>();
    public static String imageDirectory = "assignment4/src/main/java/assignments/assignment4/Assets/";

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        initUser();
        initResto();
        logCustomer();
        initScene();

        window = primaryStage;
        window.setTitle("DepeFood");
        window.setMaximized(true);

        // Customize Icon
        window.getIcons().add(new Image(new FileInputStream("assignment4/src/main/java/assignments/assignment4/Assets/andrew-logo.png")));

        // Set the initial scene of the application to the login scene
        setScene("Login");
        window.show();
    }

    // Method to set a scene
    public static void setScene(Scene scene) {
        window.setScene(scene);
    }

    public static void initResto() {
        Restaurant newResto = new Restaurant("Holycow!");
        newResto.setDescription("Steak Enak");
        restoList.add(newResto);
        restoList.getFirst().addMenu(new Menu("Tenderloin Steak", 20000));
        restoList.getFirst().addMenu(new Menu("Sirloin Steak", 300000));
        restoList.getFirst().addMenu(new Menu("Apel", 100000));

        CustomerScene.searchRestaurantObservableList.add(newResto);
    }

    public static void setScene(String sceneName) {
        window.setScene(allScenes.get(sceneName));
    }

    // Method to get a scene by name
    public static Scene getScene(String sceneName) {
        return allScenes.get(sceneName);
    }

    public static void addScene(String sceneName, Scene scene) {
        allScenes.put(sceneName, scene);
    }

    public static void logout() {
        allScenes.clear();
        Scene loginScene = new LoginScene().initPage();
        setScene(loginScene);
    }

    public void initScene() {
        // Initialize all scenes
        Scene loginScene = new LoginScene().initPage();
        Scene adminScene = new AdminScene().initPage();
        Scene customerScene = new CustomerScene().initPage();

        // Populate all scenes map
        allScenes.put("Login", loginScene);
        allScenes.put("Admin", adminScene);
        allScenes.put("Customer", customerScene);

    }

    public void logAdmin() {
        CustomerSystemCLI.currentUserLoggedIn = getUser("Admin", "123456789");
    }

    public void logCustomer() {
        CustomerSystemCLI.currentUserLoggedIn = getUser("Thomas N", "9928765403");
    }

    public static void refreshPage(Scene scene, String sceneName) {
         allScenes.remove(sceneName);
         allScenes.put(sceneName, scene);
         setScene(getScene(sceneName));
    }

    public static Alert alertMessage(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return alert;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
