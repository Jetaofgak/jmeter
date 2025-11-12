package com.benchmark.dao;

import com.benchmark.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ItemDAO {

    private EntityManager em;

    public ItemDAO(EntityManager em) {
        this.em = em;
    }

    public List<Item> findAll(int page, int size) {
        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM Item i ORDER BY i.id", Item.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(i) FROM Item i", Long.class)
                .getSingleResult();
    }

    public Item findById(Long id) {
        return em.find(Item.class, id);
    }

    public Item findBySku(String sku) {
        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM Item i WHERE i.sku = :sku", Item.class);
        query.setParameter("sku", sku);
        List<Item> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Item> findByCategoryId(Long categoryId, int page, int size) {
        TypedQuery<Item> query = em.createQuery(
                "SELECT i FROM Item i WHERE i.category.id = :categoryId ORDER BY i.id",
                Item.class);
        query.setParameter("categoryId", categoryId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long countByCategory(Long categoryId) {
        return em.createQuery(
                        "SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId", Long.class)
                .setParameter("categoryId", categoryId)
                .getSingleResult();
    }

    public Item save(Item item) {
        em.getTransaction().begin();
        try {
            if (item.getId() == null) {
                em.persist(item);
            } else {
                item = em.merge(item);
            }
            em.getTransaction().commit();
            return item;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        try {
            Item item = em.find(Item.class, id);
            if (item != null) {
                em.remove(item);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
