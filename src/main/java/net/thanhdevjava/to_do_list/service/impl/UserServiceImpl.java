package net.thanhdevjava.to_do_list.service.impl;

import net.thanhdevjava.to_do_list.dto.UserDTO;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.mapper.UserMapper;
import net.thanhdevjava.to_do_list.repository.UserRepository;
import net.thanhdevjava.to_do_list.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User foundUser = userRepository
                .findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        foundUser.setFullName(userDTO.getFullName());
        foundUser.setEmail(userDTO.getEmail());
        foundUser.setRole(userDTO.getRole());

        userRepository.save(foundUser);

        return UserMapper.toDTO(foundUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toDTO(foundUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(foundUser);
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
