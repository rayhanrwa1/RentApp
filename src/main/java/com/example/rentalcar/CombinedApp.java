package com.example.rentalcar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.UUID;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.geometry.Pos;

/**
 * Aplikasi Rental Mobil yang memungkinkan pengguna untuk login, registrasi,
 * memilih mobil untuk disewa, dan melihat informasi pembayaran.
 *
 * <p>
 * Fitur utama:
 * - Login dengan username dan password.
 * - Registrasi pengguna baru dengan verifikasi password.
 * - Memilih mobil berdasarkan merek, model, dan durasi sewa.
 * - Menampilkan nomor Virtual Account untuk pembayaran.
 * </p>
 *
 * @author Kelompok Rent Car
 *
 */
public class CombinedApp extends Application {

    private Map<String, String> userCredentials = new HashMap<>();
    private String currentUser;
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private TextField usernameField;
    private ComboBox<String> brandComboBox;
    private ComboBox<String> modelComboBox;
    private TextField daysField;

    /**
     * Metode utama yang dilaksanakan oleh JavaFX untuk inisialisasi aplikasi.
     *
     * @param args Argumen baris perintah, jika ada.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metode untuk menyiapkan dan menampilkan antarmuka pengguna awal.
     *
     * @param primaryStage Panggung utama aplikasi.
     */

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
    /**
     * Metode untuk membuat antarmuka pengguna untuk login.
     *
     * @return GridPane yang berisi elemen-elemen antarmuka pengguna login.
     */
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


    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\p{Punct}).+$";
        return password.matches(regex);
    }


    /**
     * Metode untuk menangani login pengguna.
     *
     * @param username Nama pengguna yang dimasukkan.
     * @param password Kata sandi yang dimasukkan.
     */
    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty. Please enter valid credentials.");
        } else if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            currentUser = username;
            showCarSelection();
        } else {
            showAlert("Error", "Invalid credentials. Please try again.");
        }
    }

    /**
     * Metode untuk menangani registrasi pengguna baru.
     *
     * @param username Nama pengguna yang dimasukkan.
     * @param password Kata sandi yang dimasukkan.
     */
    private void handleRegistration(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty. Please enter valid credentials.");
        } else if (isPasswordValid(password)) {
            if (!userCredentials.containsKey(username)) {
                userCredentials.put(username, password);
                showAlert("Success", "Registration successful!");
            } else {
                showAlert("Error", "Username already exists. Please choose a different username.");
            }
        } else {
            showAlert("Error", "Password must contain at least one uppercase letter, one lowercase letter, and one symbol.");
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
                    modelComboBox.getItems().addAll("Jazz (700.000)", "Brio (500.000)", "Civic (1.000.000)");
                    break;
                case "Toyota":
                    modelComboBox.getItems().addAll("Alphard (1.000.000)", "Innova (800.000)", "Avanza (500.000)");
                    break;
                case "Mitsubishi":
                    modelComboBox.getItems().addAll("Pajero (1.000.000)", "X Pander (700.000)", "X force (500.000)");
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

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> goBackToLogin());
        GridPane.setConstraints(backButton, 0, 3);

        Button nextButton = new Button("Info Payment");
        nextButton.setOnAction(event -> rentCar(brandComboBox.getValue(), modelComboBox.getValue(), daysField.getText()));
        GridPane.setConstraints(nextButton, 1, 3);

        Button virtualAccountButton = new Button("Virtual Account");
        virtualAccountButton.setOnAction(event -> showVirtualAccountScene());
        GridPane.setConstraints(virtualAccountButton, 2, 3);

        grid.getChildren().addAll(brandLabel, brandComboBox, modelLabel, modelComboBox, daysLabel, daysField, backButton, nextButton, virtualAccountButton);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(grid);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(mainLayout, 400, 300));
    }
    

    private void goBackToLogin() {
        CombinedApp mainLayout = new CombinedApp();
        mainLayout.start(new Stage());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();  // Close the current stage
    }

    // Inside the CombinedApp class


    private void rentCar(String brand, String model, String days) {
        if (brand == null || model == null || days.isEmpty()) {
            showAlert("Error", "Please select a car and enter the number of days before proceeding to Info Payment.");
            return;
        }

        double dailyRate = getDailyRate(brand, model);
        if (dailyRate != -1) {
            int numberOfDays = Integer.parseInt(days);
            double totalPrice = dailyRate * numberOfDays;
            showAlert("Rental Info", "You rented a " + brand + " " + model + " for " + days + " days.\nTotal Price: $" + totalPrice);
        } else {
            showAlert("Error", "Invalid car model selected.");
        }
    }


    private void showVirtualAccountScene() {

        if (brandComboBox == null || modelComboBox == null || daysField == null) {
            showAlert("Error", "The fields cannot empty");
            return;
        }

        String brand = brandComboBox.getValue();
        String model = modelComboBox.getValue();
        String days = daysField.getText();

        if (brand == null || model == null || days.isEmpty()) {
            showAlert("Error", "Please select a car and enter the number of days before proceeding to Virtual Account.");
            return;
        }

        String virtualAccount = generateRandomNumericString();

        // Create layout
        BorderPane virtualAccountLayout = new BorderPane();
        VBox vbox = new VBox(10); // Vertical box for better arrangement
        vbox.setAlignment(Pos.CENTER); // Align the VBox to the center

        // Create Label for Virtual Account
        Label vaTextLabel = new Label("Virtual Account:");
        vaTextLabel.setStyle("-fx-font-size: 14pt;");

        // Create TextField for Virtual Account Number
        TextField virtualAccountTextField = new TextField(virtualAccount);
        virtualAccountTextField.setEditable(false); // Set to non-editable
        virtualAccountTextField.setPrefColumnCount(virtualAccount.length());
        virtualAccountTextField.setStyle("-fx-font-size: 14pt;");

        // Create Label for Timer
        Label timerLabel = new Label("Payment window closes in 5:00");
        timerLabel.setStyle("-fx-font-size: 14pt;");

        // Add components to vbox
        vbox.getChildren().addAll(vaTextLabel, virtualAccountTextField, timerLabel);

        // Create "Back to Menu" button
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(event -> goBackToMenu());

        // Add button to vbox
        vbox.getChildren().add(backButton);

        // Apply CSS style to the button
        backButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14pt;"
        );

        // Add vbox to virtualAccountLayout
        virtualAccountLayout.setCenter(vbox);

        // Create stage
        Stage stage = new Stage();
        stage.setScene(new Scene(virtualAccountLayout, 400, 300));
        stage.show();

        // Set up a 5-minute countdown timer
        int[] durationSeconds = {5 * 60};
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(durationSeconds[0]), (ActionEvent event) -> {
                    stage.close(); // Close the stage after 5 minutes
                })
        );
        timeline.setCycleCount(1);
        timeline.play();

        // Update timer label every second
        Timeline timerUpdate = new Timeline(
                new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                    int minutes = durationSeconds[0] / 60;
                    int seconds = durationSeconds[0] % 60;
                    timerLabel.setText("Payment window closes in " + String.format("%d:%02d", minutes, seconds));
                    durationSeconds[0]--; // Update value in the array
                })
        );
        timerUpdate.setCycleCount(durationSeconds[0]);
        timerUpdate.play();
    }



    // Method to handle the "Back to Menu" button action
    private void goBackToMenu() {
        CombinedApp mainLayout = new CombinedApp();
        mainLayout.start(new Stage());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();  // Close the current stage
    }


    private double getDailyRate(String brand, String model) {
        switch (brand) {
            case "Honda":
                switch (model) {
                    case "Jazz (700.000)":
                        return 700000;
                    case "Brio (500.000)":
                        return 500000;
                    case "Civic (1.000.000)":
                        return 1000000;
                }
                break;
            case "Toyota":
                switch (model) {
                    case "Alphard (1.000.000)":
                        return 1000000;
                    case "Innova (800.000)":
                        return 800000;
                    case "Avanza (500.000)":
                        return 500000;
                }
                break;
            case "Mitsubishi":
                switch (model) {
                    case "Pajero (1.000.000)":
                        return 1000000;
                    case "X Pander (700.000)":
                        return 700000;
                    case "X force (500.000)":
                        return 500000;
                }
                break;
        }
        return -1;
    }

    private String generateRandomNumericString() {
        // Menghasilkan UUID
        String uuid = UUID.randomUUID().toString();

        // Menghapus karakter "-" dan mengambil hanya digit
        return uuid.replaceAll("-", "").replaceAll("[a-zA-Z]", "");
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
