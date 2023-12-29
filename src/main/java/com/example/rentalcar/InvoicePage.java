package com.example.rentalcar;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvoicePage {

    public static void displayInvoice(String brand, String model, String days) {
        Stage invoiceStage = new Stage();
        invoiceStage.setTitle("Invoice");

        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Car Rental Invoice"));
        layout.getChildren().add(new Label("Brand: " + brand));
        layout.getChildren().add(new Label("Model: " + model));
        layout.getChildren().add(new Label("Rental Days: " + days));

        // Add your price calculation logic here
        double price = calculatePrice(brand, model, days);
        layout.getChildren().add(new Label("Total Price: " + price));

        Scene scene = new Scene(layout, 300, 200);
        invoiceStage.setScene(scene);
        invoiceStage.show();
    }

    private static double calculatePrice(String brand, String model, String days) {
        double dailyRate = 500; // Change this according to your pricing logic
        int rentalDays = Integer.parseInt(days);
        return dailyRate * rentalDays;
    }
}
