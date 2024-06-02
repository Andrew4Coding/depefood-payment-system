package assignments.assignment4.Pages.Restaurant;

import assignments.assignment2.Menu;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ImageView;
import assignments.assignment4.Components.ExtendedComponents.InputWithTextField;
import assignments.assignment4.Components.ExtendedComponents.ScrollPane;
import assignments.assignment4.MainApp;
import assignments.assignment4.Template.Card;
import assignments.assignment4.Template.Page;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Scenes.CustomerScene;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class AddRestaurantMenu extends Page {
    public InputWithTextField restaurantName = new InputWithTextField("Restaurant Name");
    public InputWithTextField restaurantDescription = new InputWithTextField("Restaurant Description");
    public InputWithTextField menuName = new InputWithTextField("Menu Name");
    public InputWithTextField menuPrice = new InputWithTextField("Menu Price");

    // For Edit Mode
    public boolean isEditMode = false;
    public static Restaurant editedRestaurant;

    public AddRestaurantMenu() {
        super("Add Restaurant", true, AdminScene.adminSceneDashboard);

        RightSide rightSide = new RightSide(this);
        LeftSide leftSide = new LeftSide(this, rightSide);

        // Set Witdh to Be 50% of parent
        leftSide.prefWidthProperty().bind(mainBox.widthProperty().divide(2));
        rightSide.prefWidthProperty().bind(mainBox.widthProperty().divide(2));

        mainBox.getChildren().addAll(leftSide, rightSide);
    }

    public AddRestaurantMenu(Restaurant editedRestaurant) {
        super("Edit " + editedRestaurant.getNama(), true, AdminScene.restaurantDetailMenu);
        this.isEditMode = true;
        AddRestaurantMenu.editedRestaurant = editedRestaurant;

        RightSide rightSide = new RightSide(this);
        LeftSide leftSide = new LeftSide(this, rightSide);

        // Set Witdh to Be 50% of parent
        leftSide.prefWidthProperty().bind(mainBox.widthProperty().divide(2));
        rightSide.prefWidthProperty().bind(mainBox.widthProperty().divide(2));

        // Set Default
        restaurantName.textField.setText(editedRestaurant.getNama());
        restaurantDescription.textField.setText(editedRestaurant.getDescription());

        // Add Menu to RightSide
        for (Menu menu : editedRestaurant.getMenu()) {
            rightSide.handleCreateMenu(menu.getNamaMakanan(), String.valueOf(menu.getHarga()));
        }

        mainBox.getChildren().addAll(leftSide, rightSide);
    }
}

class LeftSide extends Card {
    private AddRestaurantMenu addRestaurantMenu;
    private RightSide rightSide;

    public LeftSide(AddRestaurantMenu addRestaurantMenu, RightSide rightSide) {
        super();
        this.addRestaurantMenu = addRestaurantMenu;
        this.rightSide = rightSide;

        // Set Width
        addRestaurantMenu.restaurantName.setPrefWidth(300);
        addRestaurantMenu.restaurantDescription.setPrefWidth(300);

        // Make Save Button
        Button saveButton = new Button("Save", Button.Variant.PRIMARY);
        saveButton.setMaxWidth(Double.MAX_VALUE);

        try {
            Image saveIcon = new Image(new FileInputStream(MainApp.imageDirectory + "save.png"));
            ImageView saveIconView = new ImageView(saveIcon);
            saveIconView.setFitWidth(15);
            saveIconView.setFitHeight(15);
            saveButton.setGraphic(saveIconView);
        } catch (FileNotFoundException e) {
            return;
        }

        // Handle Save Button
        saveButton.setOnAction(e -> {
            if (addRestaurantMenu.isEditMode) {
                editRestaurant();
            }
            else {
                createNewRestaurant();
            }
        });

        // Maximize LeftSide Width
        this.prefWidthProperty().bind(this.widthProperty());

        this.setSpacing(20);
        this.getChildren().addAll(addRestaurantMenu.restaurantName, addRestaurantMenu.restaurantDescription,
                saveButton);
    }

    private void editRestaurant() {
        if (addRestaurantMenu.restaurantName.getTextFieldText().isEmpty()
                || addRestaurantMenu.restaurantDescription.getTextFieldText().isEmpty()) {
            System.out.println("Restaurant Name or Restaurant Description is Empty");
            return;
        }

        // Validate Restaurant Name
        if (!Restaurant.validateRestaurantName(addRestaurantMenu.restaurantName.getTextFieldText())) {
            System.out.println("Restaurant Name is Invalid");
            return;
        }

        // Set new data to Edited Resto
        AddRestaurantMenu.editedRestaurant.setNama(
                addRestaurantMenu.restaurantName.getTextFieldText()
        );

        AddRestaurantMenu.editedRestaurant.setDescription(
                addRestaurantMenu.restaurantDescription.getTextFieldText()
        );

        // Add Edited Menu List
        AddRestaurantMenu.editedRestaurant.getMenu().clear();
        for (String menuName : rightSide.temporaryMenuList.keySet()) {
            Menu newMenu = new Menu(menuName, rightSide.temporaryMenuList.get(menuName).menu.getHarga());
            AddRestaurantMenu.editedRestaurant.addMenu(newMenu);
        }

        // Clear Input
        addRestaurantMenu.restaurantName.textField.clear();
        addRestaurantMenu.restaurantDescription.textField.clear();
        addRestaurantMenu.menuName.textField.clear();
        addRestaurantMenu.menuPrice.textField.clear();

        // Clear Menu List and Remove Components
        rightSide.temporaryMenuList.clear();

        Alert successAlert = alert("Success", "Restaurant has been edited");

        // Check the response of User is Ok or not
        AdminScene.clearMenu();
        AdminScene.restaurantDetailMenu = new RestaurantDetailMenu(AddRestaurantMenu.editedRestaurant.getNama());
        AdminScene.mainContainer.getChildren().add(AdminScene.restaurantDetailMenu);
    }
    private void createNewRestaurant() {
        // Input Validation
        if (addRestaurantMenu.restaurantName.getTextFieldText().isEmpty()
                || addRestaurantMenu.restaurantDescription.getTextFieldText().isEmpty()) {
            System.out.println("Restaurant Name or Restaurant Description is Empty");
            return;
        }

        // Validate Restaurant Name
        if (!Restaurant.validateRestaurantName(addRestaurantMenu.restaurantName.getTextFieldText())) {
            System.out.println("Restaurant Name is Invalid");
            return;
        }

        // Create Restaurant
        Restaurant newResto = new Restaurant(addRestaurantMenu.restaurantName.getTextFieldText());
        newResto.setDescription(addRestaurantMenu.restaurantDescription.getTextFieldText());
        MainMenu.restoList.add(newResto);
        CustomerScene.searchRestaurantObservableList.add(newResto);

        // Add Resto Menu
        for (String menuName : rightSide.temporaryMenuList.keySet()) {
            Menu newMenu = new Menu(menuName, rightSide.temporaryMenuList.get(menuName).menu.getHarga());
            newResto.addMenu(newMenu);
        }

        // Clear Input
        addRestaurantMenu.restaurantName.textField.clear();
        addRestaurantMenu.restaurantDescription.textField.clear();
        addRestaurantMenu.menuName.textField.clear();
        addRestaurantMenu.menuPrice.textField.clear();

        // Clear Menu List and Remove Components
        rightSide.temporaryMenuList.clear();
        rightSide.menuListContainer.getChildren().clear();

        // Alert Success Message
        alert("Success", "Restaurant has been added");
    }

    private Alert alert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Set to null if you don't want a header text
        alert.setContentText(content);

        // Show the alert
        alert.showAndWait();

        return alert;
    }
}

class RightSide extends Card {
    VBox menuListContainer = new VBox();
    HashMap<String, MenuItem> temporaryMenuList = new HashMap<>();
    void alertInvalid(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null); // Set to null if you don't want a header text
        alert.setContentText(message);

        // Show the alert
        alert.showAndWait();
    }

    public RightSide(AddRestaurantMenu addRestaurantMenu) {
        super("Add Menu");

        GridPane menuFormContainer = new GridPane();
        menuFormContainer.prefWidthProperty().bind(this.widthProperty());
        menuFormContainer.setHgap(20);

        // Setup Column Constraint
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(33);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(33);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(33);
        menuFormContainer.getColumnConstraints().addAll(column1, column2, column3);

        // Make Add Menu Button
        Button addMenuButton = new Button("Add", Button.Variant.PRIMARY);

        try {
            Image plusIcon = new Image(new FileInputStream(MainApp.imageDirectory + "plus.png"));
            ImageView plusIconView = new ImageView(plusIcon);
            plusIconView.setFitWidth(15);
            plusIconView.setFitHeight(15);
            addMenuButton.setGraphic(plusIconView);
        } catch (FileNotFoundException e) {
            return;
        }

        addMenuButton.setMaxWidth(Double.MAX_VALUE);

        // Handle Add Menu Button
        addMenuButton.setOnAction(e -> {
            handleCreateMenu(addRestaurantMenu.menuName.getTextFieldText(),
                    addRestaurantMenu.menuPrice.getTextFieldText());
        });

        // Add to Menu Form Container
        menuFormContainer.add(addRestaurantMenu.menuName, 0, 0);
        menuFormContainer.add(addRestaurantMenu.menuPrice, 1, 0);
        menuFormContainer.add(addMenuButton, 2, 0);

        // Set Allignment to Bottom
        menuFormContainer.setAlignment(Pos.BOTTOM_CENTER);

        // Maximize RightSide Width
        this.prefWidthProperty().bind(this.widthProperty());

        // Scroll pane
        ScrollPane scrollPane = new ScrollPane(menuListContainer);
        scrollPane.setPrefHeight(300);

        this.getChildren().addAll(menuFormContainer, scrollPane);
    }

    void handleDeleteMenu(String menuName) {
        Platform.runLater(() -> {
            // Remove from Menu List Container
            menuListContainer.getChildren().remove(temporaryMenuList.get(menuName));

            // Remove from Temporary Menu List
            temporaryMenuList.remove(menuName);
        });

    }

    void handleCreateMenu(String menuName, String menuPrice) {
        // Input Validation
        if (menuName.isEmpty() || menuPrice.isEmpty()) {
            alertInvalid("Menu Name or Menu Price is Empty");
            return;
        }

        // Check if MenuPrice is Not a Number
        try {
            Double.parseDouble(menuPrice);
        } catch (NumberFormatException e) {
            alertInvalid("Menu Price is not a number");
            return;
        }

        // Check if menuName already exists
        if (temporaryMenuList.containsKey(menuName)) {
            alertInvalid("Menu Name already exists");
            return;
        }

        // Create Menu Item Box
        MenuItem menuItem = new MenuItem(new Menu(menuName, Double.parseDouble(menuPrice)));
        menuItem.setAlignment(Pos.CENTER_LEFT);

        // Add Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        // Add Delete Button
        Button deleteButton = new Button("", Button.Variant.DESTRUCTIVE);

        // Add Image to DeleteButton
        try {
            Image trashIcon = new Image(new FileInputStream(MainApp.imageDirectory + "trash.png"));
            ImageView trashIconView = new ImageView(trashIcon);
            trashIconView.setFitWidth(15);
            trashIconView.setFitHeight(15);
            deleteButton.setGraphic(trashIconView);
        } catch (FileNotFoundException e) {
            return;
        }

        menuItem.getChildren().add(spacer);
        menuItem.getChildren().add(deleteButton);

        deleteButton.setOnAction(e -> {
            handleDeleteMenu(menuName);
        });

        Platform.runLater(() -> {
            // Add to Menu List Container
            menuListContainer.getChildren().add(menuItem);

            // Add Database to Temporary Menu List
            temporaryMenuList.put(menuName, menuItem);
        });
    }
}
