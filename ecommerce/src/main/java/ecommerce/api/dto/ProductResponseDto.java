package ecommerce.api.dto;

import ecommerce.domain.model.Product;
import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;
    private String sku;
    private String name;
    private BigDecimal basePriceHt;
    private int stockQuantity;

    // Méthode de mapping pratique depuis le modèle du domaine vers le DTO
    public static ProductResponseDto fromDomain(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.id = product.getId();
        dto.sku = product.getSku();
        dto.name = product.getName();
        dto.basePriceHt = product.getBasePriceHt();
        dto.stockQuantity = product.getStockQuantity();
        return dto;
    }

    // Getters et Setters (ou utilisez Lombok avec @Data / @Getter / @Setter)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getBasePriceHt() { return basePriceHt; }
    public void setBasePriceHt(BigDecimal basePriceHt) { this.basePriceHt = basePriceHt; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}