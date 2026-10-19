package ecommerce.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class CompanyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String siret;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "payment_term_days")
    private Integer paymentTermDays = 0;

    private BigDecimal creditLimit;
}
