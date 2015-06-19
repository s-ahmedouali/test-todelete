package com.funcoding.demo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import com.funcoding.demo.rest.dto.ColleagueDTO;
import com.funcoding.demo.model.Colleague;

/**
 * 
 */
@Stateless
@Path("/colleagues")
public class ColleagueEndpoint
{
   @PersistenceContext(unitName = "rest-sample-persistence-unit")
   private EntityManager em;

   @POST
   @Consumes({ "application/json", "application/xml" })
   public Response create(ColleagueDTO dto)
   {
      Colleague entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(ColleagueEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Colleague entity = em.find(Colleague.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces({ "application/json", "application/xml" })
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Colleague> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Colleague c WHERE c.id = :entityId ORDER BY c.id", Colleague.class);
      findByIdQuery.setParameter("entityId", id);
      Colleague entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      ColleagueDTO dto = new ColleagueDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces({ "application/json", "application/xml" })
   public List<ColleagueDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Colleague> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Colleague c ORDER BY c.id", Colleague.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Colleague> searchResults = findAllQuery.getResultList();
      final List<ColleagueDTO> results = new ArrayList<ColleagueDTO>();
      for (Colleague searchResult : searchResults)
      {
         ColleagueDTO dto = new ColleagueDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes({ "application/json", "application/xml" })
   public Response update(@PathParam("id") Long id, ColleagueDTO dto)
   {
      if (dto == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      if (!id.equals(dto.getId()))
      {
         return Response.status(Status.CONFLICT).entity(dto).build();
      }
      Colleague entity = em.find(Colleague.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      entity = dto.fromDTO(entity, em);
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
      }
      return Response.noContent().build();
   }
}
