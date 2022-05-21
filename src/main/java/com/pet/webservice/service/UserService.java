package com.pet.webservice.service;

import com.pet.webservice.entity.User;
import com.pet.webservice.entity.enums.ERole;
import com.pet.webservice.exceptions.UserExistException;
import com.pet.webservice.payload.request.SignupRequest;
import com.pet.webservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/* service for creating a new user */
@Service
public record UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService {
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        /* get everything from client request*/
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword())); // get the pass from client and encode this
        user.getRole().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials.");
        }
    }
}
