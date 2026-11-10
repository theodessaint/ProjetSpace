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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service 
public class JwtService {
    private final SecretKey secretKey;

    private final static Logger log = LoggerFactory.getLogger(JwtService.class);
    
    // Constructeur du chargement de la clé secrète
    public JwtService(@Value("${jwt.secret.path}") String secretPath) {
        try {

            // Récupération de la clé dans le fichier au format hexadécimal, sans espace
            String hexKey = Files.readString(Path.of(secretPath)).trim();
            
            // Conversion de la chaîne hexadécimale en octets
            byte[] keyBytes = HexFormat.of().parseHex(hexKey);
            
            // Création de la clé cryptographique à partir des octets issus de la clé privée et en utilisant l'algorithme HmacSHA256
            this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            
            log.debug("Conversion de clé privée terminée !");
            
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger la clé JWT", e);
        }
    }
    
    public SecretKey getSecretKey() {
        return secretKey;
    }

    // Ce JwtEncoder permettra de générer des jetons JWT, signés avec la clé privée
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.secretKey));
    }

    // Ce JwtDecoder permettra de valider les jetons reçus dans les requêtes HTTP, utilisé par le Filter qui se chargera de valider le jeton
    public JwtDecoder jwtDecoder(String issuer) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256) // HmacSHA256
            .build()
        ;

        // Vérification de l'émetteur du jeton - ici, dot être le nom de l'application
        decoder.setJwtValidator(
            JwtValidators.createDefaultWithIssuer(issuer)
        );

        return decoder;
    }
}
