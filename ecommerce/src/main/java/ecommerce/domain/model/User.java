package ecommerce.domain.model;

import lombok.*;

@Setter
@Getter
public class User {
    private Long userId;
    private Long companyId;
    private String firstName;
    private String lastName;
    private String email;

    public User(Long userId, Long companyId, String firstName, String lastName, String email){
        this.userId = userId;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
