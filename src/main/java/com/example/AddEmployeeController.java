package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddEmployeeController {

    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField ssnField;
    @FXML private TextField hireDateField;
    @FXML private TextField salaryField;

    @FXML
    private void handleSave() {
        String fname = fnameField.getText().trim();
        String lname = lnameField.getText().trim();
        String email = emailField.getText().trim();
        String ssn = ssnField.getText().trim();
        String hireDate = hireDateField.getText().trim();
        String salary = salaryField.getText().trim();

        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || ssn.isEmpty() || hireDate.isEmpty() || salary.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        try {
            Connection conn = DBUtil.getConnection();
            String sql = "INSERT INTO employees (Fname, Lname, email, SSN, HireDate, Salary) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, email);
            stmt.setString(4, ssn);
            stmt.setString(5, hireDate);
            stmt.setBigDecimal(6, new java.math.BigDecimal(salary));

            int rows = stmt.executeUpdate();
            conn.close();

            if (rows > 0) {
                showAlert("Employee added successfully!");
                goBackToAdminHome();
            } else {
                showAlert("Failed to add employee.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        goBackToAdminHome();
    }

    private void goBackToAdminHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) fnameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Employee");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
