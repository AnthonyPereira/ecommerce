package ecommerce.database.mapper;

import ecommerce.database.model.RoleJpaEntity;
import ecommerce.database.model.UserJpaEntity;
import ecommerce.domain.model.Role;
import ecommerce.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "role", source = "role", qualifiedByName = "toDomainRole")
    User toDomain(UserJpaEntity entity);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "toJpaRole")
    UserJpaEntity toEntity(User domain);

    @Named("toDomainRole")
    default Role toDomainRole(RoleJpaEntity role) {
        if (role == null || role.getCode() == null) {
            return null;
        }
        return Role.valueOf(role.getCode());
    }

    @Named("toJpaRole")
    default RoleJpaEntity toJpaRole(Role role) {
        if (role == null) {
            return null;
        }
        return RoleJpaEntity.builder()
                .code(role.name())
                .build();
    }
}
