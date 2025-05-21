package net.thanhdevjava.to_do_list.service;

import net.thanhdevjava.to_do_list.dto.ResponseDTO;
import net.thanhdevjava.to_do_list.dto.UserDTO;
import net.thanhdevjava.to_do_list.entity.User;

import java.util.List;

public interface UserService {
    UserDTO updateUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    void deleteUserById(Long id);

    List<UserDTO> getUsers();

    boolean existsByUsername(String username);

    void save (User user);
}
