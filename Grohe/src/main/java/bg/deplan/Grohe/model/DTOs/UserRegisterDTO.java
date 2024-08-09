package bg.deplan.Grohe.model.DTOs;

public record UserRegisterDTO(
        String username,
        String firstName,
        String lastName,
        String email,
        String password
) {
}
