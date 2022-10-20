package account.user;


import account.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private CurrentUser currentUser;
    private BCryptPasswordEncoder passwordEncoder;
    private PasswordBlacklist passwordBlacklist;

    public UserDto signup(UserDto userDto) {
        throwIfUserExistsByEmailIgnoreCase(userDto.getEmail());
        throwIfPasswordInBlackList(userDto.getPassword());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setEmail(userDto.getEmail().toLowerCase());
        userDto.setRoles(List.of(getInitialRole()));

        User user = userRepository.save(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(user);
    }

    public StatusDto changePass(PasswordDto passDto) {
        User currUser = currentUser.getCurrentUser().getUserEntity();

        throwIfPasswordsMatch(passDto.getNew_password(), currUser.getPassword());
        throwIfPasswordInBlackList(passDto.getNew_password());

        currUser.setPassword(passwordEncoder.encode(passDto.getNew_password()));
        userRepository.save(currUser);

        return new StatusDto(currUser.getEmail(), "The password has been updated successfully");
    }

    public User getUserByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    private Role getInitialRole() {
        return userRepository.count() == 0 ? Role.ROLE_ADMINISTRATOR : Role.ROLE_USER;
    }

    private void throwIfPasswordsMatch(String rawPassword, String encodedPass) {
        if (passwordEncoder.matches(rawPassword, encodedPass)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be different!");
        }
    }

    private void throwIfUserExistsByEmailIgnoreCase(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
    }

    private void throwIfPasswordInBlackList(String password) {
        if (passwordBlacklist.isPasswordInBlacklist(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
    }
}