package com.mbe.viapdv.controller;

import com.mbe.viapdv.exception.ShortPasswordException;
import com.mbe.viapdv.exception.UserAlredyExistException;
import com.mbe.viapdv.model.auth.dto.LoginRequestDTO;
import com.mbe.viapdv.model.auth.dto.LoginResponseDTO;
import com.mbe.viapdv.model.auth.dto.RegisterDTO;
import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.user.User;
import com.mbe.viapdv.model.user.dto.BasicUserDTO;
import com.mbe.viapdv.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final IUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtEncoder jwtEncoder,
                           IUserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {

        var user = userRepository.findByEmailAndActiveTrue(loginRequest.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));
    }

    @PostMapping("/register")
    public ResponseEntity<BasicUserDTO> register(@RequestBody RegisterDTO data) {

        Optional<User> userSelected = userRepository.findByEmail(data.email());

        if(userSelected.isPresent()) {
            throw new UserAlredyExistException("A user with this username alredy exist");
        };

        if(data.password().length() < 8 && data.password() == "") throw new ShortPasswordException("The password is short, It's should be bigger than 8 caracters");

        String encryptedpassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(data.name(), data.email(), encryptedpassword);

        BasicUserDTO userCreated = new BasicUserDTO(userRepository.save(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
