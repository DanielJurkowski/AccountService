package accountservice.exceptions;

import accountservice.auditor.AuditorService;
import accountservice.security.Event;
import accountservice.security.SecurityEvent;
import accountservice.user.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final AuditorService auditorService;
    private final CurrentUser currentUser;

    public AccessDeniedHandlerImpl(AuditorService auditorService, CurrentUser currentUser) {
        this.auditorService = auditorService;
        this.currentUser = currentUser;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.ACCESS_DENIED)
                .subject(currentUser.getCurrentUser().getUsername())
                .object(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                .build());

        response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
    }
}
