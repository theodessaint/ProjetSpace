package station.security.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service 
public class JwtService {
    private final SecretKey secretKey;
    private final static Logger log = LoggerFactory.getLogger(JwtService.class);
    
    public JwtService(@Value("${jwt.secret.path}") String secretPath) {
        try {
            String hexKey = Files.readString(Path.of(secretPath)).trim();
            
            byte[] keyBytes = HexFormat.of().parseHex(hexKey);
            
            this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            
            log.debug("Conversion de clé privée terminée !");
            
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger la clé JWT", e);
        }
    }
    
    private SecretKey secretKey() {
        return secretKey;
    }
}
