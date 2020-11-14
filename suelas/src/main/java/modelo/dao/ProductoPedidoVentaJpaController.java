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
import modelo.dto.PedidoDeVenta;
import modelo.dto.Producto;
import modelo.dto.ProductoPedidoVenta;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class ProductoPedidoVentaJpaController implements Serializable {

    public ProductoPedidoVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoPedidoVenta productoPedidoVenta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoDeVenta pedidoDeVentaIdPedidoDeVenta = productoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta = em.getReference(pedidoDeVentaIdPedidoDeVenta.getClass(), pedidoDeVentaIdPedidoDeVenta.getIdPedidoDeVenta());
                productoPedidoVenta.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVentaIdPedidoDeVenta);
            }
            Producto productoId = productoPedidoVenta.getProductoId();
            if (productoId != null) {
                productoId = em.getReference(productoId.getClass(), productoId.getId());
                productoPedidoVenta.setProductoId(productoId);
            }
            em.persist(productoPedidoVenta);
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta.getProductoPedidoVentaList().add(productoPedidoVenta);
                pedidoDeVentaIdPedidoDeVenta = em.merge(pedidoDeVentaIdPedidoDeVenta);
            }
            if (productoId != null) {
                productoId.getProductoPedidoVentaList().add(productoPedidoVenta);
                productoId = em.merge(productoId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductoPedidoVenta(productoPedidoVenta.getIdProductoCliente()) != null) {
                throw new PreexistingEntityException("ProductoPedidoVenta " + productoPedidoVenta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoPedidoVenta productoPedidoVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoPedidoVenta persistentProductoPedidoVenta = em.find(ProductoPedidoVenta.class, productoPedidoVenta.getIdProductoCliente());
            PedidoDeVenta pedidoDeVentaIdPedidoDeVentaOld = persistentProductoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
            PedidoDeVenta pedidoDeVentaIdPedidoDeVentaNew = productoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
            Producto productoIdOld = persistentProductoPedidoVenta.getProductoId();
            Producto productoIdNew = productoPedidoVenta.getProductoId();
            if (pedidoDeVentaIdPedidoDeVentaNew != null) {
                pedidoDeVentaIdPedidoDeVentaNew = em.getReference(pedidoDeVentaIdPedidoDeVentaNew.getClass(), pedidoDeVentaIdPedidoDeVentaNew.getIdPedidoDeVenta());
                productoPedidoVenta.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVentaIdPedidoDeVentaNew);
            }
            if (productoIdNew != null) {
                productoIdNew = em.getReference(productoIdNew.getClass(), productoIdNew.getId());
                productoPedidoVenta.setProductoId(productoIdNew);
            }
            productoPedidoVenta = em.merge(productoPedidoVenta);
            if (pedidoDeVentaIdPedidoDeVentaOld != null && !pedidoDeVentaIdPedidoDeVentaOld.equals(pedidoDeVentaIdPedidoDeVentaNew)) {
                pedidoDeVentaIdPedidoDeVentaOld.getProductoPedidoVentaList().remove(productoPedidoVenta);
                pedidoDeVentaIdPedidoDeVentaOld = em.merge(pedidoDeVentaIdPedidoDeVentaOld);
            }
            if (pedidoDeVentaIdPedidoDeVentaNew != null && !pedidoDeVentaIdPedidoDeVentaNew.equals(pedidoDeVentaIdPedidoDeVentaOld)) {
                pedidoDeVentaIdPedidoDeVentaNew.getProductoPedidoVentaList().add(productoPedidoVenta);
                pedidoDeVentaIdPedidoDeVentaNew = em.merge(pedidoDeVentaIdPedidoDeVentaNew);
            }
            if (productoIdOld != null && !productoIdOld.equals(productoIdNew)) {
                productoIdOld.getProductoPedidoVentaList().remove(productoPedidoVenta);
                productoIdOld = em.merge(productoIdOld);
            }
            if (productoIdNew != null && !productoIdNew.equals(productoIdOld)) {
                productoIdNew.getProductoPedidoVentaList().add(productoPedidoVenta);
                productoIdNew = em.merge(productoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productoPedidoVenta.getIdProductoCliente();
                if (findProductoPedidoVenta(id) == null) {
                    throw new NonexistentEntityException("The productoPedidoVenta with id " + id + " no longer exists.");
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
            ProductoPedidoVenta productoPedidoVenta;
            try {
                productoPedidoVenta = em.getReference(ProductoPedidoVenta.class, id);
                productoPedidoVenta.getIdProductoCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoPedidoVenta with id " + id + " no longer exists.", enfe);
            }
            PedidoDeVenta pedidoDeVentaIdPedidoDeVenta = productoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta.getProductoPedidoVentaList().remove(productoPedidoVenta);
                pedidoDeVentaIdPedidoDeVenta = em.merge(pedidoDeVentaIdPedidoDeVenta);
            }
            Producto productoId = productoPedidoVenta.getProductoId();
            if (productoId != null) {
                productoId.getProductoPedidoVentaList().remove(productoPedidoVenta);
                productoId = em.merge(productoId);
            }
            em.remove(productoPedidoVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoPedidoVenta> findProductoPedidoVentaEntities() {
        return findProductoPedidoVentaEntities(true, -1, -1);
    }

    public List<ProductoPedidoVenta> findProductoPedidoVentaEntities(int maxResults, int firstResult) {
        return findProductoPedidoVentaEntities(false, maxResults, firstResult);
    }

    private List<ProductoPedidoVenta> findProductoPedidoVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoPedidoVenta.class));
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

    public ProductoPedidoVenta findProductoPedidoVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoPedidoVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoPedidoVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoPedidoVenta> rt = cq.from(ProductoPedidoVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
