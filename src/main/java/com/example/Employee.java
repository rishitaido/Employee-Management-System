package com.example;

public class Employee {
    private int empid;
    private String fname;
    private String lname;
    private String email;
    private String hireDate;
    private String salary;

    public Employee(int empid, String fname, String lname, String email, String hireDate, String salary) {
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public int getEmpid() { return empid; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getEmail() { return email; }
    public String getHireDate() { return hireDate; }
    public String getSalary() { return salary; }
}
