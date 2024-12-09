package edutech.backend.dto;
import lombok.Builder;
import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Data
@Builder
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String mobile_no;
    private Set<String> role;

    // Builder logic to ensure role is never null
    public Set<String> getRole() {
        return (role != null) ? role : new HashSet<>();
    }
}
