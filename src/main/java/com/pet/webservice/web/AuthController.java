package com.pet.webservice.web;

import com.pet.webservice.payload.request.LoginRequest;
import com.pet.webservice.payload.request.SignupRequest;
import com.pet.webservice.payload.response.JWTTokenSuccessResponse;
import com.pet.webservice.payload.response.MessageResponse;
import com.pet.webservice.security.JWTTokenProvider;
import com.pet.webservice.security.SecurityConstants;
import com.pet.webservice.service.UserService;
import com.pet.webservice.validations.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/* this controller for users authorization */
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;
    @Autowired
    private UserService userService;

    /* create endpoint */
    /* when the user is authentication in the same way service give LoginRequest, making validation,
     * if auth hasn't errors, the service generates token and transfer it to client */
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    /* when user try to registration, client transfer request from LoginRequest.class
     * if service hasn't errors, a new user is being created and entered to the DB,
     * and return the message "User registered successfully" */

    @PostMapping("/signup") // creating URL /api/auth/signup
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidatorService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
