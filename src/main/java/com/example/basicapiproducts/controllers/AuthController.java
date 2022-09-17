package com.example.basicapiproducts.controllers;

import com.example.basicapiproducts.dtos.AuthRequest;
import com.example.basicapiproducts.dtos.AuthResponse;
import com.example.basicapiproducts.dtos.ResponseDto;
import com.example.basicapiproducts.dtos.UserDto;
import com.example.basicapiproducts.entities.User;
import com.example.basicapiproducts.jwt.JwtUtil;
import com.example.basicapiproducts.services.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDto userDto){
        return authService.save(userDto);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateAccessToken(authentication);
        return new AuthResponse(authentication.getName(), token);
    }

    @PutMapping("become-to-admin/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto becomeToAdmin(@PathVariable String username){
        authService.becomeToUser(username);
        Map<String, String> data = new HashMap<>();
        data.put("message", username + ": become to admin");
        return new ResponseDto(HttpStatus.OK.value(), data, LocalDate.now());
    }

}
