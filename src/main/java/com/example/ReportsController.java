package com.example;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportsController {

    @FXML private ComboBox<String> reportTypeBox;
    @FXML private ListView<String> reportTable; 

    @FXML
    public void initialize() {
        reportTypeBox.setItems(FXCollections.observableArrayList(
            "Employee Pay History",
            "Total Pay by Job Title",
            "Total Pay by Division"
        ));
    }

    @FXML
    private void handleGenerate() {
        String selected = reportTypeBox.getValue();
        ObservableList<String> data = FXCollections.observableArrayList();

        String sql = null;

        if ("Employee Pay History".equals(selected)) {
            sql = "SELECT empid, pay_date, earnings FROM payroll ORDER BY empid, pay_date";
        } else if ("Total Pay by Job Title".equals(selected)) {
            sql = "SELECT jt.job_title, SUM(p.earnings) AS total FROM payroll p " +
                  "JOIN employee_job_titles ejt ON p.empid = ejt.empid " +
                  "JOIN job_titles jt ON jt.job_title_id = ejt.job_title_id GROUP BY jt.job_title";
        } else if ("Total Pay by Division".equals(selected)) {
            sql = "SELECT d.name, SUM(p.earnings) AS total FROM payroll p " +
                  "JOIN employee_division ed ON p.empid = ed.empid " +
                  "JOIN division d ON ed.div_ID = d.ID GROUP BY d.name";
        }

        if (sql == null) {
            showAlert("Please select a valid report type.");
            return;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    row.append(rs.getString(i)).append(" | ");
                }
                data.add(row.toString());
            }

            reportTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_home.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) reportTypeBox.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reports");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
