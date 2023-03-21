package mx.ecommerce.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.xml.bind.DatatypeConverter;
import mx.ecommerce.dtos.LoginRequestDTO;
import mx.ecommerce.dtos.LoginResponseDTO;
import mx.ecommerce.repositories.ClientRepository;
import mx.ecommerce.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    public static String secretKey = "riXJ89V6NCXag6LzVzsTVcdMh0aqn8zP";

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    EmployeeRepository employeeRepository;
    ClientRepository clientRepository;

    public UserController(@Autowired EmployeeRepository employeeRepository,
                          @Autowired ClientRepository clientRepository) {
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/employee/login")
    @Operation(summary = "Login employees")
    public @ResponseBody ResponseEntity<LoginResponseDTO> employeeLogin(@RequestBody LoginRequestDTO body) {
        md.reset();
        md.update(body.getPassword().getBytes());
        String passwordHash = DatatypeConverter
                .printHexBinary(md.digest()).toUpperCase();
        System.out.println(passwordHash);
        return employeeRepository.loginEmployee(body.getEmail(), passwordHash).map(employee -> {
            String token = getJWTToken(body.getEmail(), "employee", employee.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setEmail(body.getEmail());
            loginResponseDTO.setToken(token);

            return ResponseEntity.ok().body(loginResponseDTO);
        }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/client/login")
    @Operation(summary = "Login client")
    public @ResponseBody ResponseEntity<LoginResponseDTO> clientLogin(@RequestBody LoginRequestDTO body) {
        md.reset();
        md.update(body.getPassword().getBytes());
        String passwordHash = DatatypeConverter
                .printHexBinary(md.digest()).toUpperCase();
        System.out.println(passwordHash);
        return clientRepository.loginClient(body.getEmail(), passwordHash).map(client -> {
            String token = getJWTToken(body.getEmail(), "client", client.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setEmail(body.getEmail());
            loginResponseDTO.setToken(token);

            return ResponseEntity.ok().body(loginResponseDTO);
        }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private String getJWTToken(String email, String role, int id) {
        String token = Jwts.builder()
                .setId("ecommerceJWT")
                .setSubject(email)
                .addClaims(Map.of("userId", id))
                .claim("authorities", Collections.singletonList(role))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();

        return token;
    }


}
