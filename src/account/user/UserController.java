package account.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    @PostMapping("api/auth/signup")
    public UserDto signup(@RequestBody @Valid UserDto userDto) {
        return userService.signup(userDto);
    }

    @Secured({"ROLE_ACCOUNTANT", "ROLE_USER", "ROLE_ADMINISTRATOR"})
    @PostMapping("api/auth/changepass")
    public StatusDto changePass(@RequestBody @Valid PasswordDto passwordDto) {
        return userService.changePass(passwordDto);
    }
}