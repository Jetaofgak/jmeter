package com.benchmark.dao;

import com.benchmark.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CategoryDAO {

    private EntityManager em;

    public CategoryDAO(EntityManager em) {
        this.em = em;
    }

    public List<Category> findAll(int page, int size) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c ORDER BY c.id", Category.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                .getSingleResult();
    }

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public Category findByCode(String code) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c WHERE c.code = :code", Category.class);
        query.setParameter("code", code);
        List<Category> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public Category save(Category category) {
        em.getTransaction().begin();
        try {
            if (category.getId() == null) {
                em.persist(category);
            } else {
                category = em.merge(category);
            }
            em.getTransaction().commit();
            return category;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        try {
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
