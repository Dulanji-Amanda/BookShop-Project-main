package lk.ijse.stock1stsemesterfinalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}