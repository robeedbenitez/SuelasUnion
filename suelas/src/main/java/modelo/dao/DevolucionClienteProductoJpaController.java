/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dto.DevolucionCliente;
import modelo.dto.DevolucionClienteProducto;
import modelo.dto.DevolucionClienteProductoPK;
import modelo.dto.Producto;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class DevolucionClienteProductoJpaController implements Serializable {

    public DevolucionClienteProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DevolucionClienteProducto devolucionClienteProducto) throws PreexistingEntityException, Exception {
        if (devolucionClienteProducto.getDevolucionClienteProductoPK() == null) {
            devolucionClienteProducto.setDevolucionClienteProductoPK(new DevolucionClienteProductoPK());
        }
        devolucionClienteProducto.getDevolucionClienteProductoPK().setDevolucionClienteId(devolucionClienteProducto.getDevolucionCliente().getId());
        devolucionClienteProducto.getDevolucionClienteProductoPK().setProductoId(devolucionClienteProducto.getProducto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionCliente devolucionCliente = devolucionClienteProducto.getDevolucionCliente();
            if (devolucionCliente != null) {
                devolucionCliente = em.getReference(devolucionCliente.getClass(), devolucionCliente.getId());
                devolucionClienteProducto.setDevolucionCliente(devolucionCliente);
            }
            Producto producto = devolucionClienteProducto.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                devolucionClienteProducto.setProducto(producto);
            }
            em.persist(devolucionClienteProducto);
            if (devolucionCliente != null) {
                devolucionCliente.getDevolucionClienteProductoList().add(devolucionClienteProducto);
                devolucionCliente = em.merge(devolucionCliente);
            }
            if (producto != null) {
                producto.getDevolucionClienteProductoList().add(devolucionClienteProducto);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDevolucionClienteProducto(devolucionClienteProducto.getDevolucionClienteProductoPK()) != null) {
                throw new PreexistingEntityException("DevolucionClienteProducto " + devolucionClienteProducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DevolucionClienteProducto devolucionClienteProducto) throws NonexistentEntityException, Exception {
        devolucionClienteProducto.getDevolucionClienteProductoPK().setDevolucionClienteId(devolucionClienteProducto.getDevolucionCliente().getId());
        devolucionClienteProducto.getDevolucionClienteProductoPK().setProductoId(devolucionClienteProducto.getProducto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionClienteProducto persistentDevolucionClienteProducto = em.find(DevolucionClienteProducto.class, devolucionClienteProducto.getDevolucionClienteProductoPK());
            DevolucionCliente devolucionClienteOld = persistentDevolucionClienteProducto.getDevolucionCliente();
            DevolucionCliente devolucionClienteNew = devolucionClienteProducto.getDevolucionCliente();
            Producto productoOld = persistentDevolucionClienteProducto.getProducto();
            Producto productoNew = devolucionClienteProducto.getProducto();
            if (devolucionClienteNew != null) {
                devolucionClienteNew = em.getReference(devolucionClienteNew.getClass(), devolucionClienteNew.getId());
                devolucionClienteProducto.setDevolucionCliente(devolucionClienteNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                devolucionClienteProducto.setProducto(productoNew);
            }
            devolucionClienteProducto = em.merge(devolucionClienteProducto);
            if (devolucionClienteOld != null && !devolucionClienteOld.equals(devolucionClienteNew)) {
                devolucionClienteOld.getDevolucionClienteProductoList().remove(devolucionClienteProducto);
                devolucionClienteOld = em.merge(devolucionClienteOld);
            }
            if (devolucionClienteNew != null && !devolucionClienteNew.equals(devolucionClienteOld)) {
                devolucionClienteNew.getDevolucionClienteProductoList().add(devolucionClienteProducto);
                devolucionClienteNew = em.merge(devolucionClienteNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDevolucionClienteProductoList().remove(devolucionClienteProducto);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDevolucionClienteProductoList().add(devolucionClienteProducto);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DevolucionClienteProductoPK id = devolucionClienteProducto.getDevolucionClienteProductoPK();
                if (findDevolucionClienteProducto(id) == null) {
                    throw new NonexistentEntityException("The devolucionClienteProducto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DevolucionClienteProductoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionClienteProducto devolucionClienteProducto;
            try {
                devolucionClienteProducto = em.getReference(DevolucionClienteProducto.class, id);
                devolucionClienteProducto.getDevolucionClienteProductoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucionClienteProducto with id " + id + " no longer exists.", enfe);
            }
            DevolucionCliente devolucionCliente = devolucionClienteProducto.getDevolucionCliente();
            if (devolucionCliente != null) {
                devolucionCliente.getDevolucionClienteProductoList().remove(devolucionClienteProducto);
                devolucionCliente = em.merge(devolucionCliente);
            }
            Producto producto = devolucionClienteProducto.getProducto();
            if (producto != null) {
                producto.getDevolucionClienteProductoList().remove(devolucionClienteProducto);
                producto = em.merge(producto);
            }
            em.remove(devolucionClienteProducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DevolucionClienteProducto> findDevolucionClienteProductoEntities() {
        return findDevolucionClienteProductoEntities(true, -1, -1);
    }

    public List<DevolucionClienteProducto> findDevolucionClienteProductoEntities(int maxResults, int firstResult) {
        return findDevolucionClienteProductoEntities(false, maxResults, firstResult);
    }

    private List<DevolucionClienteProducto> findDevolucionClienteProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DevolucionClienteProducto.class));
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

    public DevolucionClienteProducto findDevolucionClienteProducto(DevolucionClienteProductoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DevolucionClienteProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionClienteProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DevolucionClienteProducto> rt = cq.from(DevolucionClienteProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
