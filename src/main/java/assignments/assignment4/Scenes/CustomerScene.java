package assignments.assignment4.Scenes;

import assignments.assignment2.Order;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment4.Components.ExtendedComponents.Scene;
import assignments.assignment4.Components.ExtendedComponents.ScrollPane;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.Pages.Order.BuatPesananPage;
import assignments.assignment4.Pages.Order.OrderDetailPage;
import assignments.assignment4.Pages.Restaurant.RestaurantDetailMenu;
import assignments.assignment4.Template.Card;
import assignments.assignment4.Template.Navbar;
import assignments.assignment4.Utils.ThousandFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static assignments.assignment3.systemCLI.CustomerSystemCLI.currentUserLoggedIn;

public class CustomerScene {
    public static ObservableList<Order> orderObservableList = FXCollections.observableList(new ArrayList<>());
    public static ObservableList<Restaurant> searchRestaurantObservableList = FXCollections.observableList(new ArrayList<>());

    public static Navbar navbar;
    public static VBox mainContainer = new VBox();
    public static BuatPesananPage buatPesananPage = new BuatPesananPage("");

    public static OrderDetailPage orderDetailPage = new OrderDetailPage(null);

    public static CustomerSceneDashboard customerSceneDashboard = new CustomerSceneDashboard();

    public static ObservableList<Restaurant> restaurantObservableList = FXCollections.observableList(MainMenu.restoList);

    public static Text balanceAmount = new Text("100.000", 50);

    public CustomerScene() {
        try {
            navbar = new Navbar();
        } catch (FileNotFoundException ignored) {
        }

        if (currentUserLoggedIn != null) {
            balanceAmount.setText(ThousandFormatter.formatWithThousandSeparator(currentUserLoggedIn.getSaldo()));
            customerSceneDashboard = new CustomerSceneDashboard();
        }
    }

    public Scene initPage() {
        mainContainer = new VBox();
        mainContainer.setPadding(new Insets(50, 80, 50, 80));

        // Set Max Width
        mainContainer.prefWidthProperty().bind(mainContainer.widthProperty());

        // Setup Navbar
        try {
            navbar = new Navbar();
            mainContainer.getChildren().add(navbar);
        } catch (FileNotFoundException e) {
            return null;
        }

        customerSceneDashboard = new CustomerSceneDashboard();
        customerSceneDashboard.prefWidthProperty().bind(mainContainer.widthProperty());
        mainContainer.getChildren().add(customerSceneDashboard);

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        return new Scene(mainContainer, screenSize.getWidth(), screenSize.getHeight());
    }

    public static void clearMenu() {
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(navbar);
    }
}

class CustomerSceneDashboard extends HBox {

    public CustomerSceneDashboard () {
        super();
        CustomerScene.balanceAmount = new Text("", 50);

        if (currentUserLoggedIn != null) {
            CustomerScene.balanceAmount.setText(ThousandFormatter.formatWithThousandSeparator(currentUserLoggedIn.getSaldo()));
        }

        this.setSpacing(20);
        this.setPadding(new Insets(10));

        LeftSide leftSide = new LeftSide();
        RestaurantListView rightSide = new RestaurantListView();

        leftSide.prefWidthProperty().bind(this.widthProperty().divide(2));
        rightSide.prefWidthProperty().bind(this.widthProperty().divide(2));

        this.getChildren().addAll(leftSide, rightSide);
    }

    private static class LeftSide extends VBox {
        LeftSide() {
            super();
            this.setSpacing(20);

            // Balance Card
            BalanceCard balanceCard = new BalanceCard();

            // Your Orders Card
            YourOrdersCard yourOrdersCard = new YourOrdersCard();

            VBox.setVgrow(yourOrdersCard, Priority.ALWAYS);

            this.getChildren().add(balanceCard);
            this.getChildren().add(yourOrdersCard);
        }
    }
}



class BalanceCard extends Card {
    BalanceCard () {
        super("Your Balance");

        // Balance HBox
        HBox balanceHBox = new HBox();
        balanceHBox.setSpacing(10);
        balanceHBox.setAlignment(Pos.CENTER_LEFT);

        // Rp Text
        Text rpText = new Text("RP", 20);
        rpText.setFill(javafx.scene.paint.Color.valueOf("#95979D"));
        rpText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

        // Balance Amount
        CustomerScene.balanceAmount.setFill(javafx.scene.paint.Color.valueOf("#576BEA"));
        CustomerScene.balanceAmount.setFont(Font.font("Manrope", FontWeight.EXTRA_BOLD, 50));

        balanceHBox.getChildren().addAll(rpText, CustomerScene.balanceAmount);

        this.getChildren().addAll(balanceHBox);
    }
}

class YourOrdersCard extends Card {
    YourOrdersCard() {
        super("Your Orders");

        // Set Height to Fill
        this.prefHeightProperty().bind(this.heightProperty());

        // Order List
        VBox orderList = new VBox();
        orderList.setSpacing(10);

        ScrollPane scrollPane = new ScrollPane(orderList);
        scrollPane.setMinHeight(300);

        // if Order is Empty
        if (CustomerScene.orderObservableList.isEmpty()) {
            Text emptyOrderText = new Text("You don't have any orders yet", 15);
            emptyOrderText.setFill(javafx.scene.paint.Color.valueOf("#C5C9D3"));
            emptyOrderText.setFont(Font.font("Manrope", FontWeight.BOLD, 15));
            orderList.getChildren().add(emptyOrderText);
        }

        // Iterate over Order List
        for (Order order : CustomerScene.orderObservableList) {
            OrderItem orderItem = new OrderItem(CustomerScene.orderObservableList.indexOf(order) + 1, order);
            orderList.getChildren().add(orderItem);
        }

        // Add Listener to Order List
        CustomerScene.orderObservableList.addListener((javafx.collections.ListChangeListener.Change<? extends Order> c) -> {
            orderList.getChildren().clear();
            // Make
            for (Order order : CustomerScene.orderObservableList) {
                OrderItem orderItem = new OrderItem(CustomerScene.orderObservableList.indexOf(order) + 1, order);
                orderList.getChildren().add(orderItem);
            }

            // if Order is Empty
            if (CustomerScene.orderObservableList.isEmpty()) {
                Text emptyOrderText = new Text("You don't have any orders yet", 15);
                emptyOrderText.setFill(javafx.scene.paint.Color.valueOf("#C5C9D3"));
                emptyOrderText.setFont(Font.font("Manrope", FontWeight.BOLD, 15));
                orderList.getChildren().add(emptyOrderText);
            }
        });

        this.getChildren().add(scrollPane);
    }

    static class OrderItem extends GridPane {
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
            Text totalPriceText = new Text("-Rp" + order.getTotalHarga(), 15);

            if (order.getOrderFinished()) {
                totalPriceText.setText("Finished");
                totalPriceText.setFill(javafx.scene.paint.Color.valueOf("#4CAF50"));
            } else {
                totalPriceText.setFill(javafx.scene.paint.Color.valueOf("#FB7373"));
            }

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
}

class RestaurantListView extends Card{
    RestaurantListView() {
        super("Restaurant List");

        // Add Search bar
        HBox searchBar = new HBox();
        searchBar.setSpacing(10);

        // Add Search Input
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search Restaurant");
        searchInput.setStyle("-fx-background-color: #F6F6F6; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 20;");
        searchInput.prefWidthProperty().bind(this.widthProperty());

        searchBar.getChildren().addAll(searchInput);

        // Detect Changes inside Search Input
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            CustomerScene.searchRestaurantObservableList.clear();
            for (Restaurant restaurant : CustomerScene.restaurantObservableList) {
                if (restaurant.getNama().toLowerCase().contains(newValue.toLowerCase())) {
                    CustomerScene.searchRestaurantObservableList.add(restaurant);
                }
            }
        });


        VBox restaurantList = new VBox();
        restaurantList.setSpacing(10);

        // Add Restaurant List
        ScrollPane scrollPane = new ScrollPane(restaurantList);
        scrollPane.prefHeightProperty().bind(this.heightProperty());
        scrollPane.setMinHeight(400);

        for (Restaurant restaurant : CustomerScene.searchRestaurantObservableList) {
            RestaurantItem restaurantItem = new RestaurantItem(restaurant);
            restaurantList.getChildren().add(restaurantItem);
        }

        CustomerScene.searchRestaurantObservableList.addListener((javafx.collections.ListChangeListener.Change<? extends Restaurant> c) -> {
            restaurantList.getChildren().clear();
            // Make
            for (Restaurant restaurant : CustomerScene.searchRestaurantObservableList) {
                RestaurantItem restaurantItem = new RestaurantItem(restaurant);
                restaurantList.getChildren().add(restaurantItem);
            }
        });

        // Add to Right Side
        this.getChildren().addAll(searchBar, scrollPane);
    }

    private static class RestaurantItem extends GridPane {
        Restaurant restaurant;
        RestaurantItem (Restaurant restaurant) {
            super();
            this.restaurant = restaurant;
            this.setPadding(new Insets(30));
            this.setStyle("-fx-background-color: #FCFCFC; -fx-border-radius: 10; -fx-background-radius: 10;");
            this.prefWidthProperty().bind(this.widthProperty());
            this.setHgap(20);

            // Column Constraints
            ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(10);
            ColumnConstraints column2 = new ColumnConstraints();
            column2.setPercentWidth(70);
            ColumnConstraints column3 = new ColumnConstraints();

            this.getColumnConstraints().addAll(column1, column2, column3);

            // Restaurant Icon
            VBox restaurantIcon = new VBox();
            restaurantIcon.setStyle("-fx-background-radius: 1000; -fx-background-color: #EBEBEB");
            restaurantIcon.setAlignment(Pos.CENTER);

            Text restaurantIconText = new Text(restaurant.getNama().substring(0, 1));
            restaurantIconText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));

            restaurantIcon.getChildren().add(restaurantIconText);

            // Restaurant Detail VBox
            VBox restaurantDetail = new VBox();
            restaurantDetail.setSpacing(5);

            // Restaurant Name
            Text restaurantName = new Text(restaurant.getNama(), 15);
            restaurantName.setFont(Font.font("Manrope", FontWeight.BOLD, 15));

            // Restaurant Description
            Text restaurantDescription = new Text(restaurant.getDescription(), 15);
            restaurantDescription.setFont(Font.font("Manrope", FontWeight.MEDIUM, 15));
            restaurantDescription.setFill(javafx.scene.paint.Color.valueOf("#C5C9D3"));

            // Add to Restaurant Detail
            restaurantDetail.getChildren().addAll(restaurantName, restaurantDescription);

            this.setOnMouseClicked(e -> handleClick());

            // Add to GridPane
            this.add(restaurantIcon, 0, 0);
            this.add(restaurantDetail, 1, 0);
        }

        void handleClick() {
            String userRole = currentUserLoggedIn.getRole();
            if (userRole.equalsIgnoreCase("customer")) {
                CustomerScene.clearMenu();
                CustomerScene.buatPesananPage = new BuatPesananPage(restaurant.getNama());
                CustomerScene.mainContainer.getChildren().add(CustomerScene.buatPesananPage);
                CustomerScene.buatPesananPage.prefWidthProperty().bind(CustomerScene.mainContainer.widthProperty());
            }

            else {
                AdminScene.clearMenu();
                RestaurantDetailMenu.balanceAmount = new Text(ThousandFormatter.formatWithThousandSeparator(restaurant.getSaldo()), 50);
                System.out.println("Restaurant Name :" + restaurant.getNama());
                System.out.println(MainMenu.restoList.size());
                AdminScene.restaurantDetailMenu = new RestaurantDetailMenu(restaurant.getNama());
                AdminScene.mainContainer.getChildren().add(AdminScene.restaurantDetailMenu);

                AdminScene.restaurantDetailMenu.prefWidthProperty().bind(AdminScene.mainContainer.widthProperty());
            }
        }

    }
}
