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
import modelo.dto.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Salpa;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class SalpaJpaController implements Serializable {

    public SalpaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salpa salpa) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Producto productoOrphanCheck = salpa.getProducto();
        if (productoOrphanCheck != null) {
            Salpa oldSalpaOfProducto = productoOrphanCheck.getSalpa();
            if (oldSalpaOfProducto != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Producto " + productoOrphanCheck + " already has an item of type Salpa whose producto column cannot be null. Please make another selection for the producto field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = salpa.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                salpa.setProducto(producto);
            }
            em.persist(salpa);
            if (producto != null) {
                producto.setSalpa(salpa);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSalpa(salpa.getProductoId()) != null) {
                throw new PreexistingEntityException("Salpa " + salpa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salpa salpa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salpa persistentSalpa = em.find(Salpa.class, salpa.getProductoId());
            Producto productoOld = persistentSalpa.getProducto();
            Producto productoNew = salpa.getProducto();
            List<String> illegalOrphanMessages = null;
            if (productoNew != null && !productoNew.equals(productoOld)) {
                Salpa oldSalpaOfProducto = productoNew.getSalpa();
                if (oldSalpaOfProducto != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Producto " + productoNew + " already has an item of type Salpa whose producto column cannot be null. Please make another selection for the producto field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                salpa.setProducto(productoNew);
            }
            salpa = em.merge(salpa);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.setSalpa(null);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.setSalpa(salpa);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = salpa.getProductoId();
                if (findSalpa(id) == null) {
                    throw new NonexistentEntityException("The salpa with id " + id + " no longer exists.");
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
            Salpa salpa;
            try {
                salpa = em.getReference(Salpa.class, id);
                salpa.getProductoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salpa with id " + id + " no longer exists.", enfe);
            }
            Producto producto = salpa.getProducto();
            if (producto != null) {
                producto.setSalpa(null);
                producto = em.merge(producto);
            }
            em.remove(salpa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salpa> findSalpaEntities() {
        return findSalpaEntities(true, -1, -1);
    }

    public List<Salpa> findSalpaEntities(int maxResults, int firstResult) {
        return findSalpaEntities(false, maxResults, firstResult);
    }

    private List<Salpa> findSalpaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salpa.class));
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

    public Salpa findSalpa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salpa.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalpaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salpa> rt = cq.from(Salpa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
