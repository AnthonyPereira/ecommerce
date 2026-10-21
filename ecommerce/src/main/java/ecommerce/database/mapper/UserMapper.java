package ecommerce.database.mapper;

import ecommerce.database.model.UserJpaEntity;
import ecommerce.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toDomain(UserJpaEntity entity);

    UserJpaEntity toEntity(User domain);
}
