import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CombinedApp extends Application {

    private Map<String, String> userCredentials = new HashMap<>();
    private String currentUser;
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private TextField usernameField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rental Mobil App");
        loadCredentialsFromFile();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(createLoginScene());

        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createLoginScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        GridPane.setConstraints(usernameField, 0, 0);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        GridPane.setConstraints(passwordField, 0, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));
        GridPane.setConstraints(loginButton, 1, 0);

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> handleRegistration(usernameField.getText(), passwordField.getText()));
        GridPane.setConstraints(registerButton, 1, 1);

        grid.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);
        return grid;
    }

    private void handleLogin(String username, String password) {
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            currentUser = username;
            showCarSelection();
        } else {
            showAlert("Error", "Invalid credentials. Please try again.");
        }
    }

    private void handleRegistration(String username, String password) {
        if (!userCredentials.containsKey(username)) {
            userCredentials.put(username, password);
            showAlert("Success", "Registration successful!");
        } else {
            showAlert("Error", "Username already exists. Please choose a different username.");
        }
    }

    private void showCarSelection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label brandLabel = new Label("Select Car Brand:");
        GridPane.setConstraints(brandLabel, 0, 0);

        ComboBox<String> brandComboBox = new ComboBox<>();
        brandComboBox.getItems().addAll("Honda", "Toyota", "Mitsubishi");
        brandComboBox.setPromptText("Select Brand");
        GridPane.setConstraints(brandComboBox, 1, 0);

        Label modelLabel = new Label("Select Car Model:");
        GridPane.setConstraints(modelLabel, 0, 1);

        ComboBox<String> modelComboBox = new ComboBox<>();
        brandComboBox.setOnAction(event -> {
            modelComboBox.getItems().clear();
            switch (brandComboBox.getValue()) {
                case "Honda":
                    modelComboBox.getItems().addAll("Jazz", "Brio", "Civic");
                    break;
                case "Toyota":
                    modelComboBox.getItems().addAll("Alphard", "Innova", "Avanza");
                    break;
                case "Mitsubishi":
                    modelComboBox.getItems().addAll("Pajero", "X Pander", "X force");
                    break;
            }
        });
        modelComboBox.setPromptText("Select Model");
        GridPane.setConstraints(modelComboBox, 1, 1);

        Label daysLabel = new Label("Enter Rental Days:");
        GridPane.setConstraints(daysLabel, 0, 2);

        TextField daysField = new TextField();
        daysField.setPromptText("Enter Days");
        GridPane.setConstraints(daysField, 1, 2);

        Button rentButton = new Button("Rent Car");
        rentButton.setOnAction(event -> rentCar(brandComboBox.getValue(), modelComboBox.getValue(), daysField.getText()));
        GridPane.setConstraints(rentButton, 1, 3);

        grid.getChildren().addAll(brandLabel, brandComboBox, modelLabel, modelComboBox, daysLabel, daysField, rentButton);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(grid);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(mainLayout, 400, 300));
    }

    private void rentCar(String brand, String model, String days) {
        if (brand != null && model != null && !days.isEmpty()) {
            showAlert("Rental Info", "You rented a " + brand + " " + model + " for " + days + " days.");
        } else {
            showAlert("Error", "Please select all options and enter the number of days.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadCredentialsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                userCredentials.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        saveCredentialsToFile();
    }

    private void saveCredentialsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}