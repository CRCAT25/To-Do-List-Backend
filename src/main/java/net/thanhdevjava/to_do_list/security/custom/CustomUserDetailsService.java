package net.thanhdevjava.to_do_list.security.custom;

import net.thanhdevjava.to_do_list.repository.UserRepository;
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
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),               // Username from database
                        user.getPassword(),               // Encrypted password from database
                        List.of(new SimpleGrantedAuthority(user.getRole())) // Convert user's role to Spring Security authority
                ))
                // If user not found in database, throw exception
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

