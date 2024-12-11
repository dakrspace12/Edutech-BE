package edutech.backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private Set<String> roles;
}