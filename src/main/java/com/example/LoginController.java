package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String input = passwordField.getText().trim();

        try {
            if (email.equals("admin") && input.equals("password")) {
                showAlert("Admin login successful (CRUD screen coming soon)");
                return;
            }

            Connection conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employees WHERE email = ? AND SSN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, input);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/employee_view.fxml"));
                Scene scene = new Scene(loader.load());

                EmployeeController controller = loader.getController();
                controller.setEmployeeData(
                    rs.getInt("empid"),
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("email"),
                    rs.getDate("HireDate"),
                    rs.getBigDecimal("Salary"),
                    rs.getString("SSN")
                );

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
            } else {
                showAlert("Invalid login. Please check your credentials.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}