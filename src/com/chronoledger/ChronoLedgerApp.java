package com.chronoledger;

import java.io.File;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChronoLedgerApp extends Application {

    private Ledger ledger;
    private TableView<ActivityLog> tableView;
    private String currentUser;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginScreen();
    }

    // Step 1: Login UI
    private void showLoginScreen() {
        VBox loginRoot = new VBox(20);
        loginRoot.setPadding(new Insets(30));
        loginRoot.setAlignment(Pos.CENTER);
        loginRoot.setStyle("-fx-background-color: #f0f4f7;");

        Label title = new Label("ChronoLedger");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 28));
        title.setTextFill(javafx.scene.paint.Color.DARKSLATEGRAY);

        Label prompt = new Label("Enter Your Name to Login:");
        prompt.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));

        TextField nameField = new TextField();
        nameField.setMaxWidth(250);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(120);
        loginButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");

        loginButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                this.currentUser = name;
                this.ledger = new Ledger();
                ledger.loadFromFile(getUserLedgerFile());
                showMainUI();
            }
        });

        loginRoot.getChildren().addAll(title, prompt, nameField, loginButton);
        Scene scene = new Scene(loginRoot, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login - ChronoLedger");
        primaryStage.show();
    }

    // Step 2: Main Dashboard UI
    private void showMainUI() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Welcome, " + currentUser);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(javafx.scene.paint.Color.DARKBLUE);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter task activity...");
        inputField.setMaxWidth(400);

        Button addButton = new Button("Add Task");
        Button exportPdfBtn = new Button("Export PDF");
        Button exportCsvBtn = new Button("Export CSV");
        Button verifyBtn = new Button("Verify Ledger");

        for (Button b : new Button[]{addButton, exportPdfBtn, exportCsvBtn, verifyBtn}) {
            b.setPrefWidth(130);
            b.setStyle("-fx-background-color: #2E8B57; -fx-text-fill: white;");
        }

        HBox buttonBox = new HBox(10, addButton, exportPdfBtn, exportCsvBtn, verifyBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // Add Task Button Logic ✅
        addButton.setOnAction(e -> {
            String activity = inputField.getText().trim();
            if (!activity.isEmpty()) {
                ledger.addActivity(activity);
                ledger.saveToFile(getUserLedgerFile());
                inputField.clear();
                updateTable();
            }
        });

        // Export to PDF
        exportPdfBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Ledger as PDF");
            fileChooser.setInitialFileName("ChronoLedger.pdf");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                Utils.exportLedgerToPDF(file.getAbsolutePath(), ledger.getChain());
            }
        });

        // Export to CSV
        exportCsvBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Ledger as CSV");
            fileChooser.setInitialFileName("ChronoLedger.csv");
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                Utils.exportLedgerToCSV(file.getAbsolutePath(), ledger.getChain());
            }
        });

        // Verify Ledger
        verifyBtn.setOnAction(e -> {
            boolean valid = ledger.isLedgerValid();
            Alert alert = new Alert(valid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle("Ledger Integrity Check");
            alert.setHeaderText(null);
            alert.setContentText(valid ? "✅ Ledger is valid and secure." : "❌ Ledger has been tampered!");
            alert.showAndWait();
        });

        // Setup Table
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(300);
        tableView.setPrefWidth(850);
        tableView.setStyle("-fx-font-family: 'Courier New';");

        TableColumn<ActivityLog, String> activityCol = new TableColumn<>("Activity");
        TableColumn<ActivityLog, String> timeCol = new TableColumn<>("Timestamp");
        TableColumn<ActivityLog, String> hashCol = new TableColumn<>("Hash");
        TableColumn<ActivityLog, String> prevHashCol = new TableColumn<>("Prev Hash");

        activityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActivity()));
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));
        hashCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHash()));
        prevHashCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrevHash()));

        tableView.getColumns().addAll(activityCol, timeCol, hashCol, prevHashCol);

        updateTable();

        root.getChildren().addAll(title, inputField, buttonBox, tableView);

        Scene scene = new Scene(root, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ChronoLedger Dashboard - " + currentUser);
        primaryStage.show();
    }

    private void updateTable() {
        ObservableList<ActivityLog> data = FXCollections.observableArrayList(ledger.getActivityLogs());
        tableView.setItems(data);
    }

    private String getUserLedgerFile() {
        return "ledger_" + currentUser + ".txt";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
