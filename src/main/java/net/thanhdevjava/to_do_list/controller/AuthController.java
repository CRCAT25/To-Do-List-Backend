package net.thanhdevjava.to_do_list.controller;

import lombok.RequiredArgsConstructor;
import net.thanhdevjava.to_do_list.dto.AuthRequest;
import net.thanhdevjava.to_do_list.dto.AuthResponse;
import net.thanhdevjava.to_do_list.dto.ResponseDTO;
import net.thanhdevjava.to_do_list.dto.SignupRequest;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.security.jwt.JwtUtils;
import net.thanhdevjava.to_do_list.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")  // Base endpoint for authentication-related requests
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // Optional: Enable CORS for all origins (can be restricted for production)
public class AuthController {

    private final AuthenticationManager authenticationManager;  // Authentication manager for user authentication
    private final JwtUtils jwtUtils;  // Utility class to handle JWT creation and validation
    private final UserService userService;  // Service for user-related operations (e.g., save user, check if username exists)
    private final PasswordEncoder passwordEncoder;  // Password encoder for hashing passwords

    // Endpoint for user registration (sign-up)
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> register(@RequestBody SignupRequest signupRequest) {
        // Check if the username already exists
        if (userService.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error("The username already exists", "BAD_REQUEST"));
        }

        // Hash the password before saving it in the database
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Create a new User object and set its properties
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(encodedPassword);  // Store the hashed password
        user.setEmail(signupRequest.getEmail());
        user.setRole("ROLE_USER");  // Default role assigned to the user

        // Save the user to the database
        userService.save(user);

        // Return a success message
        return ResponseEntity.ok(ResponseDTO.success("User registered successfully", null));
    }

    // Endpoint for user login (authentication) and JWT generation
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // Authenticate the user using the provided username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // After successful authentication, retrieve user details and generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();  // Get user details
        String token = jwtUtils.generateToken(userDetails);  // Generate JWT token for the authenticated user

        // Return the JWT token wrapped in an AuthResponse object
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
