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
import Logica_negocio.TipoUsuario;
import Logica_negocio.UsuarioGrado;
import java.util.ArrayList;
import java.util.List;
import Logica_negocio.TelefonoUsuario;
import Logica_negocio.Usuario;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JoseM
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getUsuarioGradoList() == null) {
            usuario.setUsuarioGradoList(new ArrayList<UsuarioGrado>());
        }
        if (usuario.getTelefonoUsuarioList() == null) {
            usuario.setTelefonoUsuarioList(new ArrayList<TelefonoUsuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoUsuario idTipo = usuario.getIdTipo();
            if (idTipo != null) {
                idTipo = em.getReference(idTipo.getClass(), idTipo.getIdTipo());
                usuario.setIdTipo(idTipo);
            }
            List<UsuarioGrado> attachedUsuarioGradoList = new ArrayList<UsuarioGrado>();
            for (UsuarioGrado usuarioGradoListUsuarioGradoToAttach : usuario.getUsuarioGradoList()) {
                usuarioGradoListUsuarioGradoToAttach = em.getReference(usuarioGradoListUsuarioGradoToAttach.getClass(), usuarioGradoListUsuarioGradoToAttach.getIdUsuarioGrado());
                attachedUsuarioGradoList.add(usuarioGradoListUsuarioGradoToAttach);
            }
            usuario.setUsuarioGradoList(attachedUsuarioGradoList);
            List<TelefonoUsuario> attachedTelefonoUsuarioList = new ArrayList<TelefonoUsuario>();
            for (TelefonoUsuario telefonoUsuarioListTelefonoUsuarioToAttach : usuario.getTelefonoUsuarioList()) {
                telefonoUsuarioListTelefonoUsuarioToAttach = em.getReference(telefonoUsuarioListTelefonoUsuarioToAttach.getClass(), telefonoUsuarioListTelefonoUsuarioToAttach.getIdTelefono());
                attachedTelefonoUsuarioList.add(telefonoUsuarioListTelefonoUsuarioToAttach);
            }
            usuario.setTelefonoUsuarioList(attachedTelefonoUsuarioList);
            em.persist(usuario);
            if (idTipo != null) {
                idTipo.getUsuarioList().add(usuario);
                idTipo = em.merge(idTipo);
            }
            for (UsuarioGrado usuarioGradoListUsuarioGrado : usuario.getUsuarioGradoList()) {
                Usuario oldIdUsuarioOfUsuarioGradoListUsuarioGrado = usuarioGradoListUsuarioGrado.getIdUsuario();
                usuarioGradoListUsuarioGrado.setIdUsuario(usuario);
                usuarioGradoListUsuarioGrado = em.merge(usuarioGradoListUsuarioGrado);
                if (oldIdUsuarioOfUsuarioGradoListUsuarioGrado != null) {
                    oldIdUsuarioOfUsuarioGradoListUsuarioGrado.getUsuarioGradoList().remove(usuarioGradoListUsuarioGrado);
                    oldIdUsuarioOfUsuarioGradoListUsuarioGrado = em.merge(oldIdUsuarioOfUsuarioGradoListUsuarioGrado);
                }
            }
            for (TelefonoUsuario telefonoUsuarioListTelefonoUsuario : usuario.getTelefonoUsuarioList()) {
                Usuario oldIdUsuarioOfTelefonoUsuarioListTelefonoUsuario = telefonoUsuarioListTelefonoUsuario.getIdUsuario();
                telefonoUsuarioListTelefonoUsuario.setIdUsuario(usuario);
                telefonoUsuarioListTelefonoUsuario = em.merge(telefonoUsuarioListTelefonoUsuario);
                if (oldIdUsuarioOfTelefonoUsuarioListTelefonoUsuario != null) {
                    oldIdUsuarioOfTelefonoUsuarioListTelefonoUsuario.getTelefonoUsuarioList().remove(telefonoUsuarioListTelefonoUsuario);
                    oldIdUsuarioOfTelefonoUsuarioListTelefonoUsuario = em.merge(oldIdUsuarioOfTelefonoUsuarioListTelefonoUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            TipoUsuario idTipoOld = persistentUsuario.getIdTipo();
            TipoUsuario idTipoNew = usuario.getIdTipo();
            List<UsuarioGrado> usuarioGradoListOld = persistentUsuario.getUsuarioGradoList();
            List<UsuarioGrado> usuarioGradoListNew = usuario.getUsuarioGradoList();
            List<TelefonoUsuario> telefonoUsuarioListOld = persistentUsuario.getTelefonoUsuarioList();
            List<TelefonoUsuario> telefonoUsuarioListNew = usuario.getTelefonoUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioGrado usuarioGradoListOldUsuarioGrado : usuarioGradoListOld) {
                if (!usuarioGradoListNew.contains(usuarioGradoListOldUsuarioGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioGrado " + usuarioGradoListOldUsuarioGrado + " since its idUsuario field is not nullable.");
                }
            }
            for (TelefonoUsuario telefonoUsuarioListOldTelefonoUsuario : telefonoUsuarioListOld) {
                if (!telefonoUsuarioListNew.contains(telefonoUsuarioListOldTelefonoUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TelefonoUsuario " + telefonoUsuarioListOldTelefonoUsuario + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoNew != null) {
                idTipoNew = em.getReference(idTipoNew.getClass(), idTipoNew.getIdTipo());
                usuario.setIdTipo(idTipoNew);
            }
            List<UsuarioGrado> attachedUsuarioGradoListNew = new ArrayList<UsuarioGrado>();
            for (UsuarioGrado usuarioGradoListNewUsuarioGradoToAttach : usuarioGradoListNew) {
                usuarioGradoListNewUsuarioGradoToAttach = em.getReference(usuarioGradoListNewUsuarioGradoToAttach.getClass(), usuarioGradoListNewUsuarioGradoToAttach.getIdUsuarioGrado());
                attachedUsuarioGradoListNew.add(usuarioGradoListNewUsuarioGradoToAttach);
            }
            usuarioGradoListNew = attachedUsuarioGradoListNew;
            usuario.setUsuarioGradoList(usuarioGradoListNew);
            List<TelefonoUsuario> attachedTelefonoUsuarioListNew = new ArrayList<TelefonoUsuario>();
            for (TelefonoUsuario telefonoUsuarioListNewTelefonoUsuarioToAttach : telefonoUsuarioListNew) {
                telefonoUsuarioListNewTelefonoUsuarioToAttach = em.getReference(telefonoUsuarioListNewTelefonoUsuarioToAttach.getClass(), telefonoUsuarioListNewTelefonoUsuarioToAttach.getIdTelefono());
                attachedTelefonoUsuarioListNew.add(telefonoUsuarioListNewTelefonoUsuarioToAttach);
            }
            telefonoUsuarioListNew = attachedTelefonoUsuarioListNew;
            usuario.setTelefonoUsuarioList(telefonoUsuarioListNew);
            usuario = em.merge(usuario);
            if (idTipoOld != null && !idTipoOld.equals(idTipoNew)) {
                idTipoOld.getUsuarioList().remove(usuario);
                idTipoOld = em.merge(idTipoOld);
            }
            if (idTipoNew != null && !idTipoNew.equals(idTipoOld)) {
                idTipoNew.getUsuarioList().add(usuario);
                idTipoNew = em.merge(idTipoNew);
            }
            for (UsuarioGrado usuarioGradoListNewUsuarioGrado : usuarioGradoListNew) {
                if (!usuarioGradoListOld.contains(usuarioGradoListNewUsuarioGrado)) {
                    Usuario oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado = usuarioGradoListNewUsuarioGrado.getIdUsuario();
                    usuarioGradoListNewUsuarioGrado.setIdUsuario(usuario);
                    usuarioGradoListNewUsuarioGrado = em.merge(usuarioGradoListNewUsuarioGrado);
                    if (oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado != null && !oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado.equals(usuario)) {
                        oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado.getUsuarioGradoList().remove(usuarioGradoListNewUsuarioGrado);
                        oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado = em.merge(oldIdUsuarioOfUsuarioGradoListNewUsuarioGrado);
                    }
                }
            }
            for (TelefonoUsuario telefonoUsuarioListNewTelefonoUsuario : telefonoUsuarioListNew) {
                if (!telefonoUsuarioListOld.contains(telefonoUsuarioListNewTelefonoUsuario)) {
                    Usuario oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario = telefonoUsuarioListNewTelefonoUsuario.getIdUsuario();
                    telefonoUsuarioListNewTelefonoUsuario.setIdUsuario(usuario);
                    telefonoUsuarioListNewTelefonoUsuario = em.merge(telefonoUsuarioListNewTelefonoUsuario);
                    if (oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario != null && !oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario.equals(usuario)) {
                        oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario.getTelefonoUsuarioList().remove(telefonoUsuarioListNewTelefonoUsuario);
                        oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario = em.merge(oldIdUsuarioOfTelefonoUsuarioListNewTelefonoUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UsuarioGrado> usuarioGradoListOrphanCheck = usuario.getUsuarioGradoList();
            for (UsuarioGrado usuarioGradoListOrphanCheckUsuarioGrado : usuarioGradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioGrado " + usuarioGradoListOrphanCheckUsuarioGrado + " in its usuarioGradoList field has a non-nullable idUsuario field.");
            }
            List<TelefonoUsuario> telefonoUsuarioListOrphanCheck = usuario.getTelefonoUsuarioList();
            for (TelefonoUsuario telefonoUsuarioListOrphanCheckTelefonoUsuario : telefonoUsuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the TelefonoUsuario " + telefonoUsuarioListOrphanCheckTelefonoUsuario + " in its telefonoUsuarioList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoUsuario idTipo = usuario.getIdTipo();
            if (idTipo != null) {
                idTipo.getUsuarioList().remove(usuario);
                idTipo = em.merge(idTipo);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
