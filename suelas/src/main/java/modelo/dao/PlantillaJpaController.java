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
import modelo.dto.Plantilla;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class PlantillaJpaController implements Serializable {

    public PlantillaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plantilla plantilla) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Producto productoOrphanCheck = plantilla.getProducto();
        if (productoOrphanCheck != null) {
            Plantilla oldPlantillaOfProducto = productoOrphanCheck.getPlantilla();
            if (oldPlantillaOfProducto != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Producto " + productoOrphanCheck + " already has an item of type Plantilla whose producto column cannot be null. Please make another selection for the producto field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = plantilla.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                plantilla.setProducto(producto);
            }
            em.persist(plantilla);
            if (producto != null) {
                producto.setPlantilla(plantilla);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlantilla(plantilla.getProductoId()) != null) {
                throw new PreexistingEntityException("Plantilla " + plantilla + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plantilla plantilla) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Plantilla persistentPlantilla = em.find(Plantilla.class, plantilla.getProductoId());
            Producto productoOld = persistentPlantilla.getProducto();
            Producto productoNew = plantilla.getProducto();
            List<String> illegalOrphanMessages = null;
            if (productoNew != null && !productoNew.equals(productoOld)) {
                Plantilla oldPlantillaOfProducto = productoNew.getPlantilla();
                if (oldPlantillaOfProducto != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Producto " + productoNew + " already has an item of type Plantilla whose producto column cannot be null. Please make another selection for the producto field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                plantilla.setProducto(productoNew);
            }
            plantilla = em.merge(plantilla);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.setPlantilla(null);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.setPlantilla(plantilla);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = plantilla.getProductoId();
                if (findPlantilla(id) == null) {
                    throw new NonexistentEntityException("The plantilla with id " + id + " no longer exists.");
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
            Plantilla plantilla;
            try {
                plantilla = em.getReference(Plantilla.class, id);
                plantilla.getProductoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plantilla with id " + id + " no longer exists.", enfe);
            }
            Producto producto = plantilla.getProducto();
            if (producto != null) {
                producto.setPlantilla(null);
                producto = em.merge(producto);
            }
            em.remove(plantilla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Plantilla> findPlantillaEntities() {
        return findPlantillaEntities(true, -1, -1);
    }

    public List<Plantilla> findPlantillaEntities(int maxResults, int firstResult) {
        return findPlantillaEntities(false, maxResults, firstResult);
    }

    private List<Plantilla> findPlantillaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plantilla.class));
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

    public Plantilla findPlantilla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plantilla.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlantillaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plantilla> rt = cq.from(Plantilla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
