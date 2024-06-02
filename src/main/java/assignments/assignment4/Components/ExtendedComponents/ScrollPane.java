package assignments.assignment4.Components.ExtendedComponents;

import javafx.scene.Node;

public class ScrollPane extends javafx.scene.control.ScrollPane {
    public ScrollPane(Node content) {
        this.setStyle("-fx-background-color: #FFF; -fx-background: #FFF;");
        this.setContent(content);

        this.setFitToWidth(true);
        this.setFitToHeight(true);

        this.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);

        this.setPannable(true);

        this.setHmax(0);

        this.setHvalue(0);

        this.setVmax(1);

        this.setVvalue(0);
    }
}
