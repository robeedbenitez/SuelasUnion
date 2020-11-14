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
import modelo.dto.Color;
import modelo.dto.Producto;
import modelo.dto.Tipo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.Suela;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;
import modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author MANUEL
 */
public class SuelaJpaController implements Serializable {

    public SuelaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Suela suela) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Producto productoOrphanCheck = suela.getProducto();
        if (productoOrphanCheck != null) {
            Suela oldSuelaOfProducto = productoOrphanCheck.getSuela();
            if (oldSuelaOfProducto != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Producto " + productoOrphanCheck + " already has an item of type Suela whose producto column cannot be null. Please make another selection for the producto field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Color colorId = suela.getColorId();
            if (colorId != null) {
                colorId = em.getReference(colorId.getClass(), colorId.getId());
                suela.setColorId(colorId);
            }
            Producto producto = suela.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getId());
                suela.setProducto(producto);
            }
            Tipo tipoId = suela.getTipoId();
            if (tipoId != null) {
                tipoId = em.getReference(tipoId.getClass(), tipoId.getId());
                suela.setTipoId(tipoId);
            }
            em.persist(suela);
            if (colorId != null) {
                colorId.getSuelaList().add(suela);
                colorId = em.merge(colorId);
            }
            if (producto != null) {
                producto.setSuela(suela);
                producto = em.merge(producto);
            }
            if (tipoId != null) {
                tipoId.getSuelaList().add(suela);
                tipoId = em.merge(tipoId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSuela(suela.getProductoId()) != null) {
                throw new PreexistingEntityException("Suela " + suela + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Suela suela) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Suela persistentSuela = em.find(Suela.class, suela.getProductoId());
            Color colorIdOld = persistentSuela.getColorId();
            Color colorIdNew = suela.getColorId();
            Producto productoOld = persistentSuela.getProducto();
            Producto productoNew = suela.getProducto();
            Tipo tipoIdOld = persistentSuela.getTipoId();
            Tipo tipoIdNew = suela.getTipoId();
            List<String> illegalOrphanMessages = null;
            if (productoNew != null && !productoNew.equals(productoOld)) {
                Suela oldSuelaOfProducto = productoNew.getSuela();
                if (oldSuelaOfProducto != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Producto " + productoNew + " already has an item of type Suela whose producto column cannot be null. Please make another selection for the producto field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (colorIdNew != null) {
                colorIdNew = em.getReference(colorIdNew.getClass(), colorIdNew.getId());
                suela.setColorId(colorIdNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getId());
                suela.setProducto(productoNew);
            }
            if (tipoIdNew != null) {
                tipoIdNew = em.getReference(tipoIdNew.getClass(), tipoIdNew.getId());
                suela.setTipoId(tipoIdNew);
            }
            suela = em.merge(suela);
            if (colorIdOld != null && !colorIdOld.equals(colorIdNew)) {
                colorIdOld.getSuelaList().remove(suela);
                colorIdOld = em.merge(colorIdOld);
            }
            if (colorIdNew != null && !colorIdNew.equals(colorIdOld)) {
                colorIdNew.getSuelaList().add(suela);
                colorIdNew = em.merge(colorIdNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.setSuela(null);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.setSuela(suela);
                productoNew = em.merge(productoNew);
            }
            if (tipoIdOld != null && !tipoIdOld.equals(tipoIdNew)) {
                tipoIdOld.getSuelaList().remove(suela);
                tipoIdOld = em.merge(tipoIdOld);
            }
            if (tipoIdNew != null && !tipoIdNew.equals(tipoIdOld)) {
                tipoIdNew.getSuelaList().add(suela);
                tipoIdNew = em.merge(tipoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = suela.getProductoId();
                if (findSuela(id) == null) {
                    throw new NonexistentEntityException("The suela with id " + id + " no longer exists.");
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
            Suela suela;
            try {
                suela = em.getReference(Suela.class, id);
                suela.getProductoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The suela with id " + id + " no longer exists.", enfe);
            }
            Color colorId = suela.getColorId();
            if (colorId != null) {
                colorId.getSuelaList().remove(suela);
                colorId = em.merge(colorId);
            }
            Producto producto = suela.getProducto();
            if (producto != null) {
                producto.setSuela(null);
                producto = em.merge(producto);
            }
            Tipo tipoId = suela.getTipoId();
            if (tipoId != null) {
                tipoId.getSuelaList().remove(suela);
                tipoId = em.merge(tipoId);
            }
            em.remove(suela);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Suela> findSuelaEntities() {
        return findSuelaEntities(true, -1, -1);
    }

    public List<Suela> findSuelaEntities(int maxResults, int firstResult) {
        return findSuelaEntities(false, maxResults, firstResult);
    }

    private List<Suela> findSuelaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Suela.class));
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

    public Suela findSuela(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Suela.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuelaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Suela> rt = cq.from(Suela.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
