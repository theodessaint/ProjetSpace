package station.api.request;

import jakarta.validation.constraints.NotBlank;

public record AuthOrSubscriptionRequest(
    @NotBlank(message = "Le login est obligatoire") String username, 
    @NotBlank(message = "Le mot de passe est obligatoire") String password) {}
