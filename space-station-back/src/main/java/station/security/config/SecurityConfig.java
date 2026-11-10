package station.security.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;

import station.security.service.JwtService;

@Configuration 
public class SecurityConfig {
    private JwtService jwtService;
    // private static final String SECRET_KEY = jwtService.secretKey();

    private SecretKey secretKey() {
        return this.jwtService.secretKey();
    }
}
