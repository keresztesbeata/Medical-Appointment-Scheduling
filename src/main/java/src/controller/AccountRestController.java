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
import src.exceptions.DuplicateDataException;
import src.exceptions.EntityNotFoundException;
import src.exceptions.InvalidDataException;
import src.mapper.AccountMapper;
import src.service.api.AccountService;
import src.service.impl.AccountServiceImpl;

@RestController
@Log4j2
public class AccountRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping(value = UrlAddressCatalogue.CURRENT_USER)
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

    @PostMapping(UrlAddressCatalogue.PERFORM_LOGIN)
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

    @PostMapping(value = UrlAddressCatalogue.PERFORM_REGISTER)
    public ResponseEntity register(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.register(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidDataException | DuplicateDataException e) {
            log.error("register: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
