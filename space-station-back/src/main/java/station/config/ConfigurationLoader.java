package station.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

@Component
public class ConfigurationLoader {
    
    private final ObjectMapper mapper;
    
    public ConfigurationLoader(ObjectMapper mapper) {
        this.mapper = JsonMapper.builder()
        .disable(EnumFeature.READ_ENUMS_USING_TO_STRING)
        .disable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
        .build();
    }
    
    public <T> T loadConfigFile(Class<T> type, String pathFile) {
        
        try (InputStream input = getClass().getResourceAsStream(pathFile)) {
            
            if (input == null) {
                throw new IllegalArgumentException(
                    "Fichier introuvable : " + pathFile
                );
            }
            
            return mapper.readValue(input, type);
            
        } catch (IOException e) {
            throw new IllegalStateException(
                "Impossible de charger le fichier : " + pathFile, e
            );
        }
    }
}