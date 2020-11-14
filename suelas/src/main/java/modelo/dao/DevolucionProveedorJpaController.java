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
import modelo.dto.PedidoProveedor;
import modelo.dto.DevolucionProveedorProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.DevolucionProveedor;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class DevolucionProveedorJpaController implements Serializable {

    public DevolucionProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DevolucionProveedor devolucionProveedor) throws PreexistingEntityException, Exception {
        if (devolucionProveedor.getDevolucionProveedorProductoList() == null) {
            devolucionProveedor.setDevolucionProveedorProductoList(new ArrayList<DevolucionProveedorProducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoProveedor pedidoProveedorId = devolucionProveedor.getPedidoProveedorId();
            if (pedidoProveedorId != null) {
                pedidoProveedorId = em.getReference(pedidoProveedorId.getClass(), pedidoProveedorId.getId());
                devolucionProveedor.setPedidoProveedorId(pedidoProveedorId);
            }
            List<DevolucionProveedorProducto> attachedDevolucionProveedorProductoList = new ArrayList<DevolucionProveedorProducto>();
            for (DevolucionProveedorProducto devolucionProveedorProductoListDevolucionProveedorProductoToAttach : devolucionProveedor.getDevolucionProveedorProductoList()) {
                devolucionProveedorProductoListDevolucionProveedorProductoToAttach = em.getReference(devolucionProveedorProductoListDevolucionProveedorProductoToAttach.getClass(), devolucionProveedorProductoListDevolucionProveedorProductoToAttach.getDevolucionProveedorProductoPK());
                attachedDevolucionProveedorProductoList.add(devolucionProveedorProductoListDevolucionProveedorProductoToAttach);
            }
            devolucionProveedor.setDevolucionProveedorProductoList(attachedDevolucionProveedorProductoList);
            em.persist(devolucionProveedor);
            if (pedidoProveedorId != null) {
                pedidoProveedorId.getDevolucionProveedorList().add(devolucionProveedor);
                pedidoProveedorId = em.merge(pedidoProveedorId);
            }
            for (DevolucionProveedorProducto devolucionProveedorProductoListDevolucionProveedorProducto : devolucionProveedor.getDevolucionProveedorProductoList()) {
                DevolucionProveedor oldDevolucionProveedorOfDevolucionProveedorProductoListDevolucionProveedorProducto = devolucionProveedorProductoListDevolucionProveedorProducto.getDevolucionProveedor();
                devolucionProveedorProductoListDevolucionProveedorProducto.setDevolucionProveedor(devolucionProveedor);
                devolucionProveedorProductoListDevolucionProveedorProducto = em.merge(devolucionProveedorProductoListDevolucionProveedorProducto);
                if (oldDevolucionProveedorOfDevolucionProveedorProductoListDevolucionProveedorProducto != null) {
                    oldDevolucionProveedorOfDevolucionProveedorProductoListDevolucionProveedorProducto.getDevolucionProveedorProductoList().remove(devolucionProveedorProductoListDevolucionProveedorProducto);
                    oldDevolucionProveedorOfDevolucionProveedorProductoListDevolucionProveedorProducto = em.merge(oldDevolucionProveedorOfDevolucionProveedorProductoListDevolucionProveedorProducto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDevolucionProveedor(devolucionProveedor.getId()) != null) {
                throw new PreexistingEntityException("DevolucionProveedor " + devolucionProveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DevolucionProveedor devolucionProveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionProveedor persistentDevolucionProveedor = em.find(DevolucionProveedor.class, devolucionProveedor.getId());
            PedidoProveedor pedidoProveedorIdOld = persistentDevolucionProveedor.getPedidoProveedorId();
            PedidoProveedor pedidoProveedorIdNew = devolucionProveedor.getPedidoProveedorId();
            List<DevolucionProveedorProducto> devolucionProveedorProductoListOld = persistentDevolucionProveedor.getDevolucionProveedorProductoList();
            List<DevolucionProveedorProducto> devolucionProveedorProductoListNew = devolucionProveedor.getDevolucionProveedorProductoList();
            List<String> illegalOrphanMessages = null;
            for (DevolucionProveedorProducto devolucionProveedorProductoListOldDevolucionProveedorProducto : devolucionProveedorProductoListOld) {
                if (!devolucionProveedorProductoListNew.contains(devolucionProveedorProductoListOldDevolucionProveedorProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionProveedorProducto " + devolucionProveedorProductoListOldDevolucionProveedorProducto + " since its devolucionProveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pedidoProveedorIdNew != null) {
                pedidoProveedorIdNew = em.getReference(pedidoProveedorIdNew.getClass(), pedidoProveedorIdNew.getId());
                devolucionProveedor.setPedidoProveedorId(pedidoProveedorIdNew);
            }
            List<DevolucionProveedorProducto> attachedDevolucionProveedorProductoListNew = new ArrayList<DevolucionProveedorProducto>();
            for (DevolucionProveedorProducto devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach : devolucionProveedorProductoListNew) {
                devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach = em.getReference(devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach.getClass(), devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach.getDevolucionProveedorProductoPK());
                attachedDevolucionProveedorProductoListNew.add(devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach);
            }
            devolucionProveedorProductoListNew = attachedDevolucionProveedorProductoListNew;
            devolucionProveedor.setDevolucionProveedorProductoList(devolucionProveedorProductoListNew);
            devolucionProveedor = em.merge(devolucionProveedor);
            if (pedidoProveedorIdOld != null && !pedidoProveedorIdOld.equals(pedidoProveedorIdNew)) {
                pedidoProveedorIdOld.getDevolucionProveedorList().remove(devolucionProveedor);
                pedidoProveedorIdOld = em.merge(pedidoProveedorIdOld);
            }
            if (pedidoProveedorIdNew != null && !pedidoProveedorIdNew.equals(pedidoProveedorIdOld)) {
                pedidoProveedorIdNew.getDevolucionProveedorList().add(devolucionProveedor);
                pedidoProveedorIdNew = em.merge(pedidoProveedorIdNew);
            }
            for (DevolucionProveedorProducto devolucionProveedorProductoListNewDevolucionProveedorProducto : devolucionProveedorProductoListNew) {
                if (!devolucionProveedorProductoListOld.contains(devolucionProveedorProductoListNewDevolucionProveedorProducto)) {
                    DevolucionProveedor oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto = devolucionProveedorProductoListNewDevolucionProveedorProducto.getDevolucionProveedor();
                    devolucionProveedorProductoListNewDevolucionProveedorProducto.setDevolucionProveedor(devolucionProveedor);
                    devolucionProveedorProductoListNewDevolucionProveedorProducto = em.merge(devolucionProveedorProductoListNewDevolucionProveedorProducto);
                    if (oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto != null && !oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto.equals(devolucionProveedor)) {
                        oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto.getDevolucionProveedorProductoList().remove(devolucionProveedorProductoListNewDevolucionProveedorProducto);
                        oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto = em.merge(oldDevolucionProveedorOfDevolucionProveedorProductoListNewDevolucionProveedorProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = devolucionProveedor.getId();
                if (findDevolucionProveedor(id) == null) {
                    throw new NonexistentEntityException("The devolucionProveedor with id " + id + " no longer exists.");
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
            DevolucionProveedor devolucionProveedor;
            try {
                devolucionProveedor = em.getReference(DevolucionProveedor.class, id);
                devolucionProveedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucionProveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DevolucionProveedorProducto> devolucionProveedorProductoListOrphanCheck = devolucionProveedor.getDevolucionProveedorProductoList();
            for (DevolucionProveedorProducto devolucionProveedorProductoListOrphanCheckDevolucionProveedorProducto : devolucionProveedorProductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DevolucionProveedor (" + devolucionProveedor + ") cannot be destroyed since the DevolucionProveedorProducto " + devolucionProveedorProductoListOrphanCheckDevolucionProveedorProducto + " in its devolucionProveedorProductoList field has a non-nullable devolucionProveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PedidoProveedor pedidoProveedorId = devolucionProveedor.getPedidoProveedorId();
            if (pedidoProveedorId != null) {
                pedidoProveedorId.getDevolucionProveedorList().remove(devolucionProveedor);
                pedidoProveedorId = em.merge(pedidoProveedorId);
            }
            em.remove(devolucionProveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DevolucionProveedor> findDevolucionProveedorEntities() {
        return findDevolucionProveedorEntities(true, -1, -1);
    }

    public List<DevolucionProveedor> findDevolucionProveedorEntities(int maxResults, int firstResult) {
        return findDevolucionProveedorEntities(false, maxResults, firstResult);
    }

    private List<DevolucionProveedor> findDevolucionProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DevolucionProveedor.class));
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

    public DevolucionProveedor findDevolucionProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DevolucionProveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DevolucionProveedor> rt = cq.from(DevolucionProveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
