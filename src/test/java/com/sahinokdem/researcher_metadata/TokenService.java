package com.sahinokdem.researcher_metadata;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TokenService {
    private final JwtService jwtService;

    public String getTokenFor(User user) {
        return jwtService.createToken(user.getId());
    }
}
