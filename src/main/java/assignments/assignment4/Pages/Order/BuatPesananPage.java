package assignments.assignment4.Pages.Order;

import assignments.assignment1.OrderGenerator;
import assignments.assignment2.Menu;
import assignments.assignment2.Order;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ImageView;
import assignments.assignment4.Components.ExtendedComponents.ScrollPane;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.MainApp;
import assignments.assignment4.Template.Card;
import assignments.assignment4.Template.Page;
import assignments.assignment4.Scenes.CustomerScene;
import assignments.assignment4.Utils.ThousandFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

import static assignments.assignment3.systemCLI.CustomerSystemCLI.currentUserLoggedIn;
import static assignments.assignment4.Pages.Order.BuatPesananPage.currentRestaurant;
import static assignments.assignment4.Pages.Order.BuatPesananPage.menuOrderedList;

public class BuatPesananPage extends Page {
    public static Restaurant currentRestaurant;
    public static ObservableList<Menu> menuOrderedList = FXCollections.observableList(new ArrayList<>());

    public BuatPesananPage(String restaurantName) {
        super(restaurantName, true, CustomerScene.customerSceneDashboard);
        for (Restaurant resto : MainMenu.restoList) {
            if (resto.getNama().equals(restaurantName)) {
                currentRestaurant = resto;
                break;
            }
        }

        if (currentRestaurant == null) {
            return;
        }

        // Add Menu List
        MenuDisplayView menuListView = new MenuDisplayView();

        // Scroll Pane
        ScrollPane scrollPane = new ScrollPane(menuListView);
        scrollPane.setStyle("-fx-background: #FFF; -fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-color: #ECEFF7; -fx-border-width: 1; -fx-padding: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        mainBox.getChildren().add(scrollPane);

        // Add Order Basket
        OrderBasket orderBasket = new OrderBasket();

        // Set menuListView to Max
        menuListView.prefWidthProperty().bind(mainBox.widthProperty());

        // Set Max Width of OrderBasket
        orderBasket.prefWidthProperty().bind(mainBox.widthProperty());
        orderBasket.setMinWidth(400);
        orderBasket.setMaxWidth(400);

        mainBox.getChildren().add(orderBasket);
    }
}
class MenuDisplayView extends GridPane {
    public MenuDisplayView() {
        this.setHgap(10);
        this.setVgap(10);
        this.prefWidthProperty().bind(this.widthProperty());
        this.prefHeightProperty().bind(this.heightProperty());

        int column = 0;
        int row = 0;

        for (Menu menu : currentRestaurant.getMenu()) {
            MenuCard menuCard = new MenuCard(menu);

            // Add To Gridpane
            this.add(menuCard, column, row);

            // Increment Column
            column++;

            // If Column is 3
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}

class MenuCard extends VBox{
    Menu currentMenu;
    public MenuCard(Menu menu) {
        this.currentMenu = menu;
        // Set Style
        this.setStyle(
                "-fx-border-color: #E8E8E8; -fx-background-color: #FFF; -fx-border-radius: 5px; -fx-padding: 30px; -fx-background-radius: 5;");

        // Set Width
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setPrefWidth(200);
        this.setPrefHeight(200);

        Image menuImage;
        try {
            menuImage = new Image(new FileInputStream("assignment4/src/main/java/assignments/assignment4/Assets/MenusImage/steak.png"));
        } catch (FileNotFoundException e) {
            return;
        }
        ImageView imageView = new ImageView(menuImage);

        // Set Image Size
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        boolean imageFound = false;
        for (String image : Objects.requireNonNull(checkMenuImages())) {
            String cleanedImageName = image.replace(".png", "");
            if (menu.getNamaMakanan().toLowerCase().contains(cleanedImageName)) {
                imageFound = true;
                FileInputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream("assignment4/src/main/java/assignments/assignment4/Assets/MenusImage/" + image);
                } catch (FileNotFoundException e) {
                    continue;
                }
                menuImage = new Image(fileInputStream);
                imageView = new ImageView(menuImage);
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                this.getChildren().remove(imageView);
                this.getChildren().add(imageView);
                break;
            }
        }
        if (!imageFound) {
            this.getChildren().add(imageView);
        }

        // Vbox for menu name and price
        VBox menuNamePrice = new VBox();
        menuNamePrice.setSpacing(10);

        // Set Menu Name
        Text menuNameText = new Text(menu.getNamaMakanan(), 20);
        menuNameText.setFont(Font.font("Urbanist", FontWeight.BOLD, 30));

        // Set Menu Price
        Text menuPriceText = new Text("Rp " + ThousandFormatter.formatWithThousandSeparator(menu.getHarga()), 15);
        menuPriceText.setFont(Font.font("Urbanist", FontWeight.BOLD, 20));
        menuPriceText.setFill(javafx.scene.paint.Color.web("#EF4444"));

        // Add Text to VBox
        menuNamePrice.getChildren().addAll(menuNameText, menuPriceText);

        // Add Component to GridPane
        this.getChildren().add(menuNamePrice);

        // HOVER ANIMATION
        this.setOnMouseEntered(e -> {
            this.setStyle(
                    "-fx-border-color: #E8E8E8; -fx-background-color: #F6F8FF; -fx-border-radius: 5px; -fx-padding: 30px; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        });

        // If Hover Out
        this.setOnMouseExited(e -> {
            this.setStyle(
                    "-fx-border-color: #E8E8E8; -fx-background-color: #FFF; -fx-border-radius: 5px; -fx-padding: 30px; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        });

        // Add Click Event
        this.setOnMouseClicked(e -> {
            handleClickCard();
        });
    }

    ArrayList<String> checkMenuImages() {
        ArrayList<String> temp = new ArrayList<>();
        Path dirPath = Paths.get("assignment4/src/main/java/assignments/assignment4/Assets/MenusImage");

        // Use try-with-resources to ensure the stream is closed after usage
        try (Stream<Path> stream = Files.list(dirPath)) {
            stream.forEach(path -> {
                // Check if the path is a file
                if (Files.isRegularFile(path)) {
                    // Print the file name
                    temp.add(path.getFileName().toString());
                }
            });
        } catch (IOException e) {
            return null;
        }

        return temp;
    }

    void handleClickCard() {
        // Add to Order
        Menu foundMenu = null;
        for (Menu menu : menuOrderedList) {
            if (menu.getNamaMakanan().equals(currentMenu.getNamaMakanan())) {
                foundMenu = menu;
                break;
            }
        }

        if (foundMenu != null) {
            Menu addedMenu = new Menu(foundMenu.getNamaMakanan(), foundMenu.getHarga());
            addedMenu.setKuantitas(foundMenu.getKuantitas() + 1);
            menuOrderedList.set(menuOrderedList.indexOf(foundMenu), addedMenu);
        } else {
            Menu addedMenu = new Menu(currentMenu.getNamaMakanan(), currentMenu.getHarga());
            menuOrderedList.add(addedMenu);
        }
    }
}


class OrderBasket extends Card {
    DatePicker datePicker = new DatePicker();
    public OrderBasket() {
        super("Your Orders");
        this.prefWidthProperty().bind(this.widthProperty());
        this.prefHeightProperty().bind(this.heightProperty());

        // Description text
        Text description = new Text("Tap an Order to Remove It");
        description.setFont(Font.font("Manrope", FontWeight.BOLD, 15));
        description.setFill(javafx.scene.paint.Color.web("#969696"));

        this.getChildren().add(description);

        VBox orderList = new VBox();

        // Scroll Pane
        ScrollPane scrollPane = new ScrollPane(orderList);
        scrollPane.setPrefHeight(300);

        this.getChildren().add(scrollPane);

        // Add Order Items
        for (Menu menu : menuOrderedList) {
            OrderItem orderItem = new OrderItem(menu);
            orderList.getChildren().add(orderItem);
        }

        // Total Price
        Text totalPriceText = new Text("Total Price: Rp " + calculateTotalPrice(), 20);
        totalPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));
        totalPriceText.setFill(javafx.scene.paint.Color.web("#FB7373"));

        // Detect Changes in menuOrderedList
        menuOrderedList.addListener((ListChangeListener.Change<? extends Menu> c) -> {
            // Clear Children that instance of OrderItem
            orderList.getChildren().removeIf(node -> node instanceof OrderItem);

            menuOrderedList.forEach(menu -> {
                OrderItem orderItem = new OrderItem(menu);
                orderList.getChildren().add(orderItem);
            });

            // Set Total Price
            totalPriceText.setText("Total Price: Rp " + ThousandFormatter.formatWithThousandSeparator(calculateTotalPrice()));
        });

        // Add Total Price to VBox
        this.getChildren().add(totalPriceText);

        // Add DatePicker
        datePicker.setPromptText("Select Date");

        // Set Default Value to Today
        datePicker.setValue(LocalDate.now());

        // Add DatePicker to VBox
        this.getChildren().add(datePicker);

        // HBox for Buttons
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(10);

        // Set Button Container to Max Width
        buttonContainer.prefWidthProperty().bind(this.widthProperty());

        // Clear Button
        Button clearButton = new Button("Clear", Button.Variant.DESTRUCTIVE);
        clearButton.prefWidthProperty().bind(buttonContainer.widthProperty().divide(2));

        // Add Clear Button Event
        clearButton.setOnAction(e -> {
            for (Menu menu : menuOrderedList) {
                menu.setKuantitas(1);
            }
            menuOrderedList.clear();
        });

        // Order Button
        Button orderButton = new Button("Order", Button.Variant.PRIMARY);
        orderButton.prefWidthProperty().bind(buttonContainer.widthProperty().divide(2));

        // Add Order Button Event
        orderButton.setOnAction(e -> {
            handleMakeOrder();
        });

        // Add Button to HBox
        buttonContainer.getChildren().addAll(clearButton, orderButton);

        // Add Button Container to VBox
        this.getChildren().add(buttonContainer);


    }

    void handleMakeOrder() {
        // Validate
        // Check if Menu is Empty
        if (menuOrderedList.isEmpty()) {
            MainApp.alertMessage("Order Failed", "Pesanan tidak boleh kosong!");
            return;
        }

        // Get Date
        LocalDate today = datePicker.getValue();

        // Format the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        // Generate order id
        String orderID = OrderGenerator.generateOrderID(currentRestaurant.getNama(), formattedDate,
                currentUserLoggedIn.getNomorTelepon());

        // Convert menuOrderedList into Conventional
        Menu[] justifiedMenuList = new Menu[menuOrderedList.size()];

        for (int i = 0; i < menuOrderedList.size(); i++) {
            justifiedMenuList[i] = menuOrderedList.get(i);
        }

        // Membuat order baru
        Order newOrder = new Order(orderID, formattedDate, 0, currentRestaurant, justifiedMenuList,
                currentUserLoggedIn.getLokasi());

        // Menambahkan order ke dalam list order user yang sedang login
        currentUserLoggedIn.addOrder(newOrder);

        CustomerScene.orderObservableList.add(newOrder);

        // Clear
        menuOrderedList.clear();

        // Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Success");
        alert.setHeaderText("Order Success");
        alert.setContentText("Pesanan dengan ID " + orderID + " berhasil diterima!");
        alert.showAndWait();
    }

    double calculateTotalPrice() {
        double totalPrice = 0;
        for (Menu menu : menuOrderedList) {
            totalPrice += menu.getHarga() * menu.getKuantitas();
        }
        return totalPrice;
    }

    private static class OrderItem extends HBox {
        Menu menu;
        OrderItem (Menu menu) {
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

            // Set Menu Quantity
            Text menuQuantityText = new Text(String.valueOf(menu.getKuantitas()), 14);
            menuQuantityText.setFont(Font.font("Manrope", FontWeight.BOLD, 14));
            menuQuantityText.setWrappingWidth(30);

            // Space Maximize
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Set Menu Price
            Text menuPriceText = new Text("Rp " + menu.getHarga(), 14);
            menuPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 14));

            // Add Text to HBox
            this.getChildren().addAll(menuNameText, menuQuantityText, menuPriceText);

            // Add Click Event
            this.setOnMouseClicked(e -> {
                deleteOrder();
            });

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

        void deleteOrder() {
            if (menu.getKuantitas() == 1) {
                menuOrderedList.remove(menu);
            } else {
                menu.setKuantitas(menu.getKuantitas() - 1);
                menuOrderedList.set(menuOrderedList.indexOf(menu), menu);
            }
        }
    }
}


