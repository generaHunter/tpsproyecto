/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Datos.exceptions.NonexistentEntityException;
import Datos.exceptions.PreexistingEntityException;
import Logica_negocio.TelefonoUsuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica_negocio.Usuario;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class TelefonoUsuarioJpaController implements Serializable {

    public TelefonoUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TelefonoUsuario telefonoUsuario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idUsuario = telefonoUsuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                telefonoUsuario.setIdUsuario(idUsuario);
            }
            em.persist(telefonoUsuario);
            if (idUsuario != null) {
                idUsuario.getTelefonoUsuarioList().add(telefonoUsuario);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTelefonoUsuario(telefonoUsuario.getIdTelefono()) != null) {
                throw new PreexistingEntityException("TelefonoUsuario " + telefonoUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TelefonoUsuario telefonoUsuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TelefonoUsuario persistentTelefonoUsuario = em.find(TelefonoUsuario.class, telefonoUsuario.getIdTelefono());
            Usuario idUsuarioOld = persistentTelefonoUsuario.getIdUsuario();
            Usuario idUsuarioNew = telefonoUsuario.getIdUsuario();
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                telefonoUsuario.setIdUsuario(idUsuarioNew);
            }
            telefonoUsuario = em.merge(telefonoUsuario);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getTelefonoUsuarioList().remove(telefonoUsuario);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getTelefonoUsuarioList().add(telefonoUsuario);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = telefonoUsuario.getIdTelefono();
                if (findTelefonoUsuario(id) == null) {
                    throw new NonexistentEntityException("The telefonoUsuario with id " + id + " no longer exists.");
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
            TelefonoUsuario telefonoUsuario;
            try {
                telefonoUsuario = em.getReference(TelefonoUsuario.class, id);
                telefonoUsuario.getIdTelefono();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telefonoUsuario with id " + id + " no longer exists.", enfe);
            }
            Usuario idUsuario = telefonoUsuario.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getTelefonoUsuarioList().remove(telefonoUsuario);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(telefonoUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TelefonoUsuario> findTelefonoUsuarioEntities() {
        return findTelefonoUsuarioEntities(true, -1, -1);
    }

    public List<TelefonoUsuario> findTelefonoUsuarioEntities(int maxResults, int firstResult) {
        return findTelefonoUsuarioEntities(false, maxResults, firstResult);
    }

    private List<TelefonoUsuario> findTelefonoUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TelefonoUsuario.class));
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

    public TelefonoUsuario findTelefonoUsuario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TelefonoUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelefonoUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TelefonoUsuario> rt = cq.from(TelefonoUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
