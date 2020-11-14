/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dto.Suela;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Tipo;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class TipoJpaController implements Serializable {

    public TipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipo tipo) throws PreexistingEntityException, Exception {
        if (tipo.getSuelaList() == null) {
            tipo.setSuelaList(new ArrayList<Suela>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Suela> attachedSuelaList = new ArrayList<Suela>();
            for (Suela suelaListSuelaToAttach : tipo.getSuelaList()) {
                suelaListSuelaToAttach = em.getReference(suelaListSuelaToAttach.getClass(), suelaListSuelaToAttach.getProductoId());
                attachedSuelaList.add(suelaListSuelaToAttach);
            }
            tipo.setSuelaList(attachedSuelaList);
            em.persist(tipo);
            for (Suela suelaListSuela : tipo.getSuelaList()) {
                Tipo oldTipoIdOfSuelaListSuela = suelaListSuela.getTipoId();
                suelaListSuela.setTipoId(tipo);
                suelaListSuela = em.merge(suelaListSuela);
                if (oldTipoIdOfSuelaListSuela != null) {
                    oldTipoIdOfSuelaListSuela.getSuelaList().remove(suelaListSuela);
                    oldTipoIdOfSuelaListSuela = em.merge(oldTipoIdOfSuelaListSuela);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipo(tipo.getId()) != null) {
                throw new PreexistingEntityException("Tipo " + tipo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipo tipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo persistentTipo = em.find(Tipo.class, tipo.getId());
            List<Suela> suelaListOld = persistentTipo.getSuelaList();
            List<Suela> suelaListNew = tipo.getSuelaList();
            List<String> illegalOrphanMessages = null;
            for (Suela suelaListOldSuela : suelaListOld) {
                if (!suelaListNew.contains(suelaListOldSuela)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Suela " + suelaListOldSuela + " since its tipoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Suela> attachedSuelaListNew = new ArrayList<Suela>();
            for (Suela suelaListNewSuelaToAttach : suelaListNew) {
                suelaListNewSuelaToAttach = em.getReference(suelaListNewSuelaToAttach.getClass(), suelaListNewSuelaToAttach.getProductoId());
                attachedSuelaListNew.add(suelaListNewSuelaToAttach);
            }
            suelaListNew = attachedSuelaListNew;
            tipo.setSuelaList(suelaListNew);
            tipo = em.merge(tipo);
            for (Suela suelaListNewSuela : suelaListNew) {
                if (!suelaListOld.contains(suelaListNewSuela)) {
                    Tipo oldTipoIdOfSuelaListNewSuela = suelaListNewSuela.getTipoId();
                    suelaListNewSuela.setTipoId(tipo);
                    suelaListNewSuela = em.merge(suelaListNewSuela);
                    if (oldTipoIdOfSuelaListNewSuela != null && !oldTipoIdOfSuelaListNewSuela.equals(tipo)) {
                        oldTipoIdOfSuelaListNewSuela.getSuelaList().remove(suelaListNewSuela);
                        oldTipoIdOfSuelaListNewSuela = em.merge(oldTipoIdOfSuelaListNewSuela);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipo.getId();
                if (findTipo(id) == null) {
                    throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipo tipo;
            try {
                tipo = em.getReference(Tipo.class, id);
                tipo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Suela> suelaListOrphanCheck = tipo.getSuelaList();
            for (Suela suelaListOrphanCheckSuela : suelaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipo (" + tipo + ") cannot be destroyed since the Suela " + suelaListOrphanCheckSuela + " in its suelaList field has a non-nullable tipoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipo> findTipoEntities() {
        return findTipoEntities(true, -1, -1);
    }

    public List<Tipo> findTipoEntities(int maxResults, int firstResult) {
        return findTipoEntities(false, maxResults, firstResult);
    }

    private List<Tipo> findTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipo.class));
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

    public Tipo findTipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipo> rt = cq.from(Tipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
