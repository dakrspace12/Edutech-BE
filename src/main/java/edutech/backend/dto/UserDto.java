package edutech.backend.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String mobile_no;
    private String name;
}