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
public class Company {
    private Long id;
    private String name;
    private String siret;
    private String vatNumber;
    private Integer paymentTermDays;
    private BigDecimal creditLimit;

}
