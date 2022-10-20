package account.security;

import java.util.Optional;

public enum Role {
    ROLE_ADMINISTRATOR,
    ROLE_ACCOUNTANT,
    ROLE_USER;

    public static Optional<Role> roleFromString(String role) {
        for (Role r : values()) {
            if (r.name().equals(role)) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }
}
