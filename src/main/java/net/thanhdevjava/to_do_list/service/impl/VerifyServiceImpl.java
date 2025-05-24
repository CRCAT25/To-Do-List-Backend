package net.thanhdevjava.to_do_list.service.impl;

import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.entity.VerificationToken;
import net.thanhdevjava.to_do_list.repository.TokenRepository;
import net.thanhdevjava.to_do_list.repository.UserRepository;
import net.thanhdevjava.to_do_list.service.VerifyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerifyServiceImpl implements VerifyService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public VerifyServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean verifyToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        // if token does not exist
        if (verificationToken == null) {
            return false;
        }

        // if token is expired
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setStatus("active");

        userRepository.save(user);

        tokenRepository.delete(verificationToken); // xoá token sau khi dùng

        return true;
    }
}
