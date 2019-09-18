/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Datos.exceptions.NonexistentEntityException;
import Datos.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica_negocio.Encargado;
import Logica_negocio.TelefonoEncargado;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class TelefonoEncargadoJpaController implements Serializable {

    public TelefonoEncargadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TelefonoEncargado telefonoEncargado) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encargado idEncargado = telefonoEncargado.getIdEncargado();
            if (idEncargado != null) {
                idEncargado = em.getReference(idEncargado.getClass(), idEncargado.getIdEncargado());
                telefonoEncargado.setIdEncargado(idEncargado);
            }
            em.persist(telefonoEncargado);
            if (idEncargado != null) {
                idEncargado.getTelefonoEncargadoList().add(telefonoEncargado);
                idEncargado = em.merge(idEncargado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTelefonoEncargado(telefonoEncargado.getIdTelefono()) != null) {
                throw new PreexistingEntityException("TelefonoEncargado " + telefonoEncargado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TelefonoEncargado telefonoEncargado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TelefonoEncargado persistentTelefonoEncargado = em.find(TelefonoEncargado.class, telefonoEncargado.getIdTelefono());
            Encargado idEncargadoOld = persistentTelefonoEncargado.getIdEncargado();
            Encargado idEncargadoNew = telefonoEncargado.getIdEncargado();
            if (idEncargadoNew != null) {
                idEncargadoNew = em.getReference(idEncargadoNew.getClass(), idEncargadoNew.getIdEncargado());
                telefonoEncargado.setIdEncargado(idEncargadoNew);
            }
            telefonoEncargado = em.merge(telefonoEncargado);
            if (idEncargadoOld != null && !idEncargadoOld.equals(idEncargadoNew)) {
                idEncargadoOld.getTelefonoEncargadoList().remove(telefonoEncargado);
                idEncargadoOld = em.merge(idEncargadoOld);
            }
            if (idEncargadoNew != null && !idEncargadoNew.equals(idEncargadoOld)) {
                idEncargadoNew.getTelefonoEncargadoList().add(telefonoEncargado);
                idEncargadoNew = em.merge(idEncargadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = telefonoEncargado.getIdTelefono();
                if (findTelefonoEncargado(id) == null) {
                    throw new NonexistentEntityException("The telefonoEncargado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TelefonoEncargado telefonoEncargado;
            try {
                telefonoEncargado = em.getReference(TelefonoEncargado.class, id);
                telefonoEncargado.getIdTelefono();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telefonoEncargado with id " + id + " no longer exists.", enfe);
            }
            Encargado idEncargado = telefonoEncargado.getIdEncargado();
            if (idEncargado != null) {
                idEncargado.getTelefonoEncargadoList().remove(telefonoEncargado);
                idEncargado = em.merge(idEncargado);
            }
            em.remove(telefonoEncargado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TelefonoEncargado> findTelefonoEncargadoEntities() {
        return findTelefonoEncargadoEntities(true, -1, -1);
    }

    public List<TelefonoEncargado> findTelefonoEncargadoEntities(int maxResults, int firstResult) {
        return findTelefonoEncargadoEntities(false, maxResults, firstResult);
    }

    private List<TelefonoEncargado> findTelefonoEncargadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TelefonoEncargado.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TelefonoEncargado findTelefonoEncargado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TelefonoEncargado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelefonoEncargadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TelefonoEncargado> rt = cq.from(TelefonoEncargado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
