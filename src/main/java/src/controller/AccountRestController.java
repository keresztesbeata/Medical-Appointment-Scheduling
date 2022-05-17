package src.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import src.config.JwtTokenProvider;
import src.dto.AccountDTO;
import src.dto.UserProfileDTO;
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.service.api.UserService;

@RestController
@Log4j2
public class AccountRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping(value = "/current_user")
    @ResponseBody
    public ResponseEntity getLoggedInUser() {
        try {
            AccountMapper accountMapper = new AccountMapper();
            return ResponseEntity.ok().body(accountMapper.mapToDto(Utils.getCurrentUserAccount()));
        } catch (EntityNotFoundException e) {
            log.warn("getLoggedInUser: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity getUserByUsername(@PathVariable String username) {
        return ResponseEntity.of(userService.findAccountByUsername(username));
    }

    @PostMapping("/perform_login")
    public ResponseEntity authenticateUser(@RequestBody AccountDTO accountDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            accountDTO.getUsername(),
                            accountDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            return ResponseEntity.ok().body(new JwtAuthenticationResponse(jwt));
        } catch (AuthenticationException e) {
            log.error("authenticateUser: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping(value = "/perform_register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        try {
            UserProfileDTO savedProfile = userService.register(registerRequest.getAccountDTO(), registerRequest.getUserProfileDTO());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
        } catch (InvalidDataException | DuplicateDataException e) {
            log.error("register: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
