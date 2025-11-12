package com.benchmark.resource;

import com.benchmark.dao.CategoryDAO;
import com.benchmark.model.Category;
import com.benchmark.util.EMF;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("0") int page,
                           @QueryParam("size") @DefaultValue("50") int size) {
        EntityManager em = EMF.getEntityManager();
        try {
            CategoryDAO dao = new CategoryDAO(em);
            List<Category> categories = dao.findAll(page, size);
            long total = dao.count();

            Map<String, Object> response = new HashMap<>();
            response.put("content", categories);
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
            CategoryDAO dao = new CategoryDAO(em);
            Category category = dao.findById(id);
            if (category == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(category).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/code/{code}")
    public Response getByCode(@PathParam("code") String code) {
        EntityManager em = EMF.getEntityManager();
        try {
            CategoryDAO dao = new CategoryDAO(em);
            Category category = dao.findByCode(code);
            if (category == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(category).build();
        } finally {
            em.close();
        }
    }

    @POST
    public Response create(Category category) {
        EntityManager em = EMF.getEntityManager();
        try {
            CategoryDAO dao = new CategoryDAO(em);
            Category saved = dao.save(category);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Category category) {
        EntityManager em = EMF.getEntityManager();
        try {
            CategoryDAO dao = new CategoryDAO(em);
            Category existing = dao.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            category.setId(id);
            Category updated = dao.save(category);
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
            CategoryDAO dao = new CategoryDAO(em);
            dao.delete(id);
            return Response.noContent().build();
        } finally {
            em.close();
        }
    }
}
