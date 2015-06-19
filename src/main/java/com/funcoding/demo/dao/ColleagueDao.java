package com.funcoding.demo.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.funcoding.demo.model.Colleague;

/**
 *  DAO for Colleague
 */
@Stateless
public class ColleagueDao
{
   @PersistenceContext(unitName = "rest-sample-persistence-unit")
   private EntityManager em;

   public void create(Colleague entity)
   {
      //[BR1] add comment BR1
	  em.persist(entity);
   }

   public void deleteById(Long id)
   {
      Colleague entity = em.find(Colleague.class, id);
      if (entity != null)
      {
         em.remove(entity);
      }
   }

   public Colleague findById(Long id)
   {
      return em.find(Colleague.class, id);
   }

   public Colleague update(Colleague entity)
   {
      return em.merge(entity);
   }

   public List<Colleague> listAll(Integer startPosition, Integer maxResult)
   {
      //evol1
	  TypedQuery<Colleague> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Colleague c ORDER BY c.id", Colleague.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      return findAllQuery.getResultList();
   }
}
