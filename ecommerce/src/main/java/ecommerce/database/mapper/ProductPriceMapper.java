package ecommerce.database.mapper;

import ecommerce.database.model.ProductPriceJpaEntity;
import ecommerce.domain.model.ProductPrice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPriceMapper {

    ProductPrice toDomain(ProductPriceJpaEntity entity);

    ProductPriceJpaEntity toEntity(ProductPrice domain);
}
