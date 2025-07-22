package com.gmt.gmttechchallenge.api;

import com.gmt.gmttechchallenge.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    //@Valid jakarta
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public void login(@RequestBody Credentials credentials){
        var authenticationToken = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        var auth = authenticationManager.authenticate(authenticationToken);
        // tá, mas e o token porra?
    }

    public record Credentials(String username, String password){}
}
