package ecommerce.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product_prices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "company_id"})
})
@Getter
@Setter
public class ProductPriceJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "company_id")
    private Long companyId; // Null = prix public B2C

    @Setter
    @Column(nullable = false, name = "price_ht")
    private BigDecimal priceHt;

}
