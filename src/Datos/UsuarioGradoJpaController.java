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
import Logica_negocio.Grado;
import Logica_negocio.Usuario;
import Logica_negocio.UsuarioGrado;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class UsuarioGradoJpaController implements Serializable {

    public UsuarioGradoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public UsuarioGradoJpaController() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioGrado usuarioGrado) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado idGrado = usuarioGrado.getIdGrado();
            if (idGrado != null) {
                idGrado = em.getReference(idGrado.getClass(), idGrado.getIdGrado());
                usuarioGrado.setIdGrado(idGrado);
            }
            Usuario idUsuario = usuarioGrado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                usuarioGrado.setIdUsuario(idUsuario);
            }
            em.persist(usuarioGrado);
            if (idGrado != null) {
                idGrado.getUsuarioGradoList().add(usuarioGrado);
                idGrado = em.merge(idGrado);
            }
            if (idUsuario != null) {
                idUsuario.getUsuarioGradoList().add(usuarioGrado);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioGrado(usuarioGrado.getIdUsuarioGrado()) != null) {
                throw new PreexistingEntityException("UsuarioGrado " + usuarioGrado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioGrado usuarioGrado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioGrado persistentUsuarioGrado = em.find(UsuarioGrado.class, usuarioGrado.getIdUsuarioGrado());
            Grado idGradoOld = persistentUsuarioGrado.getIdGrado();
            Grado idGradoNew = usuarioGrado.getIdGrado();
            Usuario idUsuarioOld = persistentUsuarioGrado.getIdUsuario();
            Usuario idUsuarioNew = usuarioGrado.getIdUsuario();
            if (idGradoNew != null) {
                idGradoNew = em.getReference(idGradoNew.getClass(), idGradoNew.getIdGrado());
                usuarioGrado.setIdGrado(idGradoNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                usuarioGrado.setIdUsuario(idUsuarioNew);
            }
            usuarioGrado = em.merge(usuarioGrado);
            if (idGradoOld != null && !idGradoOld.equals(idGradoNew)) {
                idGradoOld.getUsuarioGradoList().remove(usuarioGrado);
                idGradoOld = em.merge(idGradoOld);
            }
            if (idGradoNew != null && !idGradoNew.equals(idGradoOld)) {
                idGradoNew.getUsuarioGradoList().add(usuarioGrado);
                idGradoNew = em.merge(idGradoNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getUsuarioGradoList().remove(usuarioGrado);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getUsuarioGradoList().add(usuarioGrado);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuarioGrado.getIdUsuarioGrado();
                if (findUsuarioGrado(id) == null) {
                    throw new NonexistentEntityException("The usuarioGrado with id " + id + " no longer exists.");
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
            UsuarioGrado usuarioGrado;
            try {
                usuarioGrado = em.getReference(UsuarioGrado.class, id);
                usuarioGrado.getIdUsuarioGrado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioGrado with id " + id + " no longer exists.", enfe);
            }
            Grado idGrado = usuarioGrado.getIdGrado();
            if (idGrado != null) {
                idGrado.getUsuarioGradoList().remove(usuarioGrado);
                idGrado = em.merge(idGrado);
            }
            Usuario idUsuario = usuarioGrado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getUsuarioGradoList().remove(usuarioGrado);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(usuarioGrado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioGrado> findUsuarioGradoEntities() {
        return findUsuarioGradoEntities(true, -1, -1);
    }

    public List<UsuarioGrado> findUsuarioGradoEntities(int maxResults, int firstResult) {
        return findUsuarioGradoEntities(false, maxResults, firstResult);
    }

    private List<UsuarioGrado> findUsuarioGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioGrado.class));
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

    public UsuarioGrado findUsuarioGrado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioGrado.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioGrado> rt = cq.from(UsuarioGrado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
