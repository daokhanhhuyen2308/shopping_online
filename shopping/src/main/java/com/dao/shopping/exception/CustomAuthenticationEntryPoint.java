package com.dao.shopping.exception;

import com.dao.shopping.dto.responses.ApiHandleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ApiHandleResponse<CustomError> apiHandleResponse = new ApiHandleResponse<>();
        apiHandleResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        CustomError customError = new CustomError();
                customError.setPath(request.getRequestURI());
                customError.setTimestamp(new Date());
                customError.setCode("RESOURCE_UNAUTHORIZED");
                customError.setDetails("You need to authenticate or provide valid credentials to access this resource. Please recheck your information!");
                customError.setMessage(authException.getMessage());

        apiHandleResponse.setError(customError);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiHandleResponse));


    }
}
