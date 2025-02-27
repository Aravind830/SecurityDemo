package com.example.security_demo.Security;

import com.example.security_demo.Entity.Employee;
import com.example.security_demo.Service.EmployeeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final EmployeeService employeeService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, EmployeeService employeeService) {
        this.jwtUtil = jwtUtil;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")){
            token=token.substring(7);
            String employeeName=jwtUtil.getNameByToken(token);
            if (employeeName != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = employeeService.loadUserByUsername(employeeName);
                if (jwtUtil.isValidate(token)){
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
