package com.benchmark.orm.domain.product.service;

import com.benchmark.orm.domain.product.dto.ProductRequestDto;
import com.benchmark.orm.domain.product.dto.ProductResponseDto;
import com.benchmark.orm.domain.product.dto.ProductSearchDto;
import com.benchmark.orm.domain.product.entity.Product;
import com.benchmark.orm.domain.product.mapper.ProductMapper;
import com.benchmark.orm.domain.product.repository.ProductRepository;
import com.benchmark.orm.domain.product.repository.BrandRepository;
import com.benchmark.orm.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ProductService 인터페이스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponseDto saveProductJpa(ProductRequestDto productDto) {
        // DTO를 엔티티로 변환
        Product product = productDto.toEntity();

        // 엔티티 저장
        Product savedProduct = productRepository.save(product);

        // 응답 DTO 반환
        return ProductResponseDto.fromEntity(savedProduct);
    }

    @Override
    @Transactional
    public String saveProductMyBatis(ProductRequestDto productDto) {
        // DTO를 엔티티로 변환
        Product product = productDto.toEntity();

        // MyBatis를 통해 엔티티 저장
        productMapper.insert(product);

        return "Product created successfully with MyBatis";
    }

    @Override
    public Optional<ProductResponseDto> findProductByIdJpa(Long id) {
        return productRepository.findById(id)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public ProductResponseDto findProductByIdMyBatis(Long id) {
        Product product = productMapper.findById(id);
        return product != null ? ProductResponseDto.fromEntity(product) : null;
    }

    @Override
    public List<ProductResponseDto> findAllProductsJpa() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findAllProductsMyBatis() {
        return productMapper.findAll().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDto> findProductByNameJpql(String name) {
        return productRepository.findByNameJpql(name)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductByNameQueryDsl(String name) {
        return productRepository.findByName(name)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public List<ProductResponseDto> findProductsByPriceBetweenJpql(int minPrice, int maxPrice) {
        return productRepository.findByPriceBetweenJpql(minPrice, maxPrice).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsByPriceBetweenQueryDsl(int minPrice, int maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsByBrandIdJpql(Long brandId) {
        return productRepository.findByBrandIdJpql(brandId).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsByBrandIdQueryDsl(Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsByCategoryIdJpql(Long categoryId) {
        return productRepository.findByCategoryIdJpql(categoryId).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsByCategoryIdQueryDsl(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponseDto> findProductsWithPagingJpa(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDto> productDtos = productPage.getContent().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    @Override
    public Page<ProductResponseDto> findProductsWithPagingQueryDsl(Pageable pageable) {
        Page<Product> productPage = productRepository.findAllWithPaging(pageable);

        List<ProductResponseDto> productDtos = productPage.getContent().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    @Override
    public List<ProductResponseDto> findProductsWithPagingMyBatis(int offset, int limit) {
        return productMapper.findAllWithPaging(offset, limit).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsWithSortingJpa(Sort sort) {
        return productRepository.findAll(sort).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsWithSortingQueryDsl(Sort sort) {
        return productRepository.findAllWithSorting(sort).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findProductsWithSortingMyBatis(String sortColumn, String sortDirection) {
        return productMapper.findAllWithSorting(sortColumn, sortDirection).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponseDto> findProductsWithPagingAndSortingJpa(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDto> productDtos = productPage.getContent().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
    }

    @Override
    public List<ProductResponseDto> findProductsWithPagingAndSortingMyBatis(int offset, int limit, String sortColumn, String sortDirection) {
        return productMapper.findAllWithPagingAndSorting(offset, limit, sortColumn, sortDirection).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDto> findProductWithBrandJpql(Long productId) {
        return productRepository.findProductWithBrandJpql(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithBrandQueryDsl(Long productId) {
        return productRepository.findProductWithBrand(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithCategoryJpql(Long productId) {
        return productRepository.findProductWithCategoryJpql(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithCategoryQueryDsl(Long productId) {
        return productRepository.findProductWithCategory(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithImagesJpql(Long productId) {
        return productRepository.findProductWithImagesJpql(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithImagesQueryDsl(Long productId) {
        return productRepository.findProductWithImages(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public Optional<ProductResponseDto> findProductWithAllDetailsQueryDsl(Long productId) {
        return productRepository.findProductWithAllDetails(productId)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public List<ProductResponseDto> searchProductsByKeywordJpql(String keyword) {
        return productRepository.searchProductsByKeywordJpql(keyword).stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> searchProducts(ProductSearchDto searchDto) {
        // 검색 조건에 따라 동적으로 조회
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
            return searchProductsByKeywordJpql(searchDto.getKeyword());
        } else if (searchDto.getMinPrice() != null && searchDto.getMaxPrice() != null) {
            return findProductsByPriceBetweenQueryDsl(searchDto.getMinPrice(), searchDto.getMaxPrice());
        } else if (searchDto.getBrandId() != null) {
            return findProductsByBrandIdQueryDsl(searchDto.getBrandId());
        } else if (searchDto.getCategoryId() != null) {
            return findProductsByCategoryIdQueryDsl(searchDto.getCategoryId());
        } else {
            // 기본적으로 모든 상품 반환
            return findAllProductsJpa();
        }
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductJpa(Long id, ProductRequestDto productDto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // DTO를 엔티티로 변환 (ID 설정)
                    Product updatedProduct = productDto.toEntity();

                    // ID 설정을 위한 Reflection 사용
                    try {
                        java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(updatedProduct, id);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to set Product ID", e);
                    }

                    // 업데이트된 상품 저장
                    Product savedProduct = productRepository.save(updatedProduct);
                    return ProductResponseDto.fromEntity(savedProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public String updateProductMyBatis(Long id, ProductRequestDto productDto) {
        Product existingProduct = productMapper.findById(id);

        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        // DTO를 엔티티로 변환 (ID 설정)
        Product updatedProduct = productDto.toEntity();

        // ID 설정을 위한 Reflection 사용
        try {
            java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(updatedProduct, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set Product ID", e);
        }

        // MyBatis를 통해 상품 업데이트
        productMapper.update(updatedProduct);

        return "Product updated successfully with MyBatis";
    }

    @Override
    @Transactional
    public String deleteProductJpa(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(id);
                    return "Product deleted successfully with JPA";
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public String deleteProductMyBatis(Long id) {
        Product existingProduct = productMapper.findById(id);

        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productMapper.deleteById(id);
        return "Product deleted successfully with MyBatis";
    }
}