package assignments.assignment4.Pages.Restaurant;

import assignments.assignment2.Menu;
import assignments.assignment2.Order;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ImageView;
import assignments.assignment4.Components.ExtendedComponents.ScrollPane;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.MainApp;
import assignments.assignment4.Pages.Order.OrderDetailPage;
import assignments.assignment4.Scenes.CustomerScene;
import assignments.assignment4.Template.Page;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Utils.ThousandFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class RestaurantDetailMenu extends Page {
    Restaurant currentRestaurant;
    public static Text balanceAmount = new Text("", 20);

    public RestaurantDetailMenu(String restaurantName) {
        super(restaurantName, true, AdminScene.adminSceneDashboard);

        // Find Restaurant
        for (Restaurant resto : MainMenu.restoList) {
            if (resto.getNama().equals(restaurantName)) {
                this.currentRestaurant = resto;
                break;
            }
        }

        // Create Description Text
        if (currentRestaurant == null) {
            return;
        }

        Text description = new Text(currentRestaurant.getDescription(), 20);
        // SetStyle
        description.setStyle("-fx-font-weight: 300; -fx-text-fill: #6C757D");
        description.setFont(Font.font("Manrope", FontWeight.NORMAL, 12));
        header.getChildren().add(description);

        // HBox to Contain Button
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(10);

        // Delete Resto Button
        Button deleteButton = new Button("", Button.Variant.DESTRUCTIVE);

        // Set Icon
        try {
            Image deleteIcon = new Image(new FileInputStream(MainApp.imageDirectory + "trash.png"));
            ImageView deleteIconView = new ImageView(deleteIcon);

            deleteIconView.setFitHeight(20);
            deleteIconView.setFitWidth(20);

            deleteButton.setGraphic(deleteIconView);
        } catch (FileNotFoundException e) {
            return;
        }


        deleteButton.setOnAction(e -> {
            // Alert Confitmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this restaurant?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait();

            // Check User Answer
            if (alert.getResult().getText().equals("OK")) {
                MainMenu.restoList.remove(currentRestaurant);
                CustomerScene.searchRestaurantObservableList.remove(currentRestaurant);
                AdminScene.clearMenu();
                AdminScene.mainContainer.getChildren().add(AdminScene.adminSceneDashboard);
            }
        });

        buttonContainer.getChildren().add(deleteButton);

        // Edit Restaurant Button
        Button editButton = new Button("", Button.Variant.PRIMARY);

        try {
            Image editIcon = new Image(new FileInputStream(MainApp.imageDirectory + "pencil.png"));
            ImageView editIconView = new ImageView(editIcon);

            editIconView.setFitHeight(20);
            editIconView.setFitWidth(20);

            editButton.setGraphic(editIconView);
        } catch (FileNotFoundException e) {
            return;
        }

        editButton.setOnAction(e -> {
            AdminScene.clearMenu();
            AdminScene.editRestaurantMenu = new AddRestaurantMenu(currentRestaurant);
            AdminScene.mainContainer.getChildren().add(AdminScene.editRestaurantMenu);
        });

        buttonContainer.getChildren().add(editButton);

        header.getChildren().add(buttonContainer);

        // Add Menu List
        MenuListView menuListView = new MenuListView(this);

        // Left Side Container
        VBox leftContainer = new VBox();
        leftContainer.setSpacing(20);

        // Add Balance Card
        BalanceCard balanceCard = new BalanceCard(currentRestaurant);
        leftContainer.getChildren().add(balanceCard);

        // Add Menu List
        leftContainer.getChildren().add(menuListView);

        // Add Left Container to Main Box
        mainBox.getChildren().add(leftContainer);

        // Add OrderList View
        OrderListCard orderListCard = new OrderListCard(this.currentRestaurant);

        // Add OrderList View
        menuListView.prefWidthProperty().bind(mainBox.widthProperty().divide(2));
        orderListCard.prefWidthProperty().bind(mainBox.widthProperty().divide(2));

        mainBox.getChildren().add(orderListCard);
    }
}

class MenuListView extends VBox {
    public MenuListView(RestaurantDetailMenu restaurantDetailMenu) {
        setSpacing(10);
        this.setStyle(
                "-fx-background-color: #FFF; -fx-border-color: #E8E8E8; -fx-border-radius: 20px; -fx-padding: 40px; -fx-background-radius: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Text title = new Text("Menu List", 20);
        title.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        this.getChildren().add(title);

        if (restaurantDetailMenu.currentRestaurant == null) {
            return;
        }

        VBox menuList = new VBox();

        for (Menu menu : restaurantDetailMenu.currentRestaurant.getMenu()) {
            MenuItem orderItem = new MenuItem(menu);
            menuList.getChildren().add(orderItem);
        }

        ScrollPane scrollPane = new ScrollPane(menuList);
        scrollPane.setPrefHeight(300);

        this.getChildren().add(scrollPane);

        this.prefWidthProperty().bind(this.widthProperty());
    }

}

class MenuItem extends HBox {
    Menu menu;
    MenuItem (Menu menu) {
        super();
        this.menu = menu;
        this.setStyle(
                "-fx-background-color: #FCFCFC; -fx-border-radius: 10px; -fx-padding: 20px; -fx-background-radius: 5");
        this.setSpacing(10);

        // Set Width to Max
        this.prefWidthProperty().bind(this.widthProperty());

        // Set Menu Name
        Text menuNameText = new Text(menu.getNamaMakanan(), 14);
        menuNameText.setFont(Font.font("Manrope", FontWeight.BOLD, 14));

        // Set Menu Name Max Width
        menuNameText.setWrappingWidth(150);

        // Space Maximize
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Set Menu Price
        Text menuPriceText = new Text("Rp " + ThousandFormatter.formatWithThousandSeparator(menu.getHarga()), 14);
        menuPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 14));

        // Add Text to HBox
        this.getChildren().addAll(menuNameText, menuPriceText);


        // HOVER ANIMATION
        this.setOnMouseEntered(e -> {
            this.setStyle(
                    "-fx-background-color: #F6F8FF; -fx-border-radius: 10px; -fx-padding: 20px; -fx-background-radius: 5");
        });

        // If Hover Out
        this.setOnMouseExited(e -> {
            this.setStyle(
                    "-fx-background-color: #FCFCFC; -fx-border-radius: 10px; -fx-padding: 20px; -fx-background-radius: 5");
        });
    }
}

class BalanceCard extends VBox {
    BalanceCard (Restaurant currentRestaurant) {
        super();
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-border-color: #ECEFF7; -fx-border-width: 1; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Text restaurantBalanceText = new Text("Restaurant Balance", 20);
        restaurantBalanceText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Balance HBox
        HBox balanceHBox = new HBox();
        balanceHBox.setSpacing(10);
        balanceHBox.setAlignment(Pos.CENTER_LEFT);

        // Rp Text
        Text rpText = new Text("RP", 20);
        rpText.setFill(javafx.scene.paint.Color.valueOf("#95979D"));
        rpText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Balance Amount
        RestaurantDetailMenu.balanceAmount = new Text(ThousandFormatter.formatWithThousandSeparator(currentRestaurant.getSaldo()), 50);
        RestaurantDetailMenu.balanceAmount.setFill(javafx.scene.paint.Color.valueOf("#576BEA"));
        RestaurantDetailMenu.balanceAmount.setFont(Font.font("Manrope", FontWeight.EXTRA_BOLD, 50));

        balanceHBox.getChildren().addAll(rpText, RestaurantDetailMenu.balanceAmount);

        this.getChildren().addAll(restaurantBalanceText, balanceHBox);
    }
}

// Still on Working
class OrderListCard extends VBox {
    Restaurant currentRestaurant;
    OrderListCard(Restaurant currentRestaurant) {
        super();
        this.currentRestaurant = currentRestaurant;
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-border-color: #ECEFF7; -fx-border-width: 1; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Order History Text
        Text orderHistoryText = new Text("Order History", 20);
        orderHistoryText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Order List
        VBox orderList = new VBox();

        // Add Text to Box
        this.getChildren().addAll(orderHistoryText, orderList);

        // if Order is Empty
        if (CustomerScene.orderObservableList.isEmpty()) {
            Text emptyOrderText = new Text("You don't have any orders yet", 15);
            emptyOrderText.setFill(javafx.scene.paint.Color.valueOf("#C5C9D3"));
            emptyOrderText.setFont(Font.font("Manrope", FontWeight.BOLD, 15));
            orderList.getChildren().add(emptyOrderText);
        }

        // Iterate over Order List
        for (Order order : CustomerScene.orderObservableList) {
            if (order.getRestaurant().equals(currentRestaurant) && order.getOrderFinished()) {
                OrderItem orderItem = new OrderItem(CustomerScene.orderObservableList.indexOf(order) + 1, order);
                orderList.getChildren().add(orderItem);
            }
        }
    }
}

class OrderItem extends GridPane {
    Order order;
    OrderItem(int index, Order order) {
        super();
        this.order = order;
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #FCFCFC; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Column Constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(10);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        ColumnConstraints column3 = new ColumnConstraints();

        this.getColumnConstraints().addAll(column1, column2, column3);

        // Number Icon
        Text numberIcon = new Text(String.valueOf(index));
        numberIcon.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Order Detail VBox
        VBox orderDetail = new VBox();

        // Order ID
        Text orderID = new Text(order.getOrderId(), 15);
        orderID.setFont(Font.font("Manrope", FontWeight.BOLD, 15));

        // Order Restaurant
        Text orderRestaurant = new Text(order.getRestaurant().getNama(), 15);
        orderRestaurant.setFont(Font.font("Manrope", FontWeight.BOLD, 15));

        orderRestaurant.setFill(javafx.scene.paint.Color.valueOf("#C5C9D3"));

        // Add to Order Detail
        orderDetail.getChildren().addAll(orderID, orderRestaurant);

        // Total Price of The Order Text
        Text totalPriceText = new Text("+Rp" + order.getTotalHarga(), 15);
        totalPriceText.setFill(javafx.scene.paint.Color.valueOf("#4CAF50"));

        totalPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 15));

        this.add(numberIcon, 0, 0);
        this.add(orderDetail, 1, 0);
        this.add(totalPriceText, 2, 0);

        // Hover Animation
        this.setOnMouseEntered(e -> {
            this.setStyle("-fx-background-color: #F6F6F6; -fx-border-radius: 10; -fx-background-radius: 10;");
        });

        this.setOnMouseExited(e -> {
            this.setStyle("-fx-background-color: #FCFCFC; -fx-border-radius: 10; -fx-background-radius: 10;");
        });

        // Click Handler
        this.setOnMouseClicked(e -> {
            System.out.println(order.getOrderId());
            CustomerScene.clearMenu();
            CustomerScene.orderDetailPage = new OrderDetailPage(order);
            CustomerScene.mainContainer.getChildren().add(CustomerScene.orderDetailPage);
            CustomerScene.orderDetailPage.prefWidthProperty().bind(CustomerScene.mainContainer.widthProperty());
        });
    }
}



