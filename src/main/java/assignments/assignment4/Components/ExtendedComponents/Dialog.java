package assignments.assignment4.Components.ExtendedComponents;

public class Dialog extends javafx.scene.control.Dialog {
    public Dialog() {
        super();
        this.getDialogPane().getStylesheets().add(getClass().getResource("/stylesheets/dialog.css").toExternalForm());
    }
}
