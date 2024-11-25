package com.dao.shopping.configuration;

import com.dao.shopping.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        String token;

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7).trim();
            var storedToken = tokenRepository.findByToken(token).orElse(null);
            if (storedToken != null){
                storedToken.setExpired(true);
                storedToken.setRevoke(true);
                tokenRepository.save(storedToken);
            }
        }
    }
}
