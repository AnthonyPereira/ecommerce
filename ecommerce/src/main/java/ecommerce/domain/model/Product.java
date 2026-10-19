package ecommerce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Product {
    private Long id;
    private String sku;
    private String name;
    private BigDecimal basePriceHt;
    private int stockQuantity;

}
