package com.example.b8j2ee.service;

import com.example.b8j2ee.entity.Product;
import com.example.b8j2ee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getProducts(String keyword, Long categoryId, int pageNumber, String sortDir) {
        int pageSize = 5; // Yêu cầu 5 sản phẩm / trang
        
        Sort sort = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sortDir)) {
            sort = Sort.by("price").ascending();
        } else if ("desc".equalsIgnoreCase(sortDir)) {
            sort = Sort.by("price").descending();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        if (categoryId != null && (keyword != null && !keyword.isEmpty())) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
