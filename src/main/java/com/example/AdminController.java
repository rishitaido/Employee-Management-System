package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminController {

    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idCol;
    @FXML private TableColumn<Employee, String> fnameCol;
    @FXML private TableColumn<Employee, String> lnameCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, String> hireDateCol;
    @FXML private TableColumn<Employee, String> salaryCol;
    @FXML private TextField searchField;

    @FXML private Button logoutButton;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        try {
            employeeList.clear();
            Connection conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employees";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getInt("empid"),
                        rs.getString("Fname"),
                        rs.getString("Lname"),
                        rs.getString("email"),
                        rs.getDate("HireDate").toString(),
                        rs.getBigDecimal("Salary").toString()
                ));
            }
            employeeTable.setItems(employeeList);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading employees: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadEmployeeData();
            return;
        }

        try {
            employeeList.clear();
            Connection conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employees WHERE empid = ? OR Fname LIKE ? OR Lname LIKE ? OR SSN = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, keyword);
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setString(4, keyword);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getInt("empid"),
                        rs.getString("Fname"),
                        rs.getString("Lname"),
                        rs.getString("email"),
                        rs.getDate("HireDate").toString(),
                        rs.getBigDecimal("Salary").toString()
                ));
            }
            employeeTable.setItems(employeeList);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error searching employee: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_employee.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) employeeTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Add Employee form: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateSalary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_update_salary.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) employeeTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Salary Update form: " + e.getMessage());
        }
    }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_reports.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) employeeTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening Reports page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) employeeTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error logging out: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
