package ecommerce.database.mapper;

import ecommerce.database.model.ProductPriceJpaEntity;
import ecommerce.domain.model.ProductPrice;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPriceMapperTest {

    // Récupération de l'instance MapStruct générée
    private final ProductPriceMapper mapper = Mappers.getMapper(ProductPriceMapper.class);

    @Test
    void should_map_jpa_entity_to_domain_model() {
        // Given
        ProductPriceJpaEntity jpaEntity = new ProductPriceJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setPriceHt(new BigDecimal("2150.00"));
        ProductPrice domainModel = mapper.toDomain(jpaEntity);

        // Then
        assertThat(domainModel).isNotNull();
        assertThat(domainModel.getPriceHt()).isEqualByComparingTo("2150.00");
    }

    @Test
    void should_map_domain_model_to_jpa_entity() {
        // Given (Test inverse si votre mapper le propose, ex: toEntity)
        ProductPrice domainModel = new ProductPrice(1L, 1L, 1L, new BigDecimal("2150.00"));

        ProductPriceJpaEntity jpaEntity = mapper.toEntity(domainModel);

        assertThat(jpaEntity).isNotNull();
        assertThat(jpaEntity.getPriceHt()).isEqualByComparingTo("2150.00");
    }
}
