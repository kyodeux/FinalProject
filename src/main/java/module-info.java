module com.mycompany.finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.finalproject to javafx.fxml;
    exports com.mycompany.finalproject;
}
