package station.config.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import station.service.BonusService;

@Component
public class BonusInitializer implements ApplicationRunner {

    private final BonusService bonusService;

    public BonusInitializer(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @Override
    public void run(ApplicationArguments args) {
        bonusService.initialiserBonus();
    }
}