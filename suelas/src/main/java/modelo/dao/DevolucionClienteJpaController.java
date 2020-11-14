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
import modelo.dto.PedidoDeVenta;
import modelo.dto.DevolucionClienteProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.DevolucionCliente;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class DevolucionClienteJpaController implements Serializable {

    public DevolucionClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DevolucionCliente devolucionCliente) throws PreexistingEntityException, Exception {
        if (devolucionCliente.getDevolucionClienteProductoList() == null) {
            devolucionCliente.setDevolucionClienteProductoList(new ArrayList<DevolucionClienteProducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoDeVenta pedidoDeVentaIdPedidoDeVenta = devolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta = em.getReference(pedidoDeVentaIdPedidoDeVenta.getClass(), pedidoDeVentaIdPedidoDeVenta.getIdPedidoDeVenta());
                devolucionCliente.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVentaIdPedidoDeVenta);
            }
            List<DevolucionClienteProducto> attachedDevolucionClienteProductoList = new ArrayList<DevolucionClienteProducto>();
            for (DevolucionClienteProducto devolucionClienteProductoListDevolucionClienteProductoToAttach : devolucionCliente.getDevolucionClienteProductoList()) {
                devolucionClienteProductoListDevolucionClienteProductoToAttach = em.getReference(devolucionClienteProductoListDevolucionClienteProductoToAttach.getClass(), devolucionClienteProductoListDevolucionClienteProductoToAttach.getDevolucionClienteProductoPK());
                attachedDevolucionClienteProductoList.add(devolucionClienteProductoListDevolucionClienteProductoToAttach);
            }
            devolucionCliente.setDevolucionClienteProductoList(attachedDevolucionClienteProductoList);
            em.persist(devolucionCliente);
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta.getDevolucionClienteList().add(devolucionCliente);
                pedidoDeVentaIdPedidoDeVenta = em.merge(pedidoDeVentaIdPedidoDeVenta);
            }
            for (DevolucionClienteProducto devolucionClienteProductoListDevolucionClienteProducto : devolucionCliente.getDevolucionClienteProductoList()) {
                DevolucionCliente oldDevolucionClienteOfDevolucionClienteProductoListDevolucionClienteProducto = devolucionClienteProductoListDevolucionClienteProducto.getDevolucionCliente();
                devolucionClienteProductoListDevolucionClienteProducto.setDevolucionCliente(devolucionCliente);
                devolucionClienteProductoListDevolucionClienteProducto = em.merge(devolucionClienteProductoListDevolucionClienteProducto);
                if (oldDevolucionClienteOfDevolucionClienteProductoListDevolucionClienteProducto != null) {
                    oldDevolucionClienteOfDevolucionClienteProductoListDevolucionClienteProducto.getDevolucionClienteProductoList().remove(devolucionClienteProductoListDevolucionClienteProducto);
                    oldDevolucionClienteOfDevolucionClienteProductoListDevolucionClienteProducto = em.merge(oldDevolucionClienteOfDevolucionClienteProductoListDevolucionClienteProducto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDevolucionCliente(devolucionCliente.getId()) != null) {
                throw new PreexistingEntityException("DevolucionCliente " + devolucionCliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DevolucionCliente devolucionCliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionCliente persistentDevolucionCliente = em.find(DevolucionCliente.class, devolucionCliente.getId());
            PedidoDeVenta pedidoDeVentaIdPedidoDeVentaOld = persistentDevolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
            PedidoDeVenta pedidoDeVentaIdPedidoDeVentaNew = devolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
            List<DevolucionClienteProducto> devolucionClienteProductoListOld = persistentDevolucionCliente.getDevolucionClienteProductoList();
            List<DevolucionClienteProducto> devolucionClienteProductoListNew = devolucionCliente.getDevolucionClienteProductoList();
            List<String> illegalOrphanMessages = null;
            for (DevolucionClienteProducto devolucionClienteProductoListOldDevolucionClienteProducto : devolucionClienteProductoListOld) {
                if (!devolucionClienteProductoListNew.contains(devolucionClienteProductoListOldDevolucionClienteProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionClienteProducto " + devolucionClienteProductoListOldDevolucionClienteProducto + " since its devolucionCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pedidoDeVentaIdPedidoDeVentaNew != null) {
                pedidoDeVentaIdPedidoDeVentaNew = em.getReference(pedidoDeVentaIdPedidoDeVentaNew.getClass(), pedidoDeVentaIdPedidoDeVentaNew.getIdPedidoDeVenta());
                devolucionCliente.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVentaIdPedidoDeVentaNew);
            }
            List<DevolucionClienteProducto> attachedDevolucionClienteProductoListNew = new ArrayList<DevolucionClienteProducto>();
            for (DevolucionClienteProducto devolucionClienteProductoListNewDevolucionClienteProductoToAttach : devolucionClienteProductoListNew) {
                devolucionClienteProductoListNewDevolucionClienteProductoToAttach = em.getReference(devolucionClienteProductoListNewDevolucionClienteProductoToAttach.getClass(), devolucionClienteProductoListNewDevolucionClienteProductoToAttach.getDevolucionClienteProductoPK());
                attachedDevolucionClienteProductoListNew.add(devolucionClienteProductoListNewDevolucionClienteProductoToAttach);
            }
            devolucionClienteProductoListNew = attachedDevolucionClienteProductoListNew;
            devolucionCliente.setDevolucionClienteProductoList(devolucionClienteProductoListNew);
            devolucionCliente = em.merge(devolucionCliente);
            if (pedidoDeVentaIdPedidoDeVentaOld != null && !pedidoDeVentaIdPedidoDeVentaOld.equals(pedidoDeVentaIdPedidoDeVentaNew)) {
                pedidoDeVentaIdPedidoDeVentaOld.getDevolucionClienteList().remove(devolucionCliente);
                pedidoDeVentaIdPedidoDeVentaOld = em.merge(pedidoDeVentaIdPedidoDeVentaOld);
            }
            if (pedidoDeVentaIdPedidoDeVentaNew != null && !pedidoDeVentaIdPedidoDeVentaNew.equals(pedidoDeVentaIdPedidoDeVentaOld)) {
                pedidoDeVentaIdPedidoDeVentaNew.getDevolucionClienteList().add(devolucionCliente);
                pedidoDeVentaIdPedidoDeVentaNew = em.merge(pedidoDeVentaIdPedidoDeVentaNew);
            }
            for (DevolucionClienteProducto devolucionClienteProductoListNewDevolucionClienteProducto : devolucionClienteProductoListNew) {
                if (!devolucionClienteProductoListOld.contains(devolucionClienteProductoListNewDevolucionClienteProducto)) {
                    DevolucionCliente oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto = devolucionClienteProductoListNewDevolucionClienteProducto.getDevolucionCliente();
                    devolucionClienteProductoListNewDevolucionClienteProducto.setDevolucionCliente(devolucionCliente);
                    devolucionClienteProductoListNewDevolucionClienteProducto = em.merge(devolucionClienteProductoListNewDevolucionClienteProducto);
                    if (oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto != null && !oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto.equals(devolucionCliente)) {
                        oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto.getDevolucionClienteProductoList().remove(devolucionClienteProductoListNewDevolucionClienteProducto);
                        oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto = em.merge(oldDevolucionClienteOfDevolucionClienteProductoListNewDevolucionClienteProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = devolucionCliente.getId();
                if (findDevolucionCliente(id) == null) {
                    throw new NonexistentEntityException("The devolucionCliente with id " + id + " no longer exists.");
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
            DevolucionCliente devolucionCliente;
            try {
                devolucionCliente = em.getReference(DevolucionCliente.class, id);
                devolucionCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucionCliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DevolucionClienteProducto> devolucionClienteProductoListOrphanCheck = devolucionCliente.getDevolucionClienteProductoList();
            for (DevolucionClienteProducto devolucionClienteProductoListOrphanCheckDevolucionClienteProducto : devolucionClienteProductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DevolucionCliente (" + devolucionCliente + ") cannot be destroyed since the DevolucionClienteProducto " + devolucionClienteProductoListOrphanCheckDevolucionClienteProducto + " in its devolucionClienteProductoList field has a non-nullable devolucionCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PedidoDeVenta pedidoDeVentaIdPedidoDeVenta = devolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
            if (pedidoDeVentaIdPedidoDeVenta != null) {
                pedidoDeVentaIdPedidoDeVenta.getDevolucionClienteList().remove(devolucionCliente);
                pedidoDeVentaIdPedidoDeVenta = em.merge(pedidoDeVentaIdPedidoDeVenta);
            }
            em.remove(devolucionCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DevolucionCliente> findDevolucionClienteEntities() {
        return findDevolucionClienteEntities(true, -1, -1);
    }

    public List<DevolucionCliente> findDevolucionClienteEntities(int maxResults, int firstResult) {
        return findDevolucionClienteEntities(false, maxResults, firstResult);
    }

    private List<DevolucionCliente> findDevolucionClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DevolucionCliente.class));
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

    public DevolucionCliente findDevolucionCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DevolucionCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DevolucionCliente> rt = cq.from(DevolucionCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
