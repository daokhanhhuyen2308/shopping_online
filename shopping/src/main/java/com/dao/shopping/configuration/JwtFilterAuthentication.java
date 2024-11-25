package com.dao.shopping.configuration;

import com.dao.shopping.repository.TokenRepository;
import com.dao.shopping.security.CustomUserDetailsService;
import com.dao.shopping.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Slf4j
public class JwtFilterAuthentication extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final TokenRepository tokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilterAuthentication(JwtTokenUtils jwtTokenUtils,
                                   TokenRepository tokenRepository,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.tokenRepository = tokenRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7).trim();

            try{
                userEmail = jwtTokenUtils.getUserEmailFromToken(token);
            }
            catch (IllegalArgumentException exception){
                log.error("Unable to get JWT: {}", exception.getMessage());
            }
            catch (ExpiredJwtException exception){
                log.error("Token has expired: {}", exception.getMessage());
            }
            catch (UnsupportedJwtException exception){
                log.error("Unsupported JWT Token: {}", exception.getMessage());
            }
            catch (SignatureException exception){
                log.error("There is an error with the signature of you Token: {}", exception.getMessage());
            }

        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            var isTokenValid = tokenRepository.findByToken(token).map(t -> !t.isExpired() && !t.isRevoke())
                    .orElse(false);

                if (jwtTokenUtils.validate(token, userDetails) && isTokenValid){

                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }

        filterChain.doFilter(request, response);

    }
}
