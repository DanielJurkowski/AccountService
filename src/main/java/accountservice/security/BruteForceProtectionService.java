package accountservice.security;

import accountservice.auditor.AuditorService;
import accountservice.user.User;
import accountservice.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

import static accountservice.security.Event.*;


@Service
@AllArgsConstructor
@Transactional
public class BruteForceProtectionService {
    private final int MAX_LOGIN_FAIL_ATTEMPTS = 5;
    private UserRepository userRepository;
    private AuditorService auditorService;

    public void resetLoginFailCount(String email) {
        userRepository.setLoginFailCount(0, email);
    }

    public void resetUserLockIfLockedAndIf24HoursPassedAfterLock(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (!user.isAccountNonLocked() && user.getLockedAt().plusDays(1).isBefore(LocalDate.now())) {
            user.setAccountNonLocked(true);
            user.setLoginFailCount(0);
            user.setLockedAt(null);

            auditorService.saveSecurityEvent(SecurityEvent
                    .builder()
                    .action(UNLOCK_USER)
                    .subject(email)
                    .object("Unlock user " + email)
                    .build());
        }
    }

    public void incrementLoginFailCountAndLockAccountIfLimit(String email) {
        userRepository.findByEmailIgnoreCase(email).ifPresentOrElse(user -> {
                    user.setLoginFailCount(user.getLoginFailCount() + 1);

                    auditorService.saveSecurityEvent(SecurityEvent
                            .builder()
                            .action(LOGIN_FAILED)
                            .subject(email)
                            .object(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                            .build());

                    if (user.getLoginFailCount() == MAX_LOGIN_FAIL_ATTEMPTS && !user.getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
                        user.setLockedAt(LocalDate.now());
                        user.setAccountNonLocked(false);

                        auditorService.saveSecurityEvent(SecurityEvent
                                .builder()
                                .action(BRUTE_FORCE)
                                .subject(email)
                                .object(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                                .build());

                        auditorService.saveSecurityEvent(SecurityEvent
                                .builder()
                                .action(LOCK_USER)
                                .subject(email)
                                .object("Lock user " + email)
                                .build());
                    }
                }, () -> auditorService.saveSecurityEvent(SecurityEvent
                        .builder()
                        .action(LOGIN_FAILED)
                        .subject(email)
                        .object(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().getPath())
                        .build())
        );
    }
}