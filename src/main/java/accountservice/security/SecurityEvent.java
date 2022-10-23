package accountservice.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class SecurityEvent {
    @Id
    @GeneratedValue
    private BigInteger id;
    private Date date;
    private Event action;
    private String subject;
    private String object;
    private String path;
}
