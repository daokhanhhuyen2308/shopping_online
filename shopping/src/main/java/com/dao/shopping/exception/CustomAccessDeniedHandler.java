package com.dao.shopping.exception;

import com.dao.shopping.dto.responses.ApiHandleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ApiHandleResponse<CustomError> apiHandleResponse  = new ApiHandleResponse<>();
        apiHandleResponse.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
        CustomError customError = new CustomError();
                customError.setCode("RESOURCE_ACCESS_DENIED");
                customError.setPath(request.getRequestURI());
                customError.setTimestamp(new Date());
                customError.setMessage(accessDeniedException.getMessage());
                customError.setDetails("Access is denied due to only ADMIN or MANAGER permission access those endpoints. Please recheck your information!");

        apiHandleResponse.setError(customError);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiHandleResponse));


    }
}
