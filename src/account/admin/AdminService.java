package account.admin;

import account.security.Role;
import account.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

import static account.security.Role.ROLE_ADMINISTRATOR;

@AllArgsConstructor
@Service
public class AdminService {
    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;

    public List<UserDto> getAllUsersOdderById() {
        return userMapper.usersToUserDtos(userRepository.findAllByOrderById());
    }

    public StatusDto deleteUserByEmail(String email) {
        User user = userService.getUserByEmailIgnoreCase(email);

        if (user.getRoles().contains(ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.deleteById(user.getId());

        return StatusDto
                .builder()
                .user(email)
                .status("Deleted successfully!")
                .build();
    }

    public UserDto changeUserRole(ChangeRoleDto changeRoleDto) {
        final User user = userService.getUserByEmailIgnoreCase(changeRoleDto.getUser());
        final List<Role> roles = user.getRoles();
        final Role role = Role.roleFromString("ROLE_" + changeRoleDto.getRole().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        switch (changeRoleDto.getOperation()) {
            case GRANT -> {
                if (roles.contains(role)) {
                    break;
                }
                if (role == ROLE_ADMINISTRATOR || roles.contains(ROLE_ADMINISTRATOR)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles");
                }
                user.getRoles().add(role);
            }
            case REMOVE -> {
                if (!roles.contains(role)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
                }
                if (role == ROLE_ADMINISTRATOR) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
                }
                if (roles.size() == 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
                }
                roles.remove(role);
            }
        }

        return userMapper.userToUserDto(userRepository.save(user));
    }
}
