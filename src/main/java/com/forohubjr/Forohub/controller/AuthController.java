package com.forohubjr.Forohub.controller;

import com.forohubjr.Forohub.domain.user.User;
import com.forohubjr.Forohub.domain.user.UserAuthData;
import com.forohubjr.Forohub.infra.security.JWTtokenData;
import com.forohubjr.Forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity userAuthentication(@RequestBody @Valid UserAuthData userAuthData) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(userAuthData.mail(), userAuthData.password());
        var authenticatedUser = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generateToken((User) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new JWTtokenData(JWTtoken));

    }
}
