package accountservice.auditor;

import accountservice.security.SecurityEvent;
import accountservice.security.SecurityEventDto;
import accountservice.security.SecurityEventMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AuditorService {
    private AuditorRepository auditorRepository;
    private SecurityEventMapper securityEventMapper;

    public List<SecurityEventDto> getAllSecurityEventsOrderedByIdAsc() {
        return securityEventMapper.securityEventsToSecurityEventDtos(auditorRepository.findAllByOrderByIdAsc());
    }

    public void saveSecurityEvent(SecurityEvent securityEvent) {
        if (securityEvent.getDate() == null) {
            securityEvent.setDate(new Date());
        }

        if (securityEvent.getPath() == null) {
            securityEvent.setPath(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath());
        }

        auditorRepository.save(securityEvent);
    }
}
