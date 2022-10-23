package accountservice.auditor;

import accountservice.security.SecurityEventDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AuditorController {
    private AuditorService auditorService;

    @GetMapping("api/security/events")
    public List<SecurityEventDto> getEvents() {
        return auditorService.getAllSecurityEventsOrderedByIdAsc();
    }
}
