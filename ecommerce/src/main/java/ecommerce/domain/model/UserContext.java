package ecommerce.domain.model;

import lombok.*;

@Setter
public class UserContext {
    private Long userId;
    private Long companyId;

    public UserContext(Long userId, Long companyId){
        this.userId = userId;
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}
