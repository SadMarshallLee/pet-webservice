package com.pet.webservice.service;

import com.pet.webservice.entity.User;
import com.pet.webservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @param userRepository create user's repository
 */
@Service
public record CustomUserDetailsService(UserRepository userRepository) implements UserDetailsService {

    @Autowired
    public CustomUserDetailsService {
    }

    /* method must return user */
    @Override
    public UserDetails loadUserByUsername(String username) {
        /* find user by email */
        User user = userRepository.findUserByEmail(username).
                orElseThrow(() -> new UsernameNotFoundException("Username not found with username: "
                        + username));
        return null;
    }

    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }

    public static User build(User user) {
        /* get authorities */
        List<GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        /* return needed user */
        return new User(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
