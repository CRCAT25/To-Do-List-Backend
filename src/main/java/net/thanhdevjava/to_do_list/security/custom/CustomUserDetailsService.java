package net.thanhdevjava.to_do_list.security.custom;

import net.thanhdevjava.to_do_list.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // This method is used by Spring Security to load user data during login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (!"active".equalsIgnoreCase(user.getStatus())) {
                        throw new DisabledException("User account has not been activated.");
                    }

                    return new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            List.of(new SimpleGrantedAuthority(user.getRole()))
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

