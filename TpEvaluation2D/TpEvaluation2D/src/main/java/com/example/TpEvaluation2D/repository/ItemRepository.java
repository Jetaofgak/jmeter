package com.example.TpEvaluation2D.repository;

import com.example.TpEvaluation2D.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "items", collectionResourceRel = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    @RestResource(path = "sku", rel = "sku")
    Item findBySku(String sku);

    @RestResource(path = "by-category", rel = "by-category")
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);
}