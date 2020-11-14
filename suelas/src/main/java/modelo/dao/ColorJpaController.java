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
import modelo.dto.Tira;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Color;
import modelo.dto.Suela;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class ColorJpaController implements Serializable {

    public ColorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Color color) throws PreexistingEntityException, Exception {
        if (color.getTiraList() == null) {
            color.setTiraList(new ArrayList<Tira>());
        }
        if (color.getSuelaList() == null) {
            color.setSuelaList(new ArrayList<Suela>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tira> attachedTiraList = new ArrayList<Tira>();
            for (Tira tiraListTiraToAttach : color.getTiraList()) {
                tiraListTiraToAttach = em.getReference(tiraListTiraToAttach.getClass(), tiraListTiraToAttach.getProductoId());
                attachedTiraList.add(tiraListTiraToAttach);
            }
            color.setTiraList(attachedTiraList);
            List<Suela> attachedSuelaList = new ArrayList<Suela>();
            for (Suela suelaListSuelaToAttach : color.getSuelaList()) {
                suelaListSuelaToAttach = em.getReference(suelaListSuelaToAttach.getClass(), suelaListSuelaToAttach.getProductoId());
                attachedSuelaList.add(suelaListSuelaToAttach);
            }
            color.setSuelaList(attachedSuelaList);
            em.persist(color);
            for (Tira tiraListTira : color.getTiraList()) {
                Color oldColorIdOfTiraListTira = tiraListTira.getColorId();
                tiraListTira.setColorId(color);
                tiraListTira = em.merge(tiraListTira);
                if (oldColorIdOfTiraListTira != null) {
                    oldColorIdOfTiraListTira.getTiraList().remove(tiraListTira);
                    oldColorIdOfTiraListTira = em.merge(oldColorIdOfTiraListTira);
                }
            }
            for (Suela suelaListSuela : color.getSuelaList()) {
                Color oldColorIdOfSuelaListSuela = suelaListSuela.getColorId();
                suelaListSuela.setColorId(color);
                suelaListSuela = em.merge(suelaListSuela);
                if (oldColorIdOfSuelaListSuela != null) {
                    oldColorIdOfSuelaListSuela.getSuelaList().remove(suelaListSuela);
                    oldColorIdOfSuelaListSuela = em.merge(oldColorIdOfSuelaListSuela);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findColor(color.getId()) != null) {
                throw new PreexistingEntityException("Color " + color + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Color color) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Color persistentColor = em.find(Color.class, color.getId());
            List<Tira> tiraListOld = persistentColor.getTiraList();
            List<Tira> tiraListNew = color.getTiraList();
            List<Suela> suelaListOld = persistentColor.getSuelaList();
            List<Suela> suelaListNew = color.getSuelaList();
            List<String> illegalOrphanMessages = null;
            for (Tira tiraListOldTira : tiraListOld) {
                if (!tiraListNew.contains(tiraListOldTira)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tira " + tiraListOldTira + " since its colorId field is not nullable.");
                }
            }
            for (Suela suelaListOldSuela : suelaListOld) {
                if (!suelaListNew.contains(suelaListOldSuela)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Suela " + suelaListOldSuela + " since its colorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tira> attachedTiraListNew = new ArrayList<Tira>();
            for (Tira tiraListNewTiraToAttach : tiraListNew) {
                tiraListNewTiraToAttach = em.getReference(tiraListNewTiraToAttach.getClass(), tiraListNewTiraToAttach.getProductoId());
                attachedTiraListNew.add(tiraListNewTiraToAttach);
            }
            tiraListNew = attachedTiraListNew;
            color.setTiraList(tiraListNew);
            List<Suela> attachedSuelaListNew = new ArrayList<Suela>();
            for (Suela suelaListNewSuelaToAttach : suelaListNew) {
                suelaListNewSuelaToAttach = em.getReference(suelaListNewSuelaToAttach.getClass(), suelaListNewSuelaToAttach.getProductoId());
                attachedSuelaListNew.add(suelaListNewSuelaToAttach);
            }
            suelaListNew = attachedSuelaListNew;
            color.setSuelaList(suelaListNew);
            color = em.merge(color);
            for (Tira tiraListNewTira : tiraListNew) {
                if (!tiraListOld.contains(tiraListNewTira)) {
                    Color oldColorIdOfTiraListNewTira = tiraListNewTira.getColorId();
                    tiraListNewTira.setColorId(color);
                    tiraListNewTira = em.merge(tiraListNewTira);
                    if (oldColorIdOfTiraListNewTira != null && !oldColorIdOfTiraListNewTira.equals(color)) {
                        oldColorIdOfTiraListNewTira.getTiraList().remove(tiraListNewTira);
                        oldColorIdOfTiraListNewTira = em.merge(oldColorIdOfTiraListNewTira);
                    }
                }
            }
            for (Suela suelaListNewSuela : suelaListNew) {
                if (!suelaListOld.contains(suelaListNewSuela)) {
                    Color oldColorIdOfSuelaListNewSuela = suelaListNewSuela.getColorId();
                    suelaListNewSuela.setColorId(color);
                    suelaListNewSuela = em.merge(suelaListNewSuela);
                    if (oldColorIdOfSuelaListNewSuela != null && !oldColorIdOfSuelaListNewSuela.equals(color)) {
                        oldColorIdOfSuelaListNewSuela.getSuelaList().remove(suelaListNewSuela);
                        oldColorIdOfSuelaListNewSuela = em.merge(oldColorIdOfSuelaListNewSuela);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = color.getId();
                if (findColor(id) == null) {
                    throw new NonexistentEntityException("The color with id " + id + " no longer exists.");
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
            Color color;
            try {
                color = em.getReference(Color.class, id);
                color.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The color with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tira> tiraListOrphanCheck = color.getTiraList();
            for (Tira tiraListOrphanCheckTira : tiraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Color (" + color + ") cannot be destroyed since the Tira " + tiraListOrphanCheckTira + " in its tiraList field has a non-nullable colorId field.");
            }
            List<Suela> suelaListOrphanCheck = color.getSuelaList();
            for (Suela suelaListOrphanCheckSuela : suelaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Color (" + color + ") cannot be destroyed since the Suela " + suelaListOrphanCheckSuela + " in its suelaList field has a non-nullable colorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(color);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Color> findColorEntities() {
        return findColorEntities(true, -1, -1);
    }

    public List<Color> findColorEntities(int maxResults, int firstResult) {
        return findColorEntities(false, maxResults, firstResult);
    }

    private List<Color> findColorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Color.class));
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

    public Color findColor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Color.class, id);
        } finally {
            em.close();
        }
    }

    public int getColorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Color> rt = cq.from(Color.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
