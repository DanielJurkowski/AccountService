package accountservice.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SecurityEventDto {
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigInteger id;
    @NotNull
    private Date date;
    @NotNull
    private Event action;
    @NotBlank
    private String subject;
    @NotBlank
    private String object;
    @NotBlank
    private String path;
}