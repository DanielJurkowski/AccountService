package accountservice.user;


import accountservice.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "\"User\"")
public class User {
    @Id
    @GeneratedValue()
    private BigInteger id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderBy
    private List<Role> roles;
    private int loginFailCount;
    private boolean isAccountNonLocked;
    private LocalDate lockedAt;
}
