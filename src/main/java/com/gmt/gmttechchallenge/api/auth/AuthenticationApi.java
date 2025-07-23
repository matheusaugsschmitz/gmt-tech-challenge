package com.gmt.gmttechchallenge.api.auth;

import com.gmt.gmttechchallenge.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    //TOOD add @Valid jakarta
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public LoginResponse login(@RequestBody Credentials credentials) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        String token = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return new LoginResponse(token);
    }

}
