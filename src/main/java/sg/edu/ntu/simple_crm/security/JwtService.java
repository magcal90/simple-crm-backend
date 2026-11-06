package sg.edu.ntu.simple_crm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        // Converts the secret text into a key used to sign and check tokens
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return generateToken(username, List.of());
    }

    public String generateToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replaceFirst("^ROLE_", ""))
                .toList();
        return generateToken(authentication.getName(), roles);
    }

    private String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)            // Who the token is for
                .claim("email", username)
                .claim("roles", roles)
                .setIssuedAt(now)                // When token was created
                .setExpiration(expiry)           // When token expires
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object roles = claims.get("roles");
        if (!(roles instanceof List<?> roleList)) {
            return List.of();
        }
        return roleList.stream().map(String::valueOf).toList();
    }

    public String extractUsername(String token) {
        // parseClaimsJws validates the signature first, then decodes the payload.
        // If the token was tampered with, it throws an exception before reaching getSubject().
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            // Parsing validates signature and also checks expiration.
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception ex) {
            // In production you would log this exception.
            return false;
        }
    }
}
