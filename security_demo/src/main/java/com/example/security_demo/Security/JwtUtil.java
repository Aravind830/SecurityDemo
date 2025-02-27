package com.example.security_demo.Security;

import com.example.security_demo.Entity.Employee;
import com.example.security_demo.Entity.Projects;
import com.example.security_demo.Repository.EmployeeRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static SecretKey secretKey= Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final int jwtExpire=180000;

    @Autowired
    private final EmployeeRepository employeeRepository;

    public JwtUtil(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String generateToken(String employeeName){
        Optional<Employee> employee=employeeRepository.findByEmployeeName(employeeName);
        List<Projects> employeeList=employee.get().getProjects();

        return Jwts.builder().setSubject(employeeName).claim("projects",employeeList.stream()
                .map(projects->projects.getProjectName()).collect(Collectors.joining(",")))
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + jwtExpire)).signWith(secretKey)
                .compact();
    }

    public String getNameByToken(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token).getBody().getSubject();
    }

    public List<String> getRolesByToken(String token){
        String projects= Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token).getBody().get("projects",String.class);
        return List.of(projects);
    }

    public Boolean isValidate(String token){
        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
