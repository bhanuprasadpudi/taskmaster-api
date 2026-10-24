package com.taskmaster.service;
import com.taskmaster.dto.AuthDtos.*;
import com.taskmaster.entity.User;
import com.taskmaster.exception.ApiException;
import com.taskmaster.repository.UserRepository;
import com.taskmaster.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor @Transactional
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final AuthenticationManager auth; private final JwtService jwt; private final CustomUserDetailsService details; private final CurrentUserService current;
    public AuthResponse register(RegisterRequest r){if(users.existsByEmailIgnoreCase(r.email()))throw new ApiException(HttpStatus.CONFLICT,"Email is already registered"); User u=users.save(User.builder().email(r.email().toLowerCase()).name(r.name()).passwordHash(encoder.encode(r.password())).build());return response(u);}
    public AuthResponse login(LoginRequest r){try{auth.authenticate(new UsernamePasswordAuthenticationToken(r.email(),r.password()));}catch(AuthenticationException e){throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid email or password");} return response(users.findByEmailIgnoreCase(r.email()).orElseThrow());}
    public UserResponse profile(){return dto(current.get());}
    public UserResponse update(UpdateProfileRequest r){User u=current.get();u.setName(r.name());return dto(users.save(u));}
    private AuthResponse response(User u){UserDetails d=details.loadUserByUsername(u.getEmail());return new AuthResponse(jwt.generate(d),dto(u));}
    private UserResponse dto(User u){return new UserResponse(u.getId(),u.getEmail(),u.getName());}
}
