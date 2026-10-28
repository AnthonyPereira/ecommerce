
package ecommerce.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long Id;
    private Long companyId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public User(
            Long Id,
            Long companyId,
            String firstName,
            String lastName,
            String email,
            Role role
    ) {
        this.Id = Id;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
