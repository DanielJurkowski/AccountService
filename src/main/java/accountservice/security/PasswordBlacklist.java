package accountservice.security;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PasswordBlacklist {
    private final Set<String> passwords = Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
            "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public boolean isPasswordInBlacklist(String pass) {
        return passwords.contains(pass);
    }
}