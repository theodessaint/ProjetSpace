package station.api.response;

public record AuthResponse(boolean success, String token, String role) {}