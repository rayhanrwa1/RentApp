module com.example.rentalcar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rentalcar to javafx.fxml;
    exports com.example.rentalcar;
}