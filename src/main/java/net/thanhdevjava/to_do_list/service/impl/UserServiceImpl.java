package net.thanhdevjava.to_do_list.service.impl;

import net.thanhdevjava.to_do_list.dto.UserDTO;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.entity.VerificationToken;
import net.thanhdevjava.to_do_list.mapper.UserMapper;
import net.thanhdevjava.to_do_list.repository.TokenRepository;
import net.thanhdevjava.to_do_list.repository.UserRepository;
import net.thanhdevjava.to_do_list.service.EmailService;
import net.thanhdevjava.to_do_list.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
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

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);

        String url = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Xác thực tài khoản",
                "Nhấn vào link để xác thực: <a href=\"" + url + "\">Xác thực</a>"
        );
    }
}
