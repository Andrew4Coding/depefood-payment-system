package assignments.assignment4.Pages.Order;

import assignments.assignment2.Order;
import assignments.assignment4.Components.ExtendedComponents.Text;
import assignments.assignment4.Template.Page;
import assignments.assignment4.Scenes.CustomerScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class LihatPesananPage extends Page {
    public static ObservableList<Order> orderObservableList = FXCollections.observableList(new ArrayList<>());

    public LihatPesananPage() {
        super("Your Orders", false, null);

        // Vbox for order list
        VBox orderList = new VBox();
        orderList.setSpacing(20);
        orderList.prefWidthProperty().bind(mainBox.widthProperty());

        Text test = new Text("Hello");
        orderList.getChildren().add(test);

        mainBox.getChildren().add(orderList);

        // Add Order List
        for (Order order : orderObservableList) {
            OrderItemBox orderItemBox = new OrderItemBox(order);
            orderList.getChildren().add(orderItemBox);
        }

        // Set Change detection
        orderObservableList.addListener((javafx.collections.ListChangeListener.Change<? extends Order> c) -> {
            orderList.getChildren().clear();
            // Make
            for (Order order : orderObservableList) {
                System.out.println(order.getOrderId());
                OrderItemBox orderItemBox = new OrderItemBox(order);
                orderList.getChildren().add(orderItemBox);
            }
        });
    }
}

class OrderItemBox extends HBox {
    Order order;

    public OrderItemBox(Order order) {
        this.order = order;

        HBox mainBox = new HBox();
        mainBox.prefWidthProperty().bind(this.widthProperty());
        mainBox.setSpacing(20);

        // Set Style
        this.setStyle(
                "-fx-background-color: #F4F4F4; -fx-padding: 20px; -fx-border-width: 1; -fx-border-color: #E8E8E8;");

        System.out.println("Order ID: " + order.getOrderId());

        Text orderIDText = new Text("Order ID: " + order.getOrderId());
        orderIDText.setFont(Font.font("Manrope", FontWeight.BOLD, 16));
        mainBox.getChildren().add(orderIDText);

        // Set Onclick
        this.setOnMouseClicked(e -> {
            CustomerScene.clearMenu();
            CustomerScene.orderDetailPage = new OrderDetailPage(order);
            CustomerScene.mainContainer.getChildren().add(CustomerScene.orderDetailPage);
            CustomerScene.orderDetailPage.prefWidthProperty().bind(CustomerScene.mainContainer.widthProperty());
        });

        this.getChildren().add(mainBox);
    }
}
