package com.taskmaster.security;
import com.taskmaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return users.findByEmailIgnoreCase(email).map(u->User.withUsername(u.getEmail()).password(u.getPasswordHash()).disabled(!u.isEnabled()).authorities("ROLE_USER").build()).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
