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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.TipoCliente;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author MANUEL
 */
public class TipoClienteJpaController implements Serializable {

    public TipoClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoCliente tipoCliente) {
        if (tipoCliente.getClienteList() == null) {
            tipoCliente.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : tipoCliente.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCedula());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            tipoCliente.setClienteList(attachedClienteList);
            em.persist(tipoCliente);
            for (Cliente clienteListCliente : tipoCliente.getClienteList()) {
                TipoCliente oldTipoClienteIdTipoClienteOfClienteListCliente = clienteListCliente.getTipoClienteIdTipoCliente();
                clienteListCliente.setTipoClienteIdTipoCliente(tipoCliente);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldTipoClienteIdTipoClienteOfClienteListCliente != null) {
                    oldTipoClienteIdTipoClienteOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldTipoClienteIdTipoClienteOfClienteListCliente = em.merge(oldTipoClienteIdTipoClienteOfClienteListCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoCliente tipoCliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoCliente persistentTipoCliente = em.find(TipoCliente.class, tipoCliente.getIdTipoCliente());
            List<Cliente> clienteListOld = persistentTipoCliente.getClienteList();
            List<Cliente> clienteListNew = tipoCliente.getClienteList();
            List<String> illegalOrphanMessages = null;
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its tipoClienteIdTipoCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCedula());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            tipoCliente.setClienteList(clienteListNew);
            tipoCliente = em.merge(tipoCliente);
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    TipoCliente oldTipoClienteIdTipoClienteOfClienteListNewCliente = clienteListNewCliente.getTipoClienteIdTipoCliente();
                    clienteListNewCliente.setTipoClienteIdTipoCliente(tipoCliente);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldTipoClienteIdTipoClienteOfClienteListNewCliente != null && !oldTipoClienteIdTipoClienteOfClienteListNewCliente.equals(tipoCliente)) {
                        oldTipoClienteIdTipoClienteOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldTipoClienteIdTipoClienteOfClienteListNewCliente = em.merge(oldTipoClienteIdTipoClienteOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoCliente.getIdTipoCliente();
                if (findTipoCliente(id) == null) {
                    throw new NonexistentEntityException("The tipoCliente with id " + id + " no longer exists.");
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
            TipoCliente tipoCliente;
            try {
                tipoCliente = em.getReference(TipoCliente.class, id);
                tipoCliente.getIdTipoCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoCliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cliente> clienteListOrphanCheck = tipoCliente.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoCliente (" + tipoCliente + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable tipoClienteIdTipoCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoCliente> findTipoClienteEntities() {
        return findTipoClienteEntities(true, -1, -1);
    }

    public List<TipoCliente> findTipoClienteEntities(int maxResults, int firstResult) {
        return findTipoClienteEntities(false, maxResults, firstResult);
    }

    private List<TipoCliente> findTipoClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoCliente.class));
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

    public TipoCliente findTipoCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoCliente> rt = cq.from(TipoCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
