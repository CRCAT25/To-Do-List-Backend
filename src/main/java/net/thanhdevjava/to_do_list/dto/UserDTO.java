package net.thanhdevjava.to_do_list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private List<TaskDTO> tasks;
    private String status;
}
