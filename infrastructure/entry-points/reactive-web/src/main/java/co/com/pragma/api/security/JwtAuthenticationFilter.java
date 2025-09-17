package co.com.pragma.api.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("[JwtAuthFilter] Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("[JwtAuthFilter] Token recibido: {}", token);
            if (jwtUtil.validateToken(token)) {
                String subject = jwtUtil.getSubject(token);
                String role = jwtUtil.getRole(token);
                log.info("[JwtAuthFilter] Subject extraído: {}", subject);
                log.info("[JwtAuthFilter] Rol extraído: {}", role);
                if (role == null || role.trim().isEmpty()) {
                    log.error("[JwtAuthFilter] El JWT no contiene un rol válido. Token: {}", token);
                } else {
                    log.info("[JwtAuthFilter] Authority asignada: {}", role);
                }
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                SecurityContext context = new SecurityContextImpl(auth);
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            } else {
                log.error("[JwtAuthFilter] Token inválido: {}", token);
            }
        } else {
            log.warn("[JwtAuthFilter] No se encontró header Authorization válido");
        }

        return chain.filter(exchange);
    }
}
