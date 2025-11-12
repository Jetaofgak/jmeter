package com.example.tpEvaluation.repository;

import com.example.tpEvaluation.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findBySku(String sku);

    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    // Requête avec JOIN FETCH pour éviter N+1
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);
}
