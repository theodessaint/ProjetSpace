package station.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {

    @Bean
    public PartieConfig partieConfig(ConfigurationLoader loader) {

        return loader.loadConfigFile(
            PartieConfig.class,
            "/config/easy_config.json"
        );
    }
}