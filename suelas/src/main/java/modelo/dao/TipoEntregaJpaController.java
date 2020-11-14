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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.TipoEntrega;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class TipoEntregaJpaController implements Serializable {

    public TipoEntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEntrega tipoEntrega) throws PreexistingEntityException, Exception {
        if (tipoEntrega.getPedidoDeVentaList() == null) {
            tipoEntrega.setPedidoDeVentaList(new ArrayList<PedidoDeVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PedidoDeVenta> attachedPedidoDeVentaList = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVentaToAttach : tipoEntrega.getPedidoDeVentaList()) {
                pedidoDeVentaListPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListPedidoDeVentaToAttach.getClass(), pedidoDeVentaListPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaList.add(pedidoDeVentaListPedidoDeVentaToAttach);
            }
            tipoEntrega.setPedidoDeVentaList(attachedPedidoDeVentaList);
            em.persist(tipoEntrega);
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVenta : tipoEntrega.getPedidoDeVentaList()) {
                TipoEntrega oldTipoEntregaIdOfPedidoDeVentaListPedidoDeVenta = pedidoDeVentaListPedidoDeVenta.getTipoEntregaId();
                pedidoDeVentaListPedidoDeVenta.setTipoEntregaId(tipoEntrega);
                pedidoDeVentaListPedidoDeVenta = em.merge(pedidoDeVentaListPedidoDeVenta);
                if (oldTipoEntregaIdOfPedidoDeVentaListPedidoDeVenta != null) {
                    oldTipoEntregaIdOfPedidoDeVentaListPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListPedidoDeVenta);
                    oldTipoEntregaIdOfPedidoDeVentaListPedidoDeVenta = em.merge(oldTipoEntregaIdOfPedidoDeVentaListPedidoDeVenta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoEntrega(tipoEntrega.getId()) != null) {
                throw new PreexistingEntityException("TipoEntrega " + tipoEntrega + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEntrega tipoEntrega) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEntrega persistentTipoEntrega = em.find(TipoEntrega.class, tipoEntrega.getId());
            List<PedidoDeVenta> pedidoDeVentaListOld = persistentTipoEntrega.getPedidoDeVentaList();
            List<PedidoDeVenta> pedidoDeVentaListNew = tipoEntrega.getPedidoDeVentaList();
            List<String> illegalOrphanMessages = null;
            for (PedidoDeVenta pedidoDeVentaListOldPedidoDeVenta : pedidoDeVentaListOld) {
                if (!pedidoDeVentaListNew.contains(pedidoDeVentaListOldPedidoDeVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoDeVenta " + pedidoDeVentaListOldPedidoDeVenta + " since its tipoEntregaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PedidoDeVenta> attachedPedidoDeVentaListNew = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVentaToAttach : pedidoDeVentaListNew) {
                pedidoDeVentaListNewPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListNewPedidoDeVentaToAttach.getClass(), pedidoDeVentaListNewPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaListNew.add(pedidoDeVentaListNewPedidoDeVentaToAttach);
            }
            pedidoDeVentaListNew = attachedPedidoDeVentaListNew;
            tipoEntrega.setPedidoDeVentaList(pedidoDeVentaListNew);
            tipoEntrega = em.merge(tipoEntrega);
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVenta : pedidoDeVentaListNew) {
                if (!pedidoDeVentaListOld.contains(pedidoDeVentaListNewPedidoDeVenta)) {
                    TipoEntrega oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta = pedidoDeVentaListNewPedidoDeVenta.getTipoEntregaId();
                    pedidoDeVentaListNewPedidoDeVenta.setTipoEntregaId(tipoEntrega);
                    pedidoDeVentaListNewPedidoDeVenta = em.merge(pedidoDeVentaListNewPedidoDeVenta);
                    if (oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta != null && !oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta.equals(tipoEntrega)) {
                        oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListNewPedidoDeVenta);
                        oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta = em.merge(oldTipoEntregaIdOfPedidoDeVentaListNewPedidoDeVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoEntrega.getId();
                if (findTipoEntrega(id) == null) {
                    throw new NonexistentEntityException("The tipoEntrega with id " + id + " no longer exists.");
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
            TipoEntrega tipoEntrega;
            try {
                tipoEntrega = em.getReference(TipoEntrega.class, id);
                tipoEntrega.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEntrega with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoDeVenta> pedidoDeVentaListOrphanCheck = tipoEntrega.getPedidoDeVentaList();
            for (PedidoDeVenta pedidoDeVentaListOrphanCheckPedidoDeVenta : pedidoDeVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoEntrega (" + tipoEntrega + ") cannot be destroyed since the PedidoDeVenta " + pedidoDeVentaListOrphanCheckPedidoDeVenta + " in its pedidoDeVentaList field has a non-nullable tipoEntregaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoEntrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEntrega> findTipoEntregaEntities() {
        return findTipoEntregaEntities(true, -1, -1);
    }

    public List<TipoEntrega> findTipoEntregaEntities(int maxResults, int firstResult) {
        return findTipoEntregaEntities(false, maxResults, firstResult);
    }

    private List<TipoEntrega> findTipoEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEntrega.class));
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

    public TipoEntrega findTipoEntrega(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEntrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEntrega> rt = cq.from(TipoEntrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
