package station.api;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/csrf")
    public void csrf(CsrfToken csrfToken) {
        csrfToken.getToken();
    }
}
