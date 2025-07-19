package com.baerchen.central.authentication.registeredclient.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    @ResponseBody

    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        return """
                <h1>OAuth2 Error Debug</h1>
                <p><strong>Status:</strong> %s</p>
                <p><strong>Message:</strong> %s</p>
                <p><strong>Exception:</strong> %s</p>
                """.formatted(status, message, (exception != null ? exception.getClass().getName() + " - " + exception.getMessage() : "none"));
    }
}
