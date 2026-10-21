package ecommerce.api.dto;

import ecommerce.domain.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDto {

    private Long id;
    private String sku;
    private String name;
    private BigDecimal basePriceHt;
    private int stockQuantity;

    public static ProductResponseDto fromDomain(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.id = product.getId();
        dto.sku = product.getSku();
        dto.name = product.getName();
        dto.basePriceHt = product.getBasePriceHt();
        dto.stockQuantity = product.getStockQuantity();
        return dto;
    }
}