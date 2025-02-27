package com.example.security_demo.Service;

import com.example.security_demo.Entity.Employee;
import com.example.security_demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EmployeeService implements UserDetailsService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override//Authorize a Employee to Project
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee=employeeRepository.findByEmployeeName(username)
                .orElseThrow(()->new UsernameNotFoundException("Employee Not Found "+ username));
        return new org.springframework.security.core.userdetails.User(employee.getEmployeeName(),employee.getPassword(),
                employee.getProjects().stream().map(projects->new SimpleGrantedAuthority(projects.getProjectName())).collect(Collectors.toList()));
    }
}
