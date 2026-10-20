package ecommerce.database.mapper;

import ecommerce.database.model.ProductJpaEntity;
import ecommerce.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toDomain(ProductJpaEntity entity);

    ProductJpaEntity toEntity(Product domain);
}
