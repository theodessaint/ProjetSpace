package station.config.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import station.service.AstronauteService;

@Component
public class AstronauteReferenceInitializer implements ApplicationRunner {

    private final AstronauteService AstronauteService;

    public AstronauteReferenceInitializer(AstronauteService AstronauteService) {
        this.AstronauteService = AstronauteService;
    }

    @Override
    public void run(ApplicationArguments args) {
        AstronauteService.initialiserCatalogueAstronautes();
    }
}
