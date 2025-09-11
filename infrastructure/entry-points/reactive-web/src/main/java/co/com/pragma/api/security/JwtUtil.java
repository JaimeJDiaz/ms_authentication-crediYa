package co.com.pragma.api.security;

import co.com.pragma.model.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String secretKey = "clave-super-segura-de-32-caracteres-minimo"; // 🔒 Usa algo más robusto en producción
    private static final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
    private static final long expirationMs = 86400000; // 1 día

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roleId", user.getRoleId()) // Puedes agregar más claims si lo deseas
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}

