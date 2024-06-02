package assignments.assignment4.Pages.Order;

import assignments.assignment2.Menu;
import assignments.assignment2.Order;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment4.Components.ExtendedComponents.Button;
import assignments.assignment4.Components.ExtendedComponents.ScrollPane;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.MainApp;
import assignments.assignment4.Scenes.AdminScene;
import assignments.assignment4.Template.Card;
import assignments.assignment4.Template.Page;
import assignments.assignment4.Scenes.CustomerScene;
import assignments.assignment4.Utils.ThousandFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static assignments.assignment4.MainApp.refreshPage;

public class OrderDetailPage extends Page {
    Order currentOrder;

    public OrderDetailPage(Order order) {
        super(order == null ? "" : order.getOrderId() , true, CustomerScene.customerSceneDashboard);
        if (order == null) {
            return;
        }

        this.currentOrder = order;

        // Order Card
        OrderCard orderCard = new OrderCard(currentOrder);

        // Bill Card
        BillCard billCard = new BillCard(currentOrder);

        // Set Height To Max
        orderCard.prefHeightProperty().bind(mainBox.heightProperty());

        // Divide into 3 sections
        orderCard.prefWidthProperty().bind(mainBox.widthProperty().divide(3));
        billCard.prefWidthProperty().bind(mainBox.widthProperty().divide(3));

        mainBox.getChildren().add(orderCard);
        mainBox.getChildren().add(billCard);
    }
}

class OrderCard extends Card {
    Order currentOrder;
    public OrderCard(Order order) {
        super("Your Orders");
        if (order == null) {
            return;
        }
        this.currentOrder = order;

        // Status Text
        Text statusText = new Text("Status: " + (order.getOrderFinished() ? "Finished" : "On Progress"), 15);
        statusText.setFont(Font.font("Manrope", FontWeight.BOLD, 15));

        if (order.getOrderFinished()) {
            statusText.setFill(javafx.scene.paint.Color.web("#4CAF50"));
        }
        else {
            statusText.setFill(javafx.scene.paint.Color.web("#FB7373"));
        }

        // Add Status Text to VBox
        this.getChildren().add(statusText);

        VBox orderList = new VBox();

        // Scroll Pane
        ScrollPane scrollPane = new ScrollPane(orderList);
        scrollPane.setPrefHeight(300);
        this.getChildren().add(scrollPane);

        // Add Order Items
        for (Menu menu : order.getItems()) {
            OrderItem orderItem = new OrderCard.OrderItem(menu);
            orderList.getChildren().add(orderItem);
        }

        // Total Price
        Text totalPriceText = new Text("Total Price: Rp " + ThousandFormatter.formatWithThousandSeparator(calculateTotalPrice()), 20);
        totalPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 20));
        totalPriceText.setFill(javafx.scene.paint.Color.web("#FB7373"));

        // Add Total Price to VBox
        this.getChildren().add(totalPriceText);

        // HBox for Buttons
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(10);

        // Set Button Container to Max Width
        buttonContainer.prefWidthProperty().bind(this.widthProperty());

        // Clear Button
        Button cancelButton = new Button("Cancel Order", Button.Variant.DESTRUCTIVE);
        cancelButton.prefWidthProperty().bind(buttonContainer.widthProperty().divide(2));

        // Add cancel Button Event
        cancelButton.setOnAction(e -> {
            // Alert Confirmation
            Alert alert = MainApp.alertMessage("Order Canceled", "Want to cancel your order?");
            alert.setAlertType(Alert.AlertType.INFORMATION);

            // Check if User Say Yes
            if (alert.getResult().getText().equalsIgnoreCase("OK")) {
                CustomerScene.orderObservableList.set(CustomerScene.orderObservableList.indexOf(currentOrder), currentOrder);

                // Delete Order
                CustomerScene.orderObservableList.remove(currentOrder);

                // Back to Page
                CustomerScene.clearMenu();
                CustomerScene.mainContainer.getChildren().add(CustomerScene.customerSceneDashboard);
            }
        });

        // Order Button
        Button payButton = new Button("Pay", Button.Variant.PRIMARY);
        payButton.prefWidthProperty().bind(buttonContainer.widthProperty().divide(2));

        // Add Order Button Event
        payButton.setOnAction(e -> {
            PaymentDialog dialog = new PaymentDialog();
            dialog.showAndWait().ifPresent(result -> {
                DepeFoodPaymentSystem currentUserPayment = CustomerSystemCLI.currentUserLoggedIn.getPayment();
                double totalBiaya = currentOrder.getTotalHarga();

                boolean isDebitCard = result.equalsIgnoreCase("Debit Card");
                boolean isCreditCard = result.equalsIgnoreCase("Credit Card");

                if (isCreditCard && currentUserPayment instanceof CreditCardPayment) {
                    // If User Balance is Not Enough
                    if (CustomerSystemCLI.currentUserLoggedIn.getSaldo() < totalBiaya) {
                        alert("Insufficient Balance", "Your balance is not enough to pay this order", Alert.AlertType.ERROR);
                    }

                    else if (currentUserPayment.processPayment((long) totalBiaya, CustomerSystemCLI.currentUserLoggedIn,
                            currentOrder.getRestaurant())) {
                        // Set Balance Text
                        CustomerScene.balanceAmount.setText(ThousandFormatter.formatWithThousandSeparator(CustomerSystemCLI.currentUserLoggedIn.getSaldo()));

                        // Set order to finish
                        currentOrder.setOrderFinished(true);

                        // Alert Success
                        Alert alert = alert("Payment Success", "Your order has been paid successfully", Alert.AlertType.INFORMATION);

                        if (alert.getResult().getText().equalsIgnoreCase("OK")) {
                            CustomerScene.orderObservableList.set(CustomerScene.orderObservableList.indexOf(currentOrder), currentOrder);
                            CustomerScene.clearMenu();
                            CustomerScene.mainContainer.getChildren().add(CustomerScene.customerSceneDashboard);
                        }
                    }
                    else {
                        alert("Payment Failed", "Your payment has been failed", Alert.AlertType.ERROR);
                    }
                }

                // Jika user memilih Debit Card dan user memiliki metode pembayaran Debit Card
                else if (isDebitCard && currentUserPayment instanceof DebitPayment) {
                    if (totalBiaya < 50000) {
                        alert("Minimum Payment", "Minimum payment for Debit Card is Rp 50.000", Alert.AlertType.ERROR);
                    }
                    // If User Balance is Not Enough
                    if (CustomerSystemCLI.currentUserLoggedIn.getSaldo() < totalBiaya) {
                        alert("Insufficient Balance", "Your balance is not enough to pay this order", Alert.AlertType.ERROR);
                    }

                    else if (currentUserPayment.processPayment((long) totalBiaya, CustomerSystemCLI.currentUserLoggedIn,
                            currentOrder.getRestaurant())) {
                        CustomerScene.balanceAmount.setText(ThousandFormatter.formatWithThousandSeparator(CustomerSystemCLI.currentUserLoggedIn.getSaldo()));

                        // Set order to finish
                        currentOrder.setOrderFinished(true);

                        // Alert Success
                        Alert alert = alert("Payment Success", "Your order has been paid successfully", Alert.AlertType.INFORMATION);

                        if (alert.getResult().getText().equalsIgnoreCase("OK")) {
                            CustomerScene.orderObservableList.set(CustomerScene.orderObservableList.indexOf(currentOrder), currentOrder);
                        }
                    }
                    else {
                        alert("Payment Failed", "Your payment has been failed", Alert.AlertType.ERROR);
                    }
                }

                // Jika user tidak memiliki payment tersebut
                else {
                    alert("Payment Failed", "User belum memiliki metode pembayaran ini!", Alert.AlertType.ERROR);
                }
            });
        });

        // Add Button to HBox
        buttonContainer.getChildren().addAll(cancelButton, payButton);

        if (!currentOrder.getOrderFinished()) {
            // Add Button Container to VBox
            this.getChildren().add(buttonContainer);
        }

    }

    Alert alert(String title, String content, Alert.AlertType variant) {
        Alert alert = new Alert(variant);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();

        return alert;
    }

    double calculateTotalPrice() {
        return currentOrder.getTotalHarga();
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
            Text menuPriceText = new Text("Rp " + ThousandFormatter.formatWithThousandSeparator(menu.getHarga()), 14);
            menuPriceText.setFont(Font.font("Manrope", FontWeight.BOLD, 14));

            // Add Text to HBox
            this.getChildren().addAll(menuNameText, menuQuantityText, menuPriceText);


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
}

class BillCard extends Card {
    public BillCard(Order order) {
        super("Your Bill");

        Text orderBill = new Text(order.toString(), 12);
        orderBill.setFont(Font.font("Manrope", FontWeight.BOLD, 12));

        this.getChildren().addAll(orderBill);

        // Clipboard Container
        VBox clipboardContainer = new VBox();
        clipboardContainer.prefHeightProperty().bind(this.heightProperty());

        // Copy to Clipboard Button
        Button copyToClipboardButton = new Button("Copy to Clipboard", Button.Variant.PRIMARY);
        copyToClipboardButton.prefWidthProperty().bind(this.widthProperty());

        // Add Copy to Clipboard Event
        copyToClipboardButton.setOnAction(e -> {
            // Copy to Clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(order.toString());
            clipboard.setContent(content);

            // ALert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Bill Copied to Clipboard");
            alert.showAndWait();
        });

        // Set Button to Bottom Position
        clipboardContainer.setAlignment(Pos.BOTTOM_CENTER);

        // Add Copy to Clipboard Button to VBox
        clipboardContainer.getChildren().add(copyToClipboardButton);

        this.getChildren().add(clipboardContainer);
    }
}

class PaymentDialog extends Dialog<String> {
    public PaymentDialog() {
        setTitle("Payment Method");
        setHeaderText("Choose your payment method");

        ButtonType debitButtonType = new ButtonType("Debit Card", ButtonBar.ButtonData.OK_DONE);
        ButtonType creditButtonType = new ButtonType("Credit Card", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().addAll(debitButtonType, creditButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == debitButtonType) {
                return "Debit Card";
            } else if (dialogButton == creditButtonType) {
                return "Credit Card";
            }
            return null;
        });
    }
}