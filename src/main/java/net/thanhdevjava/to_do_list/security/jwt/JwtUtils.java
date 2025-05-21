package net.thanhdevjava.to_do_list.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    // Secret key used to sign and verify JWT tokens
    private final String SECRET_KEY = Encoders.BASE64.encode("thanhdev25_this_is_my_secret_key".getBytes()); // 🔐 You should replace this with your own secure key

    // Extract username (subject) from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse the token and get all claims (like subject, issued date, expiration date, etc.)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // Use the same key used to sign the token
                .parseClaimsJws(token)     // Parse the token
                .getBody();                // Get the claims from the token body
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate a new JWT token for a user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // You can add custom claims if needed
        return createToken(claims, userDetails.getUsername());
    }

    // Create JWT token using claims and subject (username)
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationMs = 1000 * 60 * 60 * 10; // Token is valid for 10 hours

        return Jwts.builder()
                .setClaims(claims)                              // Set custom claims
                .setSubject(subject)                            // Set subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // Expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign the token with the secret key
                .compact();                                     // Build the token
    }

    // Validate the token with user details
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract username from token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Check if username matches and token is not expired
    }
}


