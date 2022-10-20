package account.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeRoleDto {
    @NotBlank
    private String user;
    @NotBlank
    private String role;
    @NotNull
    private Operation operation;
}
