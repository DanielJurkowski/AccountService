package accountservice.admin;


import accountservice.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @GetMapping("api/admin/user")
    public List<UserDto> getAllUsersOrderById() {
        return adminService.getAllUsersOrderById();
    }

    @DeleteMapping("api/admin/user/{email}")
    public StatusDto deleteUserByEmail(@PathVariable String email) {
        return adminService.deleteUserByEmail(email);
    }

    @DeleteMapping("api/admin/user")
    public void deleteUserByEmail() {
    }

    @PutMapping("api/admin/user/role")
    public UserDto changeUserRole(@RequestBody(required = false) @Valid ChangeRoleDto changeRoleDto) {
        return adminService.changeUserRole(changeRoleDto);
    }

    @PutMapping("api/admin/user/access")
    // used StatusDto from payment package to do not replicate code => just status message
    public accountservice.payment.StatusDto changeAccess(@RequestBody @Valid ChangeAccessDto changeAccessDto) {
        return adminService.changeAccess(changeAccessDto);
    }
}