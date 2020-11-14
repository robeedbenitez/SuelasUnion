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
import modelo.dto.Estado;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) throws PreexistingEntityException, Exception {
        if (estado.getPedidoDeVentaList() == null) {
            estado.setPedidoDeVentaList(new ArrayList<PedidoDeVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PedidoDeVenta> attachedPedidoDeVentaList = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVentaToAttach : estado.getPedidoDeVentaList()) {
                pedidoDeVentaListPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListPedidoDeVentaToAttach.getClass(), pedidoDeVentaListPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaList.add(pedidoDeVentaListPedidoDeVentaToAttach);
            }
            estado.setPedidoDeVentaList(attachedPedidoDeVentaList);
            em.persist(estado);
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVenta : estado.getPedidoDeVentaList()) {
                Estado oldEstadoIdOfPedidoDeVentaListPedidoDeVenta = pedidoDeVentaListPedidoDeVenta.getEstadoId();
                pedidoDeVentaListPedidoDeVenta.setEstadoId(estado);
                pedidoDeVentaListPedidoDeVenta = em.merge(pedidoDeVentaListPedidoDeVenta);
                if (oldEstadoIdOfPedidoDeVentaListPedidoDeVenta != null) {
                    oldEstadoIdOfPedidoDeVentaListPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListPedidoDeVenta);
                    oldEstadoIdOfPedidoDeVentaListPedidoDeVenta = em.merge(oldEstadoIdOfPedidoDeVentaListPedidoDeVenta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstado(estado.getId()) != null) {
                throw new PreexistingEntityException("Estado " + estado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            List<PedidoDeVenta> pedidoDeVentaListOld = persistentEstado.getPedidoDeVentaList();
            List<PedidoDeVenta> pedidoDeVentaListNew = estado.getPedidoDeVentaList();
            List<String> illegalOrphanMessages = null;
            for (PedidoDeVenta pedidoDeVentaListOldPedidoDeVenta : pedidoDeVentaListOld) {
                if (!pedidoDeVentaListNew.contains(pedidoDeVentaListOldPedidoDeVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoDeVenta " + pedidoDeVentaListOldPedidoDeVenta + " since its estadoId field is not nullable.");
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
            estado.setPedidoDeVentaList(pedidoDeVentaListNew);
            estado = em.merge(estado);
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVenta : pedidoDeVentaListNew) {
                if (!pedidoDeVentaListOld.contains(pedidoDeVentaListNewPedidoDeVenta)) {
                    Estado oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta = pedidoDeVentaListNewPedidoDeVenta.getEstadoId();
                    pedidoDeVentaListNewPedidoDeVenta.setEstadoId(estado);
                    pedidoDeVentaListNewPedidoDeVenta = em.merge(pedidoDeVentaListNewPedidoDeVenta);
                    if (oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta != null && !oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta.equals(estado)) {
                        oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListNewPedidoDeVenta);
                        oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta = em.merge(oldEstadoIdOfPedidoDeVentaListNewPedidoDeVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoDeVenta> pedidoDeVentaListOrphanCheck = estado.getPedidoDeVentaList();
            for (PedidoDeVenta pedidoDeVentaListOrphanCheckPedidoDeVenta : pedidoDeVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the PedidoDeVenta " + pedidoDeVentaListOrphanCheckPedidoDeVenta + " in its pedidoDeVentaList field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
