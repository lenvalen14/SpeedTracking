package edu.ut.its.filter;

import edu.ut.its.components.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtil;
    private final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/account/generate-secret-key", "GET"),
                Pair.of("/account/login", "POST"),
                Pair.of("/account/register", "POST"),
                Pair.of("/swagger-ui/index.html", "GET"),
                Pair.of("/v3/api-docs", "GET"),
                Pair.of("/v3/api-docs/**", "GET"),
                Pair.of("/v3/api-docs/swagger-config", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        boolean isBypass = bypassTokens.stream()
                .anyMatch(token -> requestPath.startsWith(token.getFirst()) &&
                        requestMethod.equalsIgnoreCase(token.getSecond()));

        logger.debug("Bypass check for path: {}, method: {} - Result: {}", requestPath, requestMethod, isBypass);
        return isBypass;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {
        try {
            if (isBypassToken(request)) {
                logger.info("Bypassing token for request path: {}", request.getServletPath());
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            logger.debug("Authorization Header: {}", authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.debug("No Authorization header or invalid format. Proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractEmail(token);
            logger.debug("Extracted Email from Token: {}", email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                logger.debug("Loaded UserDetails: {}", userDetails.getUsername());

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Authentication successful. User authenticated: {}", userDetails.getUsername());
                } else {
                    logger.debug("Token validation failed for user: {}", email);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
        }
    }
}