package com.pet.webservice.service;

import com.pet.webservice.dto.UserDTO;
import com.pet.webservice.entity.User;
import com.pet.webservice.entity.enums.ERole;
import com.pet.webservice.exceptions.UserExistException;
import com.pet.webservice.payload.request.SignupRequest;
import com.pet.webservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/* service for creating a new user */
@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRole().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    /* Principal is currently logged-in user, but it retrieves through security context
    * which is bound to the current thread and as such it's also bound to the current request and its session*/
    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);

        /* set data for user from userDTO */
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setUsername(userDTO.getUsername());
        user.setBio(userDTO.getBio());
        user.setUsername(userDTO.getUsername());

        /* save updated user to DB */
        return userRepository.save(user);
    }

    public User getCurrentUser (Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();

        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username with " + username + " username wasn't found"));
    }
}
