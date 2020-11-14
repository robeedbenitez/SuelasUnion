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
import modelo.dto.Cliente;
import modelo.dto.Estado;
import modelo.dto.Personal;
import modelo.dto.TipoEntrega;
import modelo.dto.DevolucionCliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.PedidoDeVenta;
import modelo.dto.ProductoPedidoVenta;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author MANUEL
 */
public class PedidoDeVentaJpaController implements Serializable {

    public PedidoDeVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PedidoDeVenta pedidoDeVenta) {
        if (pedidoDeVenta.getDevolucionClienteList() == null) {
            pedidoDeVenta.setDevolucionClienteList(new ArrayList<DevolucionCliente>());
        }
        if (pedidoDeVenta.getProductoPedidoVentaList() == null) {
            pedidoDeVenta.setProductoPedidoVentaList(new ArrayList<ProductoPedidoVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente clienteCedula = pedidoDeVenta.getClienteCedula();
            if (clienteCedula != null) {
                clienteCedula = em.getReference(clienteCedula.getClass(), clienteCedula.getCedula());
                pedidoDeVenta.setClienteCedula(clienteCedula);
            }
            Estado estadoId = pedidoDeVenta.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                pedidoDeVenta.setEstadoId(estadoId);
            }
            Personal personalId = pedidoDeVenta.getPersonalId();
            if (personalId != null) {
                personalId = em.getReference(personalId.getClass(), personalId.getId());
                pedidoDeVenta.setPersonalId(personalId);
            }
            TipoEntrega tipoEntregaId = pedidoDeVenta.getTipoEntregaId();
            if (tipoEntregaId != null) {
                tipoEntregaId = em.getReference(tipoEntregaId.getClass(), tipoEntregaId.getId());
                pedidoDeVenta.setTipoEntregaId(tipoEntregaId);
            }
            List<DevolucionCliente> attachedDevolucionClienteList = new ArrayList<DevolucionCliente>();
            for (DevolucionCliente devolucionClienteListDevolucionClienteToAttach : pedidoDeVenta.getDevolucionClienteList()) {
                devolucionClienteListDevolucionClienteToAttach = em.getReference(devolucionClienteListDevolucionClienteToAttach.getClass(), devolucionClienteListDevolucionClienteToAttach.getId());
                attachedDevolucionClienteList.add(devolucionClienteListDevolucionClienteToAttach);
            }
            pedidoDeVenta.setDevolucionClienteList(attachedDevolucionClienteList);
            List<ProductoPedidoVenta> attachedProductoPedidoVentaList = new ArrayList<ProductoPedidoVenta>();
            for (ProductoPedidoVenta productoPedidoVentaListProductoPedidoVentaToAttach : pedidoDeVenta.getProductoPedidoVentaList()) {
                productoPedidoVentaListProductoPedidoVentaToAttach = em.getReference(productoPedidoVentaListProductoPedidoVentaToAttach.getClass(), productoPedidoVentaListProductoPedidoVentaToAttach.getIdProductoCliente());
                attachedProductoPedidoVentaList.add(productoPedidoVentaListProductoPedidoVentaToAttach);
            }
            pedidoDeVenta.setProductoPedidoVentaList(attachedProductoPedidoVentaList);
            em.persist(pedidoDeVenta);
            if (clienteCedula != null) {
                clienteCedula.getPedidoDeVentaList().add(pedidoDeVenta);
                clienteCedula = em.merge(clienteCedula);
            }
            if (estadoId != null) {
                estadoId.getPedidoDeVentaList().add(pedidoDeVenta);
                estadoId = em.merge(estadoId);
            }
            if (personalId != null) {
                personalId.getPedidoDeVentaList().add(pedidoDeVenta);
                personalId = em.merge(personalId);
            }
            if (tipoEntregaId != null) {
                tipoEntregaId.getPedidoDeVentaList().add(pedidoDeVenta);
                tipoEntregaId = em.merge(tipoEntregaId);
            }
            for (DevolucionCliente devolucionClienteListDevolucionCliente : pedidoDeVenta.getDevolucionClienteList()) {
                PedidoDeVenta oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListDevolucionCliente = devolucionClienteListDevolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
                devolucionClienteListDevolucionCliente.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVenta);
                devolucionClienteListDevolucionCliente = em.merge(devolucionClienteListDevolucionCliente);
                if (oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListDevolucionCliente != null) {
                    oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListDevolucionCliente.getDevolucionClienteList().remove(devolucionClienteListDevolucionCliente);
                    oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListDevolucionCliente = em.merge(oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListDevolucionCliente);
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListProductoPedidoVenta : pedidoDeVenta.getProductoPedidoVentaList()) {
                PedidoDeVenta oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListProductoPedidoVenta = productoPedidoVentaListProductoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
                productoPedidoVentaListProductoPedidoVenta.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVenta);
                productoPedidoVentaListProductoPedidoVenta = em.merge(productoPedidoVentaListProductoPedidoVenta);
                if (oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListProductoPedidoVenta != null) {
                    oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListProductoPedidoVenta.getProductoPedidoVentaList().remove(productoPedidoVentaListProductoPedidoVenta);
                    oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListProductoPedidoVenta = em.merge(oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListProductoPedidoVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PedidoDeVenta pedidoDeVenta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoDeVenta persistentPedidoDeVenta = em.find(PedidoDeVenta.class, pedidoDeVenta.getIdPedidoDeVenta());
            Cliente clienteCedulaOld = persistentPedidoDeVenta.getClienteCedula();
            Cliente clienteCedulaNew = pedidoDeVenta.getClienteCedula();
            Estado estadoIdOld = persistentPedidoDeVenta.getEstadoId();
            Estado estadoIdNew = pedidoDeVenta.getEstadoId();
            Personal personalIdOld = persistentPedidoDeVenta.getPersonalId();
            Personal personalIdNew = pedidoDeVenta.getPersonalId();
            TipoEntrega tipoEntregaIdOld = persistentPedidoDeVenta.getTipoEntregaId();
            TipoEntrega tipoEntregaIdNew = pedidoDeVenta.getTipoEntregaId();
            List<DevolucionCliente> devolucionClienteListOld = persistentPedidoDeVenta.getDevolucionClienteList();
            List<DevolucionCliente> devolucionClienteListNew = pedidoDeVenta.getDevolucionClienteList();
            List<ProductoPedidoVenta> productoPedidoVentaListOld = persistentPedidoDeVenta.getProductoPedidoVentaList();
            List<ProductoPedidoVenta> productoPedidoVentaListNew = pedidoDeVenta.getProductoPedidoVentaList();
            List<String> illegalOrphanMessages = null;
            for (DevolucionCliente devolucionClienteListOldDevolucionCliente : devolucionClienteListOld) {
                if (!devolucionClienteListNew.contains(devolucionClienteListOldDevolucionCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionCliente " + devolucionClienteListOldDevolucionCliente + " since its pedidoDeVentaIdPedidoDeVenta field is not nullable.");
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListOldProductoPedidoVenta : productoPedidoVentaListOld) {
                if (!productoPedidoVentaListNew.contains(productoPedidoVentaListOldProductoPedidoVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoPedidoVenta " + productoPedidoVentaListOldProductoPedidoVenta + " since its pedidoDeVentaIdPedidoDeVenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteCedulaNew != null) {
                clienteCedulaNew = em.getReference(clienteCedulaNew.getClass(), clienteCedulaNew.getCedula());
                pedidoDeVenta.setClienteCedula(clienteCedulaNew);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                pedidoDeVenta.setEstadoId(estadoIdNew);
            }
            if (personalIdNew != null) {
                personalIdNew = em.getReference(personalIdNew.getClass(), personalIdNew.getId());
                pedidoDeVenta.setPersonalId(personalIdNew);
            }
            if (tipoEntregaIdNew != null) {
                tipoEntregaIdNew = em.getReference(tipoEntregaIdNew.getClass(), tipoEntregaIdNew.getId());
                pedidoDeVenta.setTipoEntregaId(tipoEntregaIdNew);
            }
            List<DevolucionCliente> attachedDevolucionClienteListNew = new ArrayList<DevolucionCliente>();
            for (DevolucionCliente devolucionClienteListNewDevolucionClienteToAttach : devolucionClienteListNew) {
                devolucionClienteListNewDevolucionClienteToAttach = em.getReference(devolucionClienteListNewDevolucionClienteToAttach.getClass(), devolucionClienteListNewDevolucionClienteToAttach.getId());
                attachedDevolucionClienteListNew.add(devolucionClienteListNewDevolucionClienteToAttach);
            }
            devolucionClienteListNew = attachedDevolucionClienteListNew;
            pedidoDeVenta.setDevolucionClienteList(devolucionClienteListNew);
            List<ProductoPedidoVenta> attachedProductoPedidoVentaListNew = new ArrayList<ProductoPedidoVenta>();
            for (ProductoPedidoVenta productoPedidoVentaListNewProductoPedidoVentaToAttach : productoPedidoVentaListNew) {
                productoPedidoVentaListNewProductoPedidoVentaToAttach = em.getReference(productoPedidoVentaListNewProductoPedidoVentaToAttach.getClass(), productoPedidoVentaListNewProductoPedidoVentaToAttach.getIdProductoCliente());
                attachedProductoPedidoVentaListNew.add(productoPedidoVentaListNewProductoPedidoVentaToAttach);
            }
            productoPedidoVentaListNew = attachedProductoPedidoVentaListNew;
            pedidoDeVenta.setProductoPedidoVentaList(productoPedidoVentaListNew);
            pedidoDeVenta = em.merge(pedidoDeVenta);
            if (clienteCedulaOld != null && !clienteCedulaOld.equals(clienteCedulaNew)) {
                clienteCedulaOld.getPedidoDeVentaList().remove(pedidoDeVenta);
                clienteCedulaOld = em.merge(clienteCedulaOld);
            }
            if (clienteCedulaNew != null && !clienteCedulaNew.equals(clienteCedulaOld)) {
                clienteCedulaNew.getPedidoDeVentaList().add(pedidoDeVenta);
                clienteCedulaNew = em.merge(clienteCedulaNew);
            }
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getPedidoDeVentaList().remove(pedidoDeVenta);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getPedidoDeVentaList().add(pedidoDeVenta);
                estadoIdNew = em.merge(estadoIdNew);
            }
            if (personalIdOld != null && !personalIdOld.equals(personalIdNew)) {
                personalIdOld.getPedidoDeVentaList().remove(pedidoDeVenta);
                personalIdOld = em.merge(personalIdOld);
            }
            if (personalIdNew != null && !personalIdNew.equals(personalIdOld)) {
                personalIdNew.getPedidoDeVentaList().add(pedidoDeVenta);
                personalIdNew = em.merge(personalIdNew);
            }
            if (tipoEntregaIdOld != null && !tipoEntregaIdOld.equals(tipoEntregaIdNew)) {
                tipoEntregaIdOld.getPedidoDeVentaList().remove(pedidoDeVenta);
                tipoEntregaIdOld = em.merge(tipoEntregaIdOld);
            }
            if (tipoEntregaIdNew != null && !tipoEntregaIdNew.equals(tipoEntregaIdOld)) {
                tipoEntregaIdNew.getPedidoDeVentaList().add(pedidoDeVenta);
                tipoEntregaIdNew = em.merge(tipoEntregaIdNew);
            }
            for (DevolucionCliente devolucionClienteListNewDevolucionCliente : devolucionClienteListNew) {
                if (!devolucionClienteListOld.contains(devolucionClienteListNewDevolucionCliente)) {
                    PedidoDeVenta oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente = devolucionClienteListNewDevolucionCliente.getPedidoDeVentaIdPedidoDeVenta();
                    devolucionClienteListNewDevolucionCliente.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVenta);
                    devolucionClienteListNewDevolucionCliente = em.merge(devolucionClienteListNewDevolucionCliente);
                    if (oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente != null && !oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente.equals(pedidoDeVenta)) {
                        oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente.getDevolucionClienteList().remove(devolucionClienteListNewDevolucionCliente);
                        oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente = em.merge(oldPedidoDeVentaIdPedidoDeVentaOfDevolucionClienteListNewDevolucionCliente);
                    }
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListNewProductoPedidoVenta : productoPedidoVentaListNew) {
                if (!productoPedidoVentaListOld.contains(productoPedidoVentaListNewProductoPedidoVenta)) {
                    PedidoDeVenta oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta = productoPedidoVentaListNewProductoPedidoVenta.getPedidoDeVentaIdPedidoDeVenta();
                    productoPedidoVentaListNewProductoPedidoVenta.setPedidoDeVentaIdPedidoDeVenta(pedidoDeVenta);
                    productoPedidoVentaListNewProductoPedidoVenta = em.merge(productoPedidoVentaListNewProductoPedidoVenta);
                    if (oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta != null && !oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta.equals(pedidoDeVenta)) {
                        oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta.getProductoPedidoVentaList().remove(productoPedidoVentaListNewProductoPedidoVenta);
                        oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta = em.merge(oldPedidoDeVentaIdPedidoDeVentaOfProductoPedidoVentaListNewProductoPedidoVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidoDeVenta.getIdPedidoDeVenta();
                if (findPedidoDeVenta(id) == null) {
                    throw new NonexistentEntityException("The pedidoDeVenta with id " + id + " no longer exists.");
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
            PedidoDeVenta pedidoDeVenta;
            try {
                pedidoDeVenta = em.getReference(PedidoDeVenta.class, id);
                pedidoDeVenta.getIdPedidoDeVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoDeVenta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DevolucionCliente> devolucionClienteListOrphanCheck = pedidoDeVenta.getDevolucionClienteList();
            for (DevolucionCliente devolucionClienteListOrphanCheckDevolucionCliente : devolucionClienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PedidoDeVenta (" + pedidoDeVenta + ") cannot be destroyed since the DevolucionCliente " + devolucionClienteListOrphanCheckDevolucionCliente + " in its devolucionClienteList field has a non-nullable pedidoDeVentaIdPedidoDeVenta field.");
            }
            List<ProductoPedidoVenta> productoPedidoVentaListOrphanCheck = pedidoDeVenta.getProductoPedidoVentaList();
            for (ProductoPedidoVenta productoPedidoVentaListOrphanCheckProductoPedidoVenta : productoPedidoVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PedidoDeVenta (" + pedidoDeVenta + ") cannot be destroyed since the ProductoPedidoVenta " + productoPedidoVentaListOrphanCheckProductoPedidoVenta + " in its productoPedidoVentaList field has a non-nullable pedidoDeVentaIdPedidoDeVenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clienteCedula = pedidoDeVenta.getClienteCedula();
            if (clienteCedula != null) {
                clienteCedula.getPedidoDeVentaList().remove(pedidoDeVenta);
                clienteCedula = em.merge(clienteCedula);
            }
            Estado estadoId = pedidoDeVenta.getEstadoId();
            if (estadoId != null) {
                estadoId.getPedidoDeVentaList().remove(pedidoDeVenta);
                estadoId = em.merge(estadoId);
            }
            Personal personalId = pedidoDeVenta.getPersonalId();
            if (personalId != null) {
                personalId.getPedidoDeVentaList().remove(pedidoDeVenta);
                personalId = em.merge(personalId);
            }
            TipoEntrega tipoEntregaId = pedidoDeVenta.getTipoEntregaId();
            if (tipoEntregaId != null) {
                tipoEntregaId.getPedidoDeVentaList().remove(pedidoDeVenta);
                tipoEntregaId = em.merge(tipoEntregaId);
            }
            em.remove(pedidoDeVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PedidoDeVenta> findPedidoDeVentaEntities() {
        return findPedidoDeVentaEntities(true, -1, -1);
    }

    public List<PedidoDeVenta> findPedidoDeVentaEntities(int maxResults, int firstResult) {
        return findPedidoDeVentaEntities(false, maxResults, firstResult);
    }

    private List<PedidoDeVenta> findPedidoDeVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PedidoDeVenta.class));
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

    public PedidoDeVenta findPedidoDeVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PedidoDeVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoDeVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PedidoDeVenta> rt = cq.from(PedidoDeVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
