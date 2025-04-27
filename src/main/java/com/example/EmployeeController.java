package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;

public class EmployeeController {

    @FXML private Label empIdLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label hireDateLabel;
    @FXML private Label salaryLabel;
    @FXML private Label ssnLabel;

    public void setEmployeeData(int empId, String firstName, String lastName, String email,
                                Date hireDate, BigDecimal salary, String ssn) {
        empIdLabel.setText("ID: " + empId);
        firstNameLabel.setText("First Name: " + firstName);
        lastNameLabel.setText("Last Name: " + lastName);
        emailLabel.setText("Email: " + email);
        hireDateLabel.setText("Hire Date: " + hireDate.toString());
        salaryLabel.setText("Salary: $" + salary.setScale(2));
        ssnLabel.setText("SSN: " + ssn);
    }

    @FXML
    private void handleExit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) empIdLabel.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}