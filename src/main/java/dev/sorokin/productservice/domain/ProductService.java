package dev.sorokin.productservice.domain;


import dev.sorokin.productservice.api.ProductCreateRequest;
import dev.sorokin.productservice.api.ProductUpdateRequest;
import dev.sorokin.productservice.domain.db.ProductEntity;

public interface ProductService {
    ProductEntity create(ProductCreateRequest createRequest);
    ProductEntity update(Long id, ProductUpdateRequest updateRequest);
    ProductEntity getById(Long id);
    void delete(Long id);
}
