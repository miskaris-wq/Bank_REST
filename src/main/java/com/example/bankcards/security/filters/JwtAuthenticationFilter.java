package com.example.bankcards.security.filters;

import com.example.bankcards.exception.EmptyTokenException;
import com.example.bankcards.exception.InvalidTokenException;
import com.example.bankcards.exception.JwtAuthenticationException;
import com.example.bankcards.exception.TokenExpiredException;
import com.example.bankcards.security.JwtComponent;
import com.example.bankcards.service.impl.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);

            jwtComponent.checkTokenOnEmpty(jwt);

            String username = jwtComponent.extractUserName(jwt);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                boolean valid = jwtComponent.isTokenValid(jwt, userDetails);
                if (!valid) {
                    throw new JwtAuthenticationException("JWT токен не валиден");
                }

                log.info(userDetails.getAuthorities().toString());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (EmptyTokenException
                 | TokenExpiredException
                 | InvalidTokenException
                 | JwtAuthenticationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            String sanitized = ex.getMessage().replace("\"", "\\\"");
            String body = String.format(
                    "{\"status\":%d,\"error\":\"%s\"}",
                    ex.getStatus().value(),
                    sanitized
            );
            response.getWriter().write(body);
        }
    }
}