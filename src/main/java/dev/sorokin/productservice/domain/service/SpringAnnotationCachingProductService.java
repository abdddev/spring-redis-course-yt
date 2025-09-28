package dev.sorokin.productservice.domain.service;

import dev.sorokin.productservice.api.ProductCreateRequest;
import dev.sorokin.productservice.api.ProductUpdateRequest;
import dev.sorokin.productservice.domain.ProductService;
import dev.sorokin.productservice.domain.db.ProductEntity;
import dev.sorokin.productservice.domain.db.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SpringAnnotationCachingProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductEntity create(ProductCreateRequest createRequest) {
        log.info("Creating product in DB: {}", createRequest.name());
        ProductEntity product = ProductEntity.builder()
                .name(createRequest.name())
                .price(createRequest.price())
                .description(createRequest.description())
                .build();
        return productRepository.save(product);
    }

    @CacheEvict(
            value = "product",
            key = "#id"
    )
    @Override
    public ProductEntity update(Long id, ProductUpdateRequest updateRequest) {
        log.info("Updating product in DB: {}", id);
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        if (updateRequest.price() != null) {
            product.setPrice(updateRequest.price());
        }
        if (updateRequest.description() != null) {
            product.setDescription(updateRequest.description());
        }

        return productRepository.save(product);
    }

    @Cacheable(
            value = "product",
            key = "#id"
    )
    @Override
    public ProductEntity getById(Long id) {
        log.info("Getting product from DB: id={}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @CacheEvict(
            value = "product",
            key = "#id"
    )
    @Override
    public void delete(Long id) {
        log.info("Deleting product from DB: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
