package com.example.security_demo.Dto;

import com.example.security_demo.Entity.Projects;

import java.util.List;

public class RequestDto {

    private String employeeName;
    private String password;
    private List<String> projects;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getProjectsList() {
        return projects;
    }

    public void setProjectsList(List<String> projectsList) {
        this.projects = projectsList;
    }
}
