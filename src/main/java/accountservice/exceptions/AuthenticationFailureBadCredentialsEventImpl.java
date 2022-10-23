package accountservice.exceptions;


import accountservice.security.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationFailureBadCredentialsEventImpl implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        bruteForceProtectionService.incrementLoginFailCountAndLockAccountIfLimit(event.getAuthentication().getName());
    }
}
