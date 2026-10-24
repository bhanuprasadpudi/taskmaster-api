package com.taskmaster.service;
import com.taskmaster.entity.User;
import com.taskmaster.exception.ApiException;
import com.taskmaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository users;
    public User get(){String email=SecurityContextHolder.getContext().getAuthentication().getName();return users.findByEmailIgnoreCase(email).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Authenticated user no longer exists"));}
}
