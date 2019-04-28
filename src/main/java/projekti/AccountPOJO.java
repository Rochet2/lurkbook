package projekti;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPOJO {

    @Size(min = 3, max = 255)
    private String username;
    @Size(min = 8, max = 255)
    private String password;
    @NotEmpty
    private String nickname;
    @Size(min = 4, max = 255)
    private String profileurl;
}
