package accountservice.security;

import accountservice.security.SecurityEvent;
import accountservice.security.SecurityEventDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityEventMapper {
    public SecurityEventDto securityEventToSecurityEventDto(SecurityEvent securityEvent) {
        return SecurityEventDto
                .builder()
                .id(securityEvent.getId())
                .date(securityEvent.getDate())
                .action(securityEvent.getAction())
                .subject(securityEvent.getSubject())
                .object(securityEvent.getObject())
                .path(securityEvent.getPath())
                .build();
    }

    public List<SecurityEventDto> securityEventsToSecurityEventDtos(List<SecurityEvent> securityEvents) {
        return securityEvents
                .stream()
                .map(this::securityEventToSecurityEventDto)
                .collect(Collectors.toList());
    }
}
