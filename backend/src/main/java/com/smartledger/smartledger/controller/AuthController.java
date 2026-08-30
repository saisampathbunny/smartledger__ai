package com.smartledger.smartledger.controller;

import com.smartledger.smartledger.dto.AuthResponse;
import com.smartledger.smartledger.dto.LoginRequest;
import com.smartledger.smartledger.dto.RegisterRequest;
import com.smartledger.smartledger.model.User;
import com.smartledger.smartledger.repository.UserRepository;
import com.smartledger.smartledger.security.JwtService;
import com.smartledger.smartledger.service.CustomUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request)
    {
        if (request.getName() == null ||
                request.getName().isBlank())
        {
            return ResponseEntity.badRequest()
                    .body("Name is required.");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank())
        {
            return ResponseEntity.badRequest()
                    .body("Email is required.");
        }

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        if (request.getAge() == null ||
                request.getAge() < 18)
        {
            return ResponseEntity.badRequest()
                    .body("You must be at least 18 years old.");
        }

        if (request.getPassword() == null ||
                !isStrongPassword(request.getPassword()))
        {
            return ResponseEntity.badRequest()
                    .body(
                            "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character."
                    );
        }

        if (!request.getPassword()
                .equals(request.getConfirmPassword()))
        {
            return ResponseEntity.badRequest()
                    .body("Passwords do not match.");
        }

        if (userRepository.existsByEmail(email))
        {
            return ResponseEntity.badRequest()
                    .body("Email is already registered.");
        }

        User user = new User();

        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setAge(request.getAge());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        userRepository.save(user);

        return ResponseEntity.ok(
                "Registration successful."
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request)
    {
        try
        {
            if (request.getEmail() == null ||
                    request.getEmail().isBlank() ||
                    request.getPassword() == null ||
                    request.getPassword().isBlank())
            {
                return ResponseEntity.status(401)
                        .body("Invalid email or password.");
            }

            String email =
                    request.getEmail()
                            .trim()
                            .toLowerCase();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);

            String token =
                    jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    new AuthResponse(token)
            );
        }
        catch (Exception e)
        {
            return ResponseEntity.status(401)
                    .body("Invalid email or password.");
        }
    }

    private boolean isStrongPassword(
            String password)
    {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }
}
