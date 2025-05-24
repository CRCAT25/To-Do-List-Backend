package net.thanhdevjava.to_do_list.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
}
