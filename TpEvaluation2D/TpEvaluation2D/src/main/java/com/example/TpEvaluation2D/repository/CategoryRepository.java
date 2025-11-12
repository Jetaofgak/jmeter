package com.example.TpEvaluation2D.repository;

import com.example.TpEvaluation2D.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "categories", collectionResourceRel = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @RestResource(path = "code", rel = "code")
    Category findByCode(String code);
}