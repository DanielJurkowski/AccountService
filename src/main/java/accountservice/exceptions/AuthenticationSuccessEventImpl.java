package accountservice.exceptions;


import accountservice.security.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationSuccessEventImpl implements ApplicationListener<AuthenticationSuccessEvent> {
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        bruteForceProtectionService.resetLoginFailCount(event.getAuthentication().getName());
    }
}
