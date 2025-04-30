package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateSalaryController {

    @FXML private TextField minSalaryField;
    @FXML private TextField maxSalaryField;
    @FXML private TextField percentField;

    @FXML
    private void handleUpdate() {
        try {
            BigDecimal min = new BigDecimal(minSalaryField.getText().trim());
            BigDecimal max = new BigDecimal(maxSalaryField.getText().trim());
            BigDecimal percent = new BigDecimal(percentField.getText().trim());

            Connection conn = DBUtil.getConnection();
            String sql = "UPDATE employees SET Salary = Salary * (1 + ? / 100) WHERE Salary >= ? AND Salary < ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, percent);
            stmt.setBigDecimal(2, min);
            stmt.setBigDecimal(3, max);

            int rows = stmt.executeUpdate();
            conn.close();

            showAlert(rows + " salaries updated.");
            goBack();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        goBack();
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) minSalaryField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Salary");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
