package accountservice.admin;


import accountservice.auditor.AuditorService;
import accountservice.security.Event;
import accountservice.security.SecurityEvent;
import accountservice.security.Role;
import accountservice.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AdminService {
    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;
    private AuditorService auditorService;
    private CurrentUser currentUser;

    public List<UserDto> getAllUsersOrderById() {
        return userMapper.usersToUserDtos(userRepository.findAllByOrderById());
    }

    public StatusDto deleteUserByEmail(String email) {
        User user = userService.getUserByEmailIgnoreCase(email);

        if (user.getRoles().contains(Role.ROLE_ADMINISTRATOR))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");

        userRepository.deleteById(user.getId());

        auditorService.saveSecurityEvent(SecurityEvent
                .builder()
                .action(Event.DELETE_USER)
                .subject(currentUser.getCurrentUser().getUsername())
                .object(user.getEmail())
                .build());

        return StatusDto
                .builder()
                .user(email)
                .status("Deleted successfully!")
                .build();
    }

    public UserDto changeUserRole(ChangeRoleDto changeRoleDto) {
        final User user = userService.getUserByEmailIgnoreCase(changeRoleDto.getUser());
        final List<Role> roles = user.getRoles();
        final Role role = Role
                .roleFromString("ROLE_" + changeRoleDto.getRole().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        switch (changeRoleDto.getOperation()) {
            case GRANT -> {
                if (roles.contains(role)) {
                    break;
                }
                if (role == Role.ROLE_ADMINISTRATOR || roles.contains(Role.ROLE_ADMINISTRATOR)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
                }

                auditorService.saveSecurityEvent(SecurityEvent
                        .builder()
                        .action(Event.GRANT_ROLE)
                        .subject(currentUser.getCurrentUser().getUsername())
                        .object("Grant role " + changeRoleDto.getRole() + " to " + user.getEmail())
                        .build());

                user.getRoles().add(role);
            }

            case REMOVE -> {
                if (!roles.contains(role)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
                }
                if (role == Role.ROLE_ADMINISTRATOR) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
                }
                if (roles.size() == 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
                }

                auditorService.saveSecurityEvent(SecurityEvent
                        .builder()
                        .action(Event.REMOVE_ROLE)
                        .subject(currentUser.getCurrentUser().getUsername())
                        .object("Remove role " + changeRoleDto.getRole() + " from " + user.getEmail())
                        .build());

                user.getRoles().remove(role);
            }
        }

        return userMapper.userToUserDto(userRepository.save(user));
    }

    // used StatusDto from payment package to do not replicate code => just status message
    public accountservice.payment.StatusDto changeAccess(ChangeAccessDto changeAccessDto) {
        User user = userService.getUserByEmailIgnoreCase(changeAccessDto.getUser());

        if (changeAccessDto.getOperation() == AccessOperation.LOCK && user.getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }

        user.setAccountNonLocked(changeAccessDto.getOperation().boolValue);
        userRepository.save(user);

        SecurityEvent securityEvent = SecurityEvent
                .builder()
                .subject(currentUser.getCurrentUser().getUsername())
                .build();

        switch (changeAccessDto.getOperation()) {
            case LOCK -> {
                securityEvent.setAction(Event.LOCK_USER);
                securityEvent.setObject("Lock user " + user.getEmail());
            }
            case UNLOCK -> {
                securityEvent.setAction(Event.UNLOCK_USER);
                securityEvent.setObject("Unlock user " + user.getEmail());
            }
        }

        auditorService.saveSecurityEvent(securityEvent);

        return new accountservice.payment.StatusDto(String.format("User %s %s!", user.getEmail(), changeAccessDto.getOperation().stringValue));
    }
}