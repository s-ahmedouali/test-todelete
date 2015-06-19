package com.funcoding.demo.rest.dto;

import java.io.Serializable;
import com.funcoding.demo.model.Colleague;
import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ColleagueDTO implements Serializable
{

   private Long id;
   private int version;
   private String lastName;
   private String role;

   public ColleagueDTO()
   {
   }

   public ColleagueDTO(final Colleague entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.lastName = entity.getLastName();
         this.role = entity.getRole();
      }
   }

   public Colleague fromDTO(Colleague entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Colleague();
      }
      entity.setVersion(this.version);
      entity.setLastName(this.lastName);
      entity.setRole(this.role);
      entity = em.merge(entity);
      return entity;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   public String getLastName()
   {
      return this.lastName;
   }

   public void setLastName(final String lastName)
   {
      this.lastName = lastName;
   }

   public String getRole()
   {
      return this.role;
   }

   public void setRole(final String role)
   {
      this.role = role;
   }
}