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
import modelo.dto.Color;
import modelo.dto.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Tira;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class TiraJpaController implements Serializable {

    public TiraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tira tira) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Producto productoOrphanCheck = tira.getProducto();
        if (productoOrphanCheck != null) {
            Tira oldTiraOfProducto = productoOrphanCheck.getTira();
            if (oldTiraOfProducto != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Producto " + productoOrphanCheck + " already has an item of type Tira whose producto column cannot be null. Please make another selection for the producto field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Color colorId = tira.getColorId();
            if (colorId != null) {
                colorId = em.getReference(colorId.getClass(), colorId.getId());
                tira.setColorId(colorId);
            }
            Producto producto = tira.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                tira.setProducto(producto);
            }
            em.persist(tira);
            if (colorId != null) {
                colorId.getTiraList().add(tira);
                colorId = em.merge(colorId);
            }
            if (producto != null) {
                producto.setTira(tira);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTira(tira.getProductoId()) != null) {
                throw new PreexistingEntityException("Tira " + tira + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tira tira) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tira persistentTira = em.find(Tira.class, tira.getProductoId());
            Color colorIdOld = persistentTira.getColorId();
            Color colorIdNew = tira.getColorId();
            Producto productoOld = persistentTira.getProducto();
            Producto productoNew = tira.getProducto();
            List<String> illegalOrphanMessages = null;
            if (productoNew != null && !productoNew.equals(productoOld)) {
                Tira oldTiraOfProducto = productoNew.getTira();
                if (oldTiraOfProducto != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Producto " + productoNew + " already has an item of type Tira whose producto column cannot be null. Please make another selection for the producto field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (colorIdNew != null) {
                colorIdNew = em.getReference(colorIdNew.getClass(), colorIdNew.getId());
                tira.setColorId(colorIdNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                tira.setProducto(productoNew);
            }
            tira = em.merge(tira);
            if (colorIdOld != null && !colorIdOld.equals(colorIdNew)) {
                colorIdOld.getTiraList().remove(tira);
                colorIdOld = em.merge(colorIdOld);
            }
            if (colorIdNew != null && !colorIdNew.equals(colorIdOld)) {
                colorIdNew.getTiraList().add(tira);
                colorIdNew = em.merge(colorIdNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.setTira(null);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.setTira(tira);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tira.getProductoId();
                if (findTira(id) == null) {
                    throw new NonexistentEntityException("The tira with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tira tira;
            try {
                tira = em.getReference(Tira.class, id);
                tira.getProductoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tira with id " + id + " no longer exists.", enfe);
            }
            Color colorId = tira.getColorId();
            if (colorId != null) {
                colorId.getTiraList().remove(tira);
                colorId = em.merge(colorId);
            }
            Producto producto = tira.getProducto();
            if (producto != null) {
                producto.setTira(null);
                producto = em.merge(producto);
            }
            em.remove(tira);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tira> findTiraEntities() {
        return findTiraEntities(true, -1, -1);
    }

    public List<Tira> findTiraEntities(int maxResults, int firstResult) {
        return findTiraEntities(false, maxResults, firstResult);
    }

    private List<Tira> findTiraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tira.class));
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

    public Tira findTira(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tira.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tira> rt = cq.from(Tira.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
