package com.example.security_demo.Controller;

import com.example.security_demo.Dto.RequestDto;
import com.example.security_demo.Entity.Employee;
import com.example.security_demo.Entity.Projects;
import com.example.security_demo.Repository.EmployeeRepository;
import com.example.security_demo.Repository.ProjectRepository;
import com.example.security_demo.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeRepository employeeRepository, JwtUtil jwtUtil, ProjectRepository projectRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.projectRepository = projectRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerEmployee(@RequestBody Employee requestDto){
        if (employeeRepository.findByEmployeeName(requestDto.getEmployeeName()).isPresent()){
            return ResponseEntity.badRequest().body(requestDto.getEmployeeName() + " Already Exists.");
        }
        Employee employee=new Employee();
        employee.setEmployeeName(requestDto.getEmployeeName());
        employee.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        List<Projects> projects=new ArrayList<>();
        for (Projects projects1:requestDto.getProjects()){
            Projects project=projectRepository.findByProjectName(projects1.getProjectName())
                    .orElseThrow(()->new RuntimeException(projects1 +" Not found."));
            projects.add(project);
        }
        employee.setProjects(projects);
        employeeRepository.save(employee);
        return ResponseEntity.ok("Registered SuccessFully..!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Employee loginRequest){
        try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmployeeName(),loginRequest.getPassword()));
        }catch (Exception e){
            System.out.println("exception "+e);
        }

        String token = jwtUtil.generateToken(loginRequest.getEmployeeName());
        return ResponseEntity.ok(token);

    }
}
