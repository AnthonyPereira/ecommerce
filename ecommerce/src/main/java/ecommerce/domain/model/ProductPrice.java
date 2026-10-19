package ecommerce.domain.model;

import lombok.Builder;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
public class ProductPrice {
    private Long id;
    private Long productId;
    private Long companyId; // Null si prix public (B2C)
    private BigDecimal priceHt;

    public ProductPrice(Long id, Long productId, Long companyId, BigDecimal priceHt){
        this.id = id;
        this.productId = productId;
        this.companyId = companyId;
        this.priceHt = priceHt;
    }

    public BigDecimal getPriceHt() {
        return priceHt;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getId() {
        return id;
    }
}
