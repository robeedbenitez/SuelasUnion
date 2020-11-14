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
import modelo.dto.PedidoProveedor;
import modelo.dto.Producto;
import modelo.dto.ProductoPedidoProveedor;
import modelo.dto.ProductoPedidoProveedorPK;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class ProductoPedidoProveedorJpaController implements Serializable {

    public ProductoPedidoProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoPedidoProveedor productoPedidoProveedor) throws PreexistingEntityException, Exception {
        if (productoPedidoProveedor.getProductoPedidoProveedorPK() == null) {
            productoPedidoProveedor.setProductoPedidoProveedorPK(new ProductoPedidoProveedorPK());
        }
        productoPedidoProveedor.getProductoPedidoProveedorPK().setPedidoProveedorId(productoPedidoProveedor.getPedidoProveedor().getId());
        productoPedidoProveedor.getProductoPedidoProveedorPK().setProductoId(productoPedidoProveedor.getProducto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoProveedor pedidoProveedor = productoPedidoProveedor.getPedidoProveedor();
            if (pedidoProveedor != null) {
                pedidoProveedor = em.getReference(pedidoProveedor.getClass(), pedidoProveedor.getId());
                productoPedidoProveedor.setPedidoProveedor(pedidoProveedor);
            }
            Producto producto = productoPedidoProveedor.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                productoPedidoProveedor.setProducto(producto);
            }
            em.persist(productoPedidoProveedor);
            if (pedidoProveedor != null) {
                pedidoProveedor.getProductoPedidoProveedorList().add(productoPedidoProveedor);
                pedidoProveedor = em.merge(pedidoProveedor);
            }
            if (producto != null) {
                producto.getProductoPedidoProveedorList().add(productoPedidoProveedor);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductoPedidoProveedor(productoPedidoProveedor.getProductoPedidoProveedorPK()) != null) {
                throw new PreexistingEntityException("ProductoPedidoProveedor " + productoPedidoProveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoPedidoProveedor productoPedidoProveedor) throws NonexistentEntityException, Exception {
        productoPedidoProveedor.getProductoPedidoProveedorPK().setPedidoProveedorId(productoPedidoProveedor.getPedidoProveedor().getId());
        productoPedidoProveedor.getProductoPedidoProveedorPK().setProductoId(productoPedidoProveedor.getProducto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoPedidoProveedor persistentProductoPedidoProveedor = em.find(ProductoPedidoProveedor.class, productoPedidoProveedor.getProductoPedidoProveedorPK());
            PedidoProveedor pedidoProveedorOld = persistentProductoPedidoProveedor.getPedidoProveedor();
            PedidoProveedor pedidoProveedorNew = productoPedidoProveedor.getPedidoProveedor();
            Producto productoOld = persistentProductoPedidoProveedor.getProducto();
            Producto productoNew = productoPedidoProveedor.getProducto();
            if (pedidoProveedorNew != null) {
                pedidoProveedorNew = em.getReference(pedidoProveedorNew.getClass(), pedidoProveedorNew.getId());
                productoPedidoProveedor.setPedidoProveedor(pedidoProveedorNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                productoPedidoProveedor.setProducto(productoNew);
            }
            productoPedidoProveedor = em.merge(productoPedidoProveedor);
            if (pedidoProveedorOld != null && !pedidoProveedorOld.equals(pedidoProveedorNew)) {
                pedidoProveedorOld.getProductoPedidoProveedorList().remove(productoPedidoProveedor);
                pedidoProveedorOld = em.merge(pedidoProveedorOld);
            }
            if (pedidoProveedorNew != null && !pedidoProveedorNew.equals(pedidoProveedorOld)) {
                pedidoProveedorNew.getProductoPedidoProveedorList().add(productoPedidoProveedor);
                pedidoProveedorNew = em.merge(pedidoProveedorNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getProductoPedidoProveedorList().remove(productoPedidoProveedor);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getProductoPedidoProveedorList().add(productoPedidoProveedor);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductoPedidoProveedorPK id = productoPedidoProveedor.getProductoPedidoProveedorPK();
                if (findProductoPedidoProveedor(id) == null) {
                    throw new NonexistentEntityException("The productoPedidoProveedor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductoPedidoProveedorPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoPedidoProveedor productoPedidoProveedor;
            try {
                productoPedidoProveedor = em.getReference(ProductoPedidoProveedor.class, id);
                productoPedidoProveedor.getProductoPedidoProveedorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoPedidoProveedor with id " + id + " no longer exists.", enfe);
            }
            PedidoProveedor pedidoProveedor = productoPedidoProveedor.getPedidoProveedor();
            if (pedidoProveedor != null) {
                pedidoProveedor.getProductoPedidoProveedorList().remove(productoPedidoProveedor);
                pedidoProveedor = em.merge(pedidoProveedor);
            }
            Producto producto = productoPedidoProveedor.getProducto();
            if (producto != null) {
                producto.getProductoPedidoProveedorList().remove(productoPedidoProveedor);
                producto = em.merge(producto);
            }
            em.remove(productoPedidoProveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoPedidoProveedor> findProductoPedidoProveedorEntities() {
        return findProductoPedidoProveedorEntities(true, -1, -1);
    }

    public List<ProductoPedidoProveedor> findProductoPedidoProveedorEntities(int maxResults, int firstResult) {
        return findProductoPedidoProveedorEntities(false, maxResults, firstResult);
    }

    private List<ProductoPedidoProveedor> findProductoPedidoProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoPedidoProveedor.class));
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

    public ProductoPedidoProveedor findProductoPedidoProveedor(ProductoPedidoProveedorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoPedidoProveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoPedidoProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoPedidoProveedor> rt = cq.from(ProductoPedidoProveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
