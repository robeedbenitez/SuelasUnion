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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Proveedor;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, Exception {
        if (proveedor.getPedidoProveedorList() == null) {
            proveedor.setPedidoProveedorList(new ArrayList<PedidoProveedor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PedidoProveedor> attachedPedidoProveedorList = new ArrayList<PedidoProveedor>();
            for (PedidoProveedor pedidoProveedorListPedidoProveedorToAttach : proveedor.getPedidoProveedorList()) {
                pedidoProveedorListPedidoProveedorToAttach = em.getReference(pedidoProveedorListPedidoProveedorToAttach.getClass(), pedidoProveedorListPedidoProveedorToAttach.getId());
                attachedPedidoProveedorList.add(pedidoProveedorListPedidoProveedorToAttach);
            }
            proveedor.setPedidoProveedorList(attachedPedidoProveedorList);
            em.persist(proveedor);
            for (PedidoProveedor pedidoProveedorListPedidoProveedor : proveedor.getPedidoProveedorList()) {
                Proveedor oldProveedorIdOfPedidoProveedorListPedidoProveedor = pedidoProveedorListPedidoProveedor.getProveedorId();
                pedidoProveedorListPedidoProveedor.setProveedorId(proveedor);
                pedidoProveedorListPedidoProveedor = em.merge(pedidoProveedorListPedidoProveedor);
                if (oldProveedorIdOfPedidoProveedorListPedidoProveedor != null) {
                    oldProveedorIdOfPedidoProveedorListPedidoProveedor.getPedidoProveedorList().remove(pedidoProveedorListPedidoProveedor);
                    oldProveedorIdOfPedidoProveedorListPedidoProveedor = em.merge(oldProveedorIdOfPedidoProveedorListPedidoProveedor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedor(proveedor.getId()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getId());
            List<PedidoProveedor> pedidoProveedorListOld = persistentProveedor.getPedidoProveedorList();
            List<PedidoProveedor> pedidoProveedorListNew = proveedor.getPedidoProveedorList();
            List<String> illegalOrphanMessages = null;
            for (PedidoProveedor pedidoProveedorListOldPedidoProveedor : pedidoProveedorListOld) {
                if (!pedidoProveedorListNew.contains(pedidoProveedorListOldPedidoProveedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoProveedor " + pedidoProveedorListOldPedidoProveedor + " since its proveedorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PedidoProveedor> attachedPedidoProveedorListNew = new ArrayList<PedidoProveedor>();
            for (PedidoProveedor pedidoProveedorListNewPedidoProveedorToAttach : pedidoProveedorListNew) {
                pedidoProveedorListNewPedidoProveedorToAttach = em.getReference(pedidoProveedorListNewPedidoProveedorToAttach.getClass(), pedidoProveedorListNewPedidoProveedorToAttach.getId());
                attachedPedidoProveedorListNew.add(pedidoProveedorListNewPedidoProveedorToAttach);
            }
            pedidoProveedorListNew = attachedPedidoProveedorListNew;
            proveedor.setPedidoProveedorList(pedidoProveedorListNew);
            proveedor = em.merge(proveedor);
            for (PedidoProveedor pedidoProveedorListNewPedidoProveedor : pedidoProveedorListNew) {
                if (!pedidoProveedorListOld.contains(pedidoProveedorListNewPedidoProveedor)) {
                    Proveedor oldProveedorIdOfPedidoProveedorListNewPedidoProveedor = pedidoProveedorListNewPedidoProveedor.getProveedorId();
                    pedidoProveedorListNewPedidoProveedor.setProveedorId(proveedor);
                    pedidoProveedorListNewPedidoProveedor = em.merge(pedidoProveedorListNewPedidoProveedor);
                    if (oldProveedorIdOfPedidoProveedorListNewPedidoProveedor != null && !oldProveedorIdOfPedidoProveedorListNewPedidoProveedor.equals(proveedor)) {
                        oldProveedorIdOfPedidoProveedorListNewPedidoProveedor.getPedidoProveedorList().remove(pedidoProveedorListNewPedidoProveedor);
                        oldProveedorIdOfPedidoProveedorListNewPedidoProveedor = em.merge(oldProveedorIdOfPedidoProveedorListNewPedidoProveedor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getId();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoProveedor> pedidoProveedorListOrphanCheck = proveedor.getPedidoProveedorList();
            for (PedidoProveedor pedidoProveedorListOrphanCheckPedidoProveedor : pedidoProveedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the PedidoProveedor " + pedidoProveedorListOrphanCheckPedidoProveedor + " in its pedidoProveedorList field has a non-nullable proveedorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
