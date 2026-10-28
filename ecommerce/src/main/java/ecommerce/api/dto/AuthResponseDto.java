package ecommerce.api.dto;

public record AuthResponseDto(Long userId,
                              String email,
                              String fullName,
                              Long companyId,
                              String role,
                              String token) {
}
