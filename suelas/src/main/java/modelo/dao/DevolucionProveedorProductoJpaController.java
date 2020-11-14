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
import modelo.dto.DevolucionProveedor;
import modelo.dto.DevolucionProveedorProducto;
import modelo.dto.DevolucionProveedorProductoPK;
import modelo.dto.Producto;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class DevolucionProveedorProductoJpaController implements Serializable {

    public DevolucionProveedorProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DevolucionProveedorProducto devolucionProveedorProducto) throws PreexistingEntityException, Exception {
        if (devolucionProveedorProducto.getDevolucionProveedorProductoPK() == null) {
            devolucionProveedorProducto.setDevolucionProveedorProductoPK(new DevolucionProveedorProductoPK());
        }
        devolucionProveedorProducto.getDevolucionProveedorProductoPK().setProductoId(devolucionProveedorProducto.getProducto().getId());
        devolucionProveedorProducto.getDevolucionProveedorProductoPK().setDevolucionProveedorId(devolucionProveedorProducto.getDevolucionProveedor().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionProveedor devolucionProveedor = devolucionProveedorProducto.getDevolucionProveedor();
            if (devolucionProveedor != null) {
                devolucionProveedor = em.getReference(devolucionProveedor.getClass(), devolucionProveedor.getId());
                devolucionProveedorProducto.setDevolucionProveedor(devolucionProveedor);
            }
            Producto producto = devolucionProveedorProducto.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                devolucionProveedorProducto.setProducto(producto);
            }
            em.persist(devolucionProveedorProducto);
            if (devolucionProveedor != null) {
                devolucionProveedor.getDevolucionProveedorProductoList().add(devolucionProveedorProducto);
                devolucionProveedor = em.merge(devolucionProveedor);
            }
            if (producto != null) {
                producto.getDevolucionProveedorProductoList().add(devolucionProveedorProducto);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDevolucionProveedorProducto(devolucionProveedorProducto.getDevolucionProveedorProductoPK()) != null) {
                throw new PreexistingEntityException("DevolucionProveedorProducto " + devolucionProveedorProducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DevolucionProveedorProducto devolucionProveedorProducto) throws NonexistentEntityException, Exception {
        devolucionProveedorProducto.getDevolucionProveedorProductoPK().setProductoId(devolucionProveedorProducto.getProducto().getId());
        devolucionProveedorProducto.getDevolucionProveedorProductoPK().setDevolucionProveedorId(devolucionProveedorProducto.getDevolucionProveedor().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionProveedorProducto persistentDevolucionProveedorProducto = em.find(DevolucionProveedorProducto.class, devolucionProveedorProducto.getDevolucionProveedorProductoPK());
            DevolucionProveedor devolucionProveedorOld = persistentDevolucionProveedorProducto.getDevolucionProveedor();
            DevolucionProveedor devolucionProveedorNew = devolucionProveedorProducto.getDevolucionProveedor();
            Producto productoOld = persistentDevolucionProveedorProducto.getProducto();
            Producto productoNew = devolucionProveedorProducto.getProducto();
            if (devolucionProveedorNew != null) {
                devolucionProveedorNew = em.getReference(devolucionProveedorNew.getClass(), devolucionProveedorNew.getId());
                devolucionProveedorProducto.setDevolucionProveedor(devolucionProveedorNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                devolucionProveedorProducto.setProducto(productoNew);
            }
            devolucionProveedorProducto = em.merge(devolucionProveedorProducto);
            if (devolucionProveedorOld != null && !devolucionProveedorOld.equals(devolucionProveedorNew)) {
                devolucionProveedorOld.getDevolucionProveedorProductoList().remove(devolucionProveedorProducto);
                devolucionProveedorOld = em.merge(devolucionProveedorOld);
            }
            if (devolucionProveedorNew != null && !devolucionProveedorNew.equals(devolucionProveedorOld)) {
                devolucionProveedorNew.getDevolucionProveedorProductoList().add(devolucionProveedorProducto);
                devolucionProveedorNew = em.merge(devolucionProveedorNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDevolucionProveedorProductoList().remove(devolucionProveedorProducto);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDevolucionProveedorProductoList().add(devolucionProveedorProducto);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DevolucionProveedorProductoPK id = devolucionProveedorProducto.getDevolucionProveedorProductoPK();
                if (findDevolucionProveedorProducto(id) == null) {
                    throw new NonexistentEntityException("The devolucionProveedorProducto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DevolucionProveedorProductoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionProveedorProducto devolucionProveedorProducto;
            try {
                devolucionProveedorProducto = em.getReference(DevolucionProveedorProducto.class, id);
                devolucionProveedorProducto.getDevolucionProveedorProductoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucionProveedorProducto with id " + id + " no longer exists.", enfe);
            }
            DevolucionProveedor devolucionProveedor = devolucionProveedorProducto.getDevolucionProveedor();
            if (devolucionProveedor != null) {
                devolucionProveedor.getDevolucionProveedorProductoList().remove(devolucionProveedorProducto);
                devolucionProveedor = em.merge(devolucionProveedor);
            }
            Producto producto = devolucionProveedorProducto.getProducto();
            if (producto != null) {
                producto.getDevolucionProveedorProductoList().remove(devolucionProveedorProducto);
                producto = em.merge(producto);
            }
            em.remove(devolucionProveedorProducto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DevolucionProveedorProducto> findDevolucionProveedorProductoEntities() {
        return findDevolucionProveedorProductoEntities(true, -1, -1);
    }

    public List<DevolucionProveedorProducto> findDevolucionProveedorProductoEntities(int maxResults, int firstResult) {
        return findDevolucionProveedorProductoEntities(false, maxResults, firstResult);
    }

    private List<DevolucionProveedorProducto> findDevolucionProveedorProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DevolucionProveedorProducto.class));
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

    public DevolucionProveedorProducto findDevolucionProveedorProducto(DevolucionProveedorProductoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DevolucionProveedorProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionProveedorProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DevolucionProveedorProducto> rt = cq.from(DevolucionProveedorProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
