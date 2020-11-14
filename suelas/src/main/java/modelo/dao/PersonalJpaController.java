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
import modelo.dto.Rol;
import modelo.dto.Servicio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.PedidoDeVenta;
import modelo.dto.Personal;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author MANUEL
 */
public class PersonalJpaController implements Serializable {

    public PersonalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personal personal) {
        if (personal.getServicioList() == null) {
            personal.setServicioList(new ArrayList<Servicio>());
        }
        if (personal.getPedidoDeVentaList() == null) {
            personal.setPedidoDeVentaList(new ArrayList<PedidoDeVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rolId = personal.getRolId();
            if (rolId != null) {
                rolId = em.getReference(rolId.getClass(), rolId.getId());
                personal.setRolId(rolId);
            }
            List<Servicio> attachedServicioList = new ArrayList<Servicio>();
            for (Servicio servicioListServicioToAttach : personal.getServicioList()) {
                servicioListServicioToAttach = em.getReference(servicioListServicioToAttach.getClass(), servicioListServicioToAttach.getId());
                attachedServicioList.add(servicioListServicioToAttach);
            }
            personal.setServicioList(attachedServicioList);
            List<PedidoDeVenta> attachedPedidoDeVentaList = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVentaToAttach : personal.getPedidoDeVentaList()) {
                pedidoDeVentaListPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListPedidoDeVentaToAttach.getClass(), pedidoDeVentaListPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaList.add(pedidoDeVentaListPedidoDeVentaToAttach);
            }
            personal.setPedidoDeVentaList(attachedPedidoDeVentaList);
            em.persist(personal);
            if (rolId != null) {
                rolId.getPersonalList().add(personal);
                rolId = em.merge(rolId);
            }
            for (Servicio servicioListServicio : personal.getServicioList()) {
                servicioListServicio.getPersonalList().add(personal);
                servicioListServicio = em.merge(servicioListServicio);
            }
            for (PedidoDeVenta pedidoDeVentaListPedidoDeVenta : personal.getPedidoDeVentaList()) {
                Personal oldPersonalIdOfPedidoDeVentaListPedidoDeVenta = pedidoDeVentaListPedidoDeVenta.getPersonalId();
                pedidoDeVentaListPedidoDeVenta.setPersonalId(personal);
                pedidoDeVentaListPedidoDeVenta = em.merge(pedidoDeVentaListPedidoDeVenta);
                if (oldPersonalIdOfPedidoDeVentaListPedidoDeVenta != null) {
                    oldPersonalIdOfPedidoDeVentaListPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListPedidoDeVenta);
                    oldPersonalIdOfPedidoDeVentaListPedidoDeVenta = em.merge(oldPersonalIdOfPedidoDeVentaListPedidoDeVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personal personal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal persistentPersonal = em.find(Personal.class, personal.getId());
            Rol rolIdOld = persistentPersonal.getRolId();
            Rol rolIdNew = personal.getRolId();
            List<Servicio> servicioListOld = persistentPersonal.getServicioList();
            List<Servicio> servicioListNew = personal.getServicioList();
            List<PedidoDeVenta> pedidoDeVentaListOld = persistentPersonal.getPedidoDeVentaList();
            List<PedidoDeVenta> pedidoDeVentaListNew = personal.getPedidoDeVentaList();
            List<String> illegalOrphanMessages = null;
            for (PedidoDeVenta pedidoDeVentaListOldPedidoDeVenta : pedidoDeVentaListOld) {
                if (!pedidoDeVentaListNew.contains(pedidoDeVentaListOldPedidoDeVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoDeVenta " + pedidoDeVentaListOldPedidoDeVenta + " since its personalId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (rolIdNew != null) {
                rolIdNew = em.getReference(rolIdNew.getClass(), rolIdNew.getId());
                personal.setRolId(rolIdNew);
            }
            List<Servicio> attachedServicioListNew = new ArrayList<Servicio>();
            for (Servicio servicioListNewServicioToAttach : servicioListNew) {
                servicioListNewServicioToAttach = em.getReference(servicioListNewServicioToAttach.getClass(), servicioListNewServicioToAttach.getId());
                attachedServicioListNew.add(servicioListNewServicioToAttach);
            }
            servicioListNew = attachedServicioListNew;
            personal.setServicioList(servicioListNew);
            List<PedidoDeVenta> attachedPedidoDeVentaListNew = new ArrayList<PedidoDeVenta>();
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVentaToAttach : pedidoDeVentaListNew) {
                pedidoDeVentaListNewPedidoDeVentaToAttach = em.getReference(pedidoDeVentaListNewPedidoDeVentaToAttach.getClass(), pedidoDeVentaListNewPedidoDeVentaToAttach.getIdPedidoDeVenta());
                attachedPedidoDeVentaListNew.add(pedidoDeVentaListNewPedidoDeVentaToAttach);
            }
            pedidoDeVentaListNew = attachedPedidoDeVentaListNew;
            personal.setPedidoDeVentaList(pedidoDeVentaListNew);
            personal = em.merge(personal);
            if (rolIdOld != null && !rolIdOld.equals(rolIdNew)) {
                rolIdOld.getPersonalList().remove(personal);
                rolIdOld = em.merge(rolIdOld);
            }
            if (rolIdNew != null && !rolIdNew.equals(rolIdOld)) {
                rolIdNew.getPersonalList().add(personal);
                rolIdNew = em.merge(rolIdNew);
            }
            for (Servicio servicioListOldServicio : servicioListOld) {
                if (!servicioListNew.contains(servicioListOldServicio)) {
                    servicioListOldServicio.getPersonalList().remove(personal);
                    servicioListOldServicio = em.merge(servicioListOldServicio);
                }
            }
            for (Servicio servicioListNewServicio : servicioListNew) {
                if (!servicioListOld.contains(servicioListNewServicio)) {
                    servicioListNewServicio.getPersonalList().add(personal);
                    servicioListNewServicio = em.merge(servicioListNewServicio);
                }
            }
            for (PedidoDeVenta pedidoDeVentaListNewPedidoDeVenta : pedidoDeVentaListNew) {
                if (!pedidoDeVentaListOld.contains(pedidoDeVentaListNewPedidoDeVenta)) {
                    Personal oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta = pedidoDeVentaListNewPedidoDeVenta.getPersonalId();
                    pedidoDeVentaListNewPedidoDeVenta.setPersonalId(personal);
                    pedidoDeVentaListNewPedidoDeVenta = em.merge(pedidoDeVentaListNewPedidoDeVenta);
                    if (oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta != null && !oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta.equals(personal)) {
                        oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta.getPedidoDeVentaList().remove(pedidoDeVentaListNewPedidoDeVenta);
                        oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta = em.merge(oldPersonalIdOfPedidoDeVentaListNewPedidoDeVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personal.getId();
                if (findPersonal(id) == null) {
                    throw new NonexistentEntityException("The personal with id " + id + " no longer exists.");
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
            Personal personal;
            try {
                personal = em.getReference(Personal.class, id);
                personal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PedidoDeVenta> pedidoDeVentaListOrphanCheck = personal.getPedidoDeVentaList();
            for (PedidoDeVenta pedidoDeVentaListOrphanCheckPedidoDeVenta : pedidoDeVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personal (" + personal + ") cannot be destroyed since the PedidoDeVenta " + pedidoDeVentaListOrphanCheckPedidoDeVenta + " in its pedidoDeVentaList field has a non-nullable personalId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Rol rolId = personal.getRolId();
            if (rolId != null) {
                rolId.getPersonalList().remove(personal);
                rolId = em.merge(rolId);
            }
            List<Servicio> servicioList = personal.getServicioList();
            for (Servicio servicioListServicio : servicioList) {
                servicioListServicio.getPersonalList().remove(personal);
                servicioListServicio = em.merge(servicioListServicio);
            }
            em.remove(personal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personal> findPersonalEntities() {
        return findPersonalEntities(true, -1, -1);
    }

    public List<Personal> findPersonalEntities(int maxResults, int firstResult) {
        return findPersonalEntities(false, maxResults, firstResult);
    }

    private List<Personal> findPersonalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personal.class));
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

    public Personal findPersonal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personal.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personal> rt = cq.from(Personal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
