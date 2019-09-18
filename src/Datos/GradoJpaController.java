/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Datos.exceptions.IllegalOrphanException;
import Datos.exceptions.NonexistentEntityException;
import Datos.exceptions.PreexistingEntityException;
import Logica_negocio.Grado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica_negocio.UsuarioGrado;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class GradoJpaController implements Serializable {

    public GradoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) throws PreexistingEntityException, Exception {
        if (grado.getUsuarioGradoList() == null) {
            grado.setUsuarioGradoList(new ArrayList<UsuarioGrado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UsuarioGrado> attachedUsuarioGradoList = new ArrayList<UsuarioGrado>();
            for (UsuarioGrado usuarioGradoListUsuarioGradoToAttach : grado.getUsuarioGradoList()) {
                usuarioGradoListUsuarioGradoToAttach = em.getReference(usuarioGradoListUsuarioGradoToAttach.getClass(), usuarioGradoListUsuarioGradoToAttach.getIdUsuarioGrado());
                attachedUsuarioGradoList.add(usuarioGradoListUsuarioGradoToAttach);
            }
            grado.setUsuarioGradoList(attachedUsuarioGradoList);
            em.persist(grado);
            for (UsuarioGrado usuarioGradoListUsuarioGrado : grado.getUsuarioGradoList()) {
                Grado oldIdGradoOfUsuarioGradoListUsuarioGrado = usuarioGradoListUsuarioGrado.getIdGrado();
                usuarioGradoListUsuarioGrado.setIdGrado(grado);
                usuarioGradoListUsuarioGrado = em.merge(usuarioGradoListUsuarioGrado);
                if (oldIdGradoOfUsuarioGradoListUsuarioGrado != null) {
                    oldIdGradoOfUsuarioGradoListUsuarioGrado.getUsuarioGradoList().remove(usuarioGradoListUsuarioGrado);
                    oldIdGradoOfUsuarioGradoListUsuarioGrado = em.merge(oldIdGradoOfUsuarioGradoListUsuarioGrado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrado(grado.getIdGrado()) != null) {
                throw new PreexistingEntityException("Grado " + grado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getIdGrado());
            List<UsuarioGrado> usuarioGradoListOld = persistentGrado.getUsuarioGradoList();
            List<UsuarioGrado> usuarioGradoListNew = grado.getUsuarioGradoList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioGrado usuarioGradoListOldUsuarioGrado : usuarioGradoListOld) {
                if (!usuarioGradoListNew.contains(usuarioGradoListOldUsuarioGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioGrado " + usuarioGradoListOldUsuarioGrado + " since its idGrado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UsuarioGrado> attachedUsuarioGradoListNew = new ArrayList<UsuarioGrado>();
            for (UsuarioGrado usuarioGradoListNewUsuarioGradoToAttach : usuarioGradoListNew) {
                usuarioGradoListNewUsuarioGradoToAttach = em.getReference(usuarioGradoListNewUsuarioGradoToAttach.getClass(), usuarioGradoListNewUsuarioGradoToAttach.getIdUsuarioGrado());
                attachedUsuarioGradoListNew.add(usuarioGradoListNewUsuarioGradoToAttach);
            }
            usuarioGradoListNew = attachedUsuarioGradoListNew;
            grado.setUsuarioGradoList(usuarioGradoListNew);
            grado = em.merge(grado);
            for (UsuarioGrado usuarioGradoListNewUsuarioGrado : usuarioGradoListNew) {
                if (!usuarioGradoListOld.contains(usuarioGradoListNewUsuarioGrado)) {
                    Grado oldIdGradoOfUsuarioGradoListNewUsuarioGrado = usuarioGradoListNewUsuarioGrado.getIdGrado();
                    usuarioGradoListNewUsuarioGrado.setIdGrado(grado);
                    usuarioGradoListNewUsuarioGrado = em.merge(usuarioGradoListNewUsuarioGrado);
                    if (oldIdGradoOfUsuarioGradoListNewUsuarioGrado != null && !oldIdGradoOfUsuarioGradoListNewUsuarioGrado.equals(grado)) {
                        oldIdGradoOfUsuarioGradoListNewUsuarioGrado.getUsuarioGradoList().remove(usuarioGradoListNewUsuarioGrado);
                        oldIdGradoOfUsuarioGradoListNewUsuarioGrado = em.merge(oldIdGradoOfUsuarioGradoListNewUsuarioGrado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = grado.getIdGrado();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getIdGrado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UsuarioGrado> usuarioGradoListOrphanCheck = grado.getUsuarioGradoList();
            for (UsuarioGrado usuarioGradoListOrphanCheckUsuarioGrado : usuarioGradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the UsuarioGrado " + usuarioGradoListOrphanCheckUsuarioGrado + " in its usuarioGradoList field has a non-nullable idGrado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
