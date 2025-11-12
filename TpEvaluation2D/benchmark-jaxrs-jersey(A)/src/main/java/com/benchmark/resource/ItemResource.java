package com.benchmark.resource;

import com.benchmark.dao.CategoryDAO;
import com.benchmark.dao.ItemDAO;
import com.benchmark.model.Category;
import com.benchmark.model.Item;
import com.benchmark.util.EMF;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("0") int page,
                           @QueryParam("size") @DefaultValue("50") int size) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            List<Item> items = dao.findAll(page, size);
            long total = dao.count();

            Map<String, Object> response = new HashMap<>();
            response.put("content", items);
            response.put("page", page);
            response.put("size", size);
            response.put("totalElements", total);
            response.put("totalPages", (total + size - 1) / size);

            return Response.ok(response).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            Item item = dao.findById(id);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(item).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/sku/{sku}")
    public Response getBySku(@PathParam("sku") String sku) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            Item item = dao.findBySku(sku);
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(item).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/category/{categoryId}")
    public Response getByCategory(@PathParam("categoryId") Long categoryId,
                                  @QueryParam("page") @DefaultValue("0") int page,
                                  @QueryParam("size") @DefaultValue("50") int size) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            List<Item> items = dao.findByCategoryId(categoryId, page, size);
            long total = dao.countByCategory(categoryId);

            Map<String, Object> response = new HashMap<>();
            response.put("content", items);
            response.put("page", page);
            response.put("size", size);
            response.put("totalElements", total);
            response.put("totalPages", (total + size - 1) / size);

            return Response.ok(response).build();
        } finally {
            em.close();
        }
    }

    @POST
    public Response create(Item item) {
        EntityManager em = EMF.getEntityManager();
        try {
            // Vérifier que la catégorie existe
            if (item.getCategory() == null || item.getCategory().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            CategoryDAO categoryDAO = new CategoryDAO(em);
            Category category = categoryDAO.findById(item.getCategory().getId());
            if (category == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            item.setCategory(category);
            ItemDAO dao = new ItemDAO(em);
            Item saved = dao.save(item);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Item item) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            Item existing = dao.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            // Mise à jour de la catégorie si fournie
            if (item.getCategory() != null && item.getCategory().getId() != null) {
                CategoryDAO categoryDAO = new CategoryDAO(em);
                Category category = categoryDAO.findById(item.getCategory().getId());
                if (category != null) {
                    item.setCategory(category);
                }
            }

            item.setId(id);
            Item updated = dao.save(item);
            return Response.ok(updated).build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        EntityManager em = EMF.getEntityManager();
        try {
            ItemDAO dao = new ItemDAO(em);
            dao.delete(id);
            return Response.noContent().build();
        } finally {
            em.close();
        }
    }
}
