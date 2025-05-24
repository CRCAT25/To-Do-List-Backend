package net.thanhdevjava.to_do_list.repository;

import net.thanhdevjava.to_do_list.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
