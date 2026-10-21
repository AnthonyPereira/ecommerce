package ecommerce.database.adapter;

import ecommerce.database.mapper.ProductMapper;
import ecommerce.database.ProductRepository;
import ecommerce.domain.model.Product;
import ecommerce.domain.port.ProductPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductAdapter(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Product> findByIds(List<Long> productIds) {
        return repository.findAllById(productIds).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return repository.findBySku(sku).map(mapper::toDomain);
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}