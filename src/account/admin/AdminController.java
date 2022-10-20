package account.admin;

import account.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @Secured("ROLE_ADMIN")
    @GetMapping("api/admin/user")
    public List<UserDto> getAllUserOrderById() {
        return adminService.getAllUsersOdderById();
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("api/admin/user/{email}")
    public StatusDto deleteUserByEmail(@PathVariable String email) {
        return adminService.deleteUserByEmail(email);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("api/admin/user/role")
    public UserDto changeUserRole(@RequestBody(required = false) @Valid ChangeRoleDto changeRoleDto){
        return adminService.changeUserRole(changeRoleDto);
    }
}
