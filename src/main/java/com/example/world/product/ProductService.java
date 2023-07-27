package com.example.world.product;


import com.example.world.DataNotFoundException;
import com.example.world.user.SiteUser;
import org.springframework.data.jpa.domain.Specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
//    public Product create(SiteUser username, String productName, String developer, String theme, String price, String content) {

    public Product create(String productName, String developer, String theme, String price, String content) {
        Product product = new Product();

//        product.setUsername(username);
        product.setProductName(productName);
        product.setDeveloper(developer);
        product.setTheme(theme);
        product.setPrice(price);
        product.setContent(content);
        product.setCreateDate(LocalDate.now());
        this.productRepository.save(product);
        return product;
    }

    public Page<Product> getTheme(int page, String key) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 16, Sort.by(sorts));
        Specification<Product> spec = searchTheme(key);
        return this.productRepository.findAll(spec, pageable);
    }

    public Specification<Product> searchTheme(String sortkey) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Season 컬럼을 기준으로 검색 조건 생성
            if (sortkey != null) {
                Path<String> seasonPath = root.get("theme");
                Predicate seasonPredicate = criteriaBuilder.equal(seasonPath, sortkey);
                predicates.add(seasonPredicate);
            }

            // 다른 조건들을 추가하고 싶다면 여기에 추가

            // 검색 조건들을 조합하여 최종 검색 조건 생성
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Page<Product> allTheme(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 16);
        return this.productRepository.findAll(pageable);
    }


    public Product getProduct(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }





}