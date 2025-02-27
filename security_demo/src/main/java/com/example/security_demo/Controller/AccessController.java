package com.example.security_demo.Controller;

import com.example.security_demo.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccessController {

    @Autowired
    private final JwtUtil jwtUtil;

    public AccessController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private String hasManager="MANAGER";
    private String hasEmployee="EMPLOYEE";

    @GetMapping("/access")
    public ResponseEntity<String> getAccess(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer")) {
                String jwtToken = token.substring(7);
                if (jwtUtil.isValidate(jwtToken)) {
                    String employeeName = jwtUtil.getNameByToken(jwtToken);
                    List<String> projects = jwtUtil.getRolesByToken(jwtToken);
                    if (employeeName.equals(hasManager)) {
                        return ResponseEntity.ok("Welcome " + employeeName + ",You have access " + projects + "specific Data.");
                    } else if (employeeName.equals(hasEmployee)) {
                        return ResponseEntity.ok("Welcome " + employeeName + ",You have access " + projects + "specific Data.");
                    } else {
                        return ResponseEntity.badRequest().body("Access Denied Don't have Access for Specific Role.");
                    }
                }
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized Credentials.");
    }
}
