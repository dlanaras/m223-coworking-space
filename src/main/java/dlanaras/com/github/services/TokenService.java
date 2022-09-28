package dlanaras.com.github.services;

import java.util.Arrays;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.jwt.Claims;

import dlanaras.com.github.models.User;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class TokenService {
    public String createToken(User user) {
        String token = Jwt.issuer("https://github.com/dlanaras")
                .upn(user.getId().toString())
                .groups(new HashSet<>(Arrays.asList(user.isAdmin() ? "Admin" : "User")))
                .claim(Claims.email.name(), user.getEmail())
                .sign();
        return token;
    }
}
