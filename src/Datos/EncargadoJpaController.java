/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import Datos.exceptions.IllegalOrphanException;
import Datos.exceptions.NonexistentEntityException;
import Datos.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica_negocio.Alumno;
import Logica_negocio.Encargado;
import java.util.ArrayList;
import java.util.List;
import Logica_negocio.TelefonoEncargado;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class EncargadoJpaController implements Serializable {

    public EncargadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encargado encargado) throws PreexistingEntityException, Exception {
        if (encargado.getAlumnoList() == null) {
            encargado.setAlumnoList(new ArrayList<Alumno>());
        }
        if (encargado.getTelefonoEncargadoList() == null) {
            encargado.setTelefonoEncargadoList(new ArrayList<TelefonoEncargado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Alumno> attachedAlumnoList = new ArrayList<Alumno>();
            for (Alumno alumnoListAlumnoToAttach : encargado.getAlumnoList()) {
                alumnoListAlumnoToAttach = em.getReference(alumnoListAlumnoToAttach.getClass(), alumnoListAlumnoToAttach.getIdAlumno());
                attachedAlumnoList.add(alumnoListAlumnoToAttach);
            }
            encargado.setAlumnoList(attachedAlumnoList);
            List<TelefonoEncargado> attachedTelefonoEncargadoList = new ArrayList<TelefonoEncargado>();
            for (TelefonoEncargado telefonoEncargadoListTelefonoEncargadoToAttach : encargado.getTelefonoEncargadoList()) {
                telefonoEncargadoListTelefonoEncargadoToAttach = em.getReference(telefonoEncargadoListTelefonoEncargadoToAttach.getClass(), telefonoEncargadoListTelefonoEncargadoToAttach.getIdTelefono());
                attachedTelefonoEncargadoList.add(telefonoEncargadoListTelefonoEncargadoToAttach);
            }
            encargado.setTelefonoEncargadoList(attachedTelefonoEncargadoList);
            em.persist(encargado);
            for (Alumno alumnoListAlumno : encargado.getAlumnoList()) {
                Encargado oldIdEncargadoOfAlumnoListAlumno = alumnoListAlumno.getIdEncargado();
                alumnoListAlumno.setIdEncargado(encargado);
                alumnoListAlumno = em.merge(alumnoListAlumno);
                if (oldIdEncargadoOfAlumnoListAlumno != null) {
                    oldIdEncargadoOfAlumnoListAlumno.getAlumnoList().remove(alumnoListAlumno);
                    oldIdEncargadoOfAlumnoListAlumno = em.merge(oldIdEncargadoOfAlumnoListAlumno);
                }
            }
            for (TelefonoEncargado telefonoEncargadoListTelefonoEncargado : encargado.getTelefonoEncargadoList()) {
                Encargado oldIdEncargadoOfTelefonoEncargadoListTelefonoEncargado = telefonoEncargadoListTelefonoEncargado.getIdEncargado();
                telefonoEncargadoListTelefonoEncargado.setIdEncargado(encargado);
                telefonoEncargadoListTelefonoEncargado = em.merge(telefonoEncargadoListTelefonoEncargado);
                if (oldIdEncargadoOfTelefonoEncargadoListTelefonoEncargado != null) {
                    oldIdEncargadoOfTelefonoEncargadoListTelefonoEncargado.getTelefonoEncargadoList().remove(telefonoEncargadoListTelefonoEncargado);
                    oldIdEncargadoOfTelefonoEncargadoListTelefonoEncargado = em.merge(oldIdEncargadoOfTelefonoEncargadoListTelefonoEncargado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEncargado(encargado.getIdEncargado()) != null) {
                throw new PreexistingEntityException("Encargado " + encargado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encargado encargado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encargado persistentEncargado = em.find(Encargado.class, encargado.getIdEncargado());
            List<Alumno> alumnoListOld = persistentEncargado.getAlumnoList();
            List<Alumno> alumnoListNew = encargado.getAlumnoList();
            List<TelefonoEncargado> telefonoEncargadoListOld = persistentEncargado.getTelefonoEncargadoList();
            List<TelefonoEncargado> telefonoEncargadoListNew = encargado.getTelefonoEncargadoList();
            List<String> illegalOrphanMessages = null;
            for (Alumno alumnoListOldAlumno : alumnoListOld) {
                if (!alumnoListNew.contains(alumnoListOldAlumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumno " + alumnoListOldAlumno + " since its idEncargado field is not nullable.");
                }
            }
            for (TelefonoEncargado telefonoEncargadoListOldTelefonoEncargado : telefonoEncargadoListOld) {
                if (!telefonoEncargadoListNew.contains(telefonoEncargadoListOldTelefonoEncargado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelefonoEncargado " + telefonoEncargadoListOldTelefonoEncargado + " since its idEncargado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Alumno> attachedAlumnoListNew = new ArrayList<Alumno>();
            for (Alumno alumnoListNewAlumnoToAttach : alumnoListNew) {
                alumnoListNewAlumnoToAttach = em.getReference(alumnoListNewAlumnoToAttach.getClass(), alumnoListNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoListNew.add(alumnoListNewAlumnoToAttach);
            }
            alumnoListNew = attachedAlumnoListNew;
            encargado.setAlumnoList(alumnoListNew);
            List<TelefonoEncargado> attachedTelefonoEncargadoListNew = new ArrayList<TelefonoEncargado>();
            for (TelefonoEncargado telefonoEncargadoListNewTelefonoEncargadoToAttach : telefonoEncargadoListNew) {
                telefonoEncargadoListNewTelefonoEncargadoToAttach = em.getReference(telefonoEncargadoListNewTelefonoEncargadoToAttach.getClass(), telefonoEncargadoListNewTelefonoEncargadoToAttach.getIdTelefono());
                attachedTelefonoEncargadoListNew.add(telefonoEncargadoListNewTelefonoEncargadoToAttach);
            }
            telefonoEncargadoListNew = attachedTelefonoEncargadoListNew;
            encargado.setTelefonoEncargadoList(telefonoEncargadoListNew);
            encargado = em.merge(encargado);
            for (Alumno alumnoListNewAlumno : alumnoListNew) {
                if (!alumnoListOld.contains(alumnoListNewAlumno)) {
                    Encargado oldIdEncargadoOfAlumnoListNewAlumno = alumnoListNewAlumno.getIdEncargado();
                    alumnoListNewAlumno.setIdEncargado(encargado);
                    alumnoListNewAlumno = em.merge(alumnoListNewAlumno);
                    if (oldIdEncargadoOfAlumnoListNewAlumno != null && !oldIdEncargadoOfAlumnoListNewAlumno.equals(encargado)) {
                        oldIdEncargadoOfAlumnoListNewAlumno.getAlumnoList().remove(alumnoListNewAlumno);
                        oldIdEncargadoOfAlumnoListNewAlumno = em.merge(oldIdEncargadoOfAlumnoListNewAlumno);
                    }
                }
            }
            for (TelefonoEncargado telefonoEncargadoListNewTelefonoEncargado : telefonoEncargadoListNew) {
                if (!telefonoEncargadoListOld.contains(telefonoEncargadoListNewTelefonoEncargado)) {
                    Encargado oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado = telefonoEncargadoListNewTelefonoEncargado.getIdEncargado();
                    telefonoEncargadoListNewTelefonoEncargado.setIdEncargado(encargado);
                    telefonoEncargadoListNewTelefonoEncargado = em.merge(telefonoEncargadoListNewTelefonoEncargado);
                    if (oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado != null && !oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado.equals(encargado)) {
                        oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado.getTelefonoEncargadoList().remove(telefonoEncargadoListNewTelefonoEncargado);
                        oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado = em.merge(oldIdEncargadoOfTelefonoEncargadoListNewTelefonoEncargado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = encargado.getIdEncargado();
                if (findEncargado(id) == null) {
                    throw new NonexistentEntityException("The encargado with id " + id + " no longer exists.");
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
            Encargado encargado;
            try {
                encargado = em.getReference(Encargado.class, id);
                encargado.getIdEncargado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encargado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Alumno> alumnoListOrphanCheck = encargado.getAlumnoList();
            for (Alumno alumnoListOrphanCheckAlumno : alumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encargado (" + encargado + ") cannot be destroyed since the Alumno " + alumnoListOrphanCheckAlumno + " in its alumnoList field has a non-nullable idEncargado field.");
            }
            List<TelefonoEncargado> telefonoEncargadoListOrphanCheck = encargado.getTelefonoEncargadoList();
            for (TelefonoEncargado telefonoEncargadoListOrphanCheckTelefonoEncargado : telefonoEncargadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encargado (" + encargado + ") cannot be destroyed since the TelefonoEncargado " + telefonoEncargadoListOrphanCheckTelefonoEncargado + " in its telefonoEncargadoList field has a non-nullable idEncargado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(encargado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encargado> findEncargadoEntities() {
        return findEncargadoEntities(true, -1, -1);
    }

    public List<Encargado> findEncargadoEntities(int maxResults, int firstResult) {
        return findEncargadoEntities(false, maxResults, firstResult);
    }

    private List<Encargado> findEncargadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encargado.class));
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

    public Encargado findEncargado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encargado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncargadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encargado> rt = cq.from(Encargado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
