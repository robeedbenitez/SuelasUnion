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
import modelo.dto.TipoCliente;
import modelo.dto.PedidoDeVenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Cliente;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getPedidoDeVentaList() == null) {
            cliente.setPedidoDeVentaList(new ArrayList<PedidoDeVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCliente tipoClienteIdTipoCliente = cliente.getTipoClienteIdTipoCliente();
            if (tipoClienteIdTipoCliente != null) {
                tipoClienteIdTipoCliente = em.getReference(tipoClienteIdTipoCliente.getClass(), tipoClienteIdTipoCliente.getIdTipoCliente());
                cliente.setTipoClienteIdTipoCliente(tipoClienteIdTipoCliente);
            }
            List<PedidoDeVenta> attachedPedidoDeVentaList = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVentaToAttach : cliente.getPedidoDeVentaList()) {
                pedidoDeVentaListPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListPedidoDeVentaToAttach.getClass(), pedidoDeVentaListPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaList.add(pedidoDeVentaListPedidoDeVentaToAttach);
            }
            cliente.setPedidoDeVentaList(attachedPedidoDeVentaList);
            em.persist(cliente);
            if (tipoClienteIdTipoCliente != null) {
                tipoClienteIdTipoCliente.getClienteList().add(cliente);
                tipoClienteIdTipoCliente = em.merge(tipoClienteIdTipoCliente);
            }
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVenta : cliente.getPedidoDeVentaList()) {
                Cliente oldClienteCedulaOfPedidoDeVentaListPedidoDeVenta = pedidoDeVentaListPedidoDeVenta.getClienteCedula();
                pedidoDeVentaListPedidoDeVenta.setClienteCedula(cliente);
                pedidoDeVentaListPedidoDeVenta = em.merge(pedidoDeVentaListPedidoDeVenta);
                if (oldClienteCedulaOfPedidoDeVentaListPedidoDeVenta != null) {
                    oldClienteCedulaOfPedidoDeVentaListPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListPedidoDeVenta);
                    oldClienteCedulaOfPedidoDeVentaListPedidoDeVenta = em.merge(oldClienteCedulaOfPedidoDeVentaListPedidoDeVenta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getCedula()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCedula());
            TipoCliente tipoClienteIdTipoClienteOld = persistentCliente.getTipoClienteIdTipoCliente();
            TipoCliente tipoClienteIdTipoClienteNew = cliente.getTipoClienteIdTipoCliente();
            List<PedidoDeVenta> pedidoDeVentaListOld = persistentCliente.getPedidoDeVentaList();
            List<PedidoDeVenta> pedidoDeVentaListNew = cliente.getPedidoDeVentaList();
            if (tipoClienteIdTipoClienteNew != null) {
                tipoClienteIdTipoClienteNew = em.getReference(tipoClienteIdTipoClienteNew.getClass(), tipoClienteIdTipoClienteNew.getIdTipoCliente());
                cliente.setTipoClienteIdTipoCliente(tipoClienteIdTipoClienteNew);
            }
            List<PedidoDeVenta> attachedPedidoDeVentaListNew = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVentaToAttach : pedidoDeVentaListNew) {
                pedidoDeVentaListNewPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListNewPedidoDeVentaToAttach.getClass(), pedidoDeVentaListNewPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaListNew.add(pedidoDeVentaListNewPedidoDeVentaToAttach);
            }
            pedidoDeVentaListNew = attachedPedidoDeVentaListNew;
            cliente.setPedidoDeVentaList(pedidoDeVentaListNew);
            cliente = em.merge(cliente);
            if (tipoClienteIdTipoClienteOld != null && !tipoClienteIdTipoClienteOld.equals(tipoClienteIdTipoClienteNew)) {
                tipoClienteIdTipoClienteOld.getClienteList().remove(cliente);
                tipoClienteIdTipoClienteOld = em.merge(tipoClienteIdTipoClienteOld);
            }
            if (tipoClienteIdTipoClienteNew != null && !tipoClienteIdTipoClienteNew.equals(tipoClienteIdTipoClienteOld)) {
                tipoClienteIdTipoClienteNew.getClienteList().add(cliente);
                tipoClienteIdTipoClienteNew = em.merge(tipoClienteIdTipoClienteNew);
            }
            for (PedidoDeVenta pedidoDeVentaListOldPedidoDeVenta : pedidoDeVentaListOld) {
                if (!pedidoDeVentaListNew.contains(pedidoDeVentaListOldPedidoDeVenta)) {
                    pedidoDeVentaListOldPedidoDeVenta.setClienteCedula(null);
                    pedidoDeVentaListOldPedidoDeVenta = em.merge(pedidoDeVentaListOldPedidoDeVenta);
                }
            }
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVenta : pedidoDeVentaListNew) {
                if (!pedidoDeVentaListOld.contains(pedidoDeVentaListNewPedidoDeVenta)) {
                    Cliente oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta = pedidoDeVentaListNewPedidoDeVenta.getClienteCedula();
                    pedidoDeVentaListNewPedidoDeVenta.setClienteCedula(cliente);
                    pedidoDeVentaListNewPedidoDeVenta = em.merge(pedidoDeVentaListNewPedidoDeVenta);
                    if (oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta != null && !oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta.equals(cliente)) {
                        oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListNewPedidoDeVenta);
                        oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta = em.merge(oldClienteCedulaOfPedidoDeVentaListNewPedidoDeVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cliente.getCedula();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            TipoCliente tipoClienteIdTipoCliente = cliente.getTipoClienteIdTipoCliente();
            if (tipoClienteIdTipoCliente != null) {
                tipoClienteIdTipoCliente.getClienteList().remove(cliente);
                tipoClienteIdTipoCliente = em.merge(tipoClienteIdTipoCliente);
            }
            List<PedidoDeVenta> pedidoDeVentaList = cliente.getPedidoDeVentaList();
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVenta : pedidoDeVentaList) {
                pedidoDeVentaListPedidoDeVenta.setClienteCedula(null);
                pedidoDeVentaListPedidoDeVenta = em.merge(pedidoDeVentaListPedidoDeVenta);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
