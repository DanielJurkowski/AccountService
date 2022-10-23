package accountservice.user;

import accountservice.security.BruteForceProtectionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            bruteForceProtectionService.resetUserLockIfLockedAndIf24HoursPassedAfterLock(email);
        }

        return userRepository
                .findByEmailIgnoreCase(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}