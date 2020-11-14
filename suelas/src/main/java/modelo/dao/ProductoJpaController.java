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
import modelo.dto.Salpa;
import modelo.dto.Tira;
import modelo.dto.Suela;
import modelo.dto.Plantilla;
import modelo.dto.DevolucionProveedorProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.dto.ProductoPedidoProveedor;
import modelo.dto.ProductoPedidoVenta;
import modelo.dto.DevolucionClienteProducto;
import modelo.dto.Producto;
import modelo.exceptions.IllegalOrphanException;
import modelo.exceptions.NonexistentEntityException;

/**
 *
 * @author MANUEL
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getDevolucionProveedorProductoList() == null) {
            producto.setDevolucionProveedorProductoList(new ArrayList<DevolucionProveedorProducto>());
        }
        if (producto.getProductoPedidoProveedorList() == null) {
            producto.setProductoPedidoProveedorList(new ArrayList<ProductoPedidoProveedor>());
        }
        if (producto.getProductoPedidoVentaList() == null) {
            producto.setProductoPedidoVentaList(new ArrayList<ProductoPedidoVenta>());
        }
        if (producto.getDevolucionClienteProductoList() == null) {
            producto.setDevolucionClienteProductoList(new ArrayList<DevolucionClienteProducto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salpa salpa = producto.getSalpa();
            if (salpa != null) {
                salpa = em.getReference(salpa.getClass(), salpa.getProductoId());
                producto.setSalpa(salpa);
            }
            Tira tira = producto.getTira();
            if (tira != null) {
                tira = em.getReference(tira.getClass(), tira.getProductoId());
                producto.setTira(tira);
            }
            Suela suela = producto.getSuela();
            if (suela != null) {
                suela = em.getReference(suela.getClass(), suela.getProductoId());
                producto.setSuela(suela);
            }
            Plantilla plantilla = producto.getPlantilla();
            if (plantilla != null) {
                plantilla = em.getReference(plantilla.getClass(), plantilla.getProductoId());
                producto.setPlantilla(plantilla);
            }
            List<DevolucionProveedorProducto> attachedDevolucionProveedorProductoList = new ArrayList<DevolucionProveedorProducto>();
            for (DevolucionProveedorProducto devolucionProveedorProductoListDevolucionProveedorProductoToAttach : producto.getDevolucionProveedorProductoList()) {
                devolucionProveedorProductoListDevolucionProveedorProductoToAttach = em.getReference(devolucionProveedorProductoListDevolucionProveedorProductoToAttach.getClass(), devolucionProveedorProductoListDevolucionProveedorProductoToAttach.getDevolucionProveedorProductoPK());
                attachedDevolucionProveedorProductoList.add(devolucionProveedorProductoListDevolucionProveedorProductoToAttach);
            }
            producto.setDevolucionProveedorProductoList(attachedDevolucionProveedorProductoList);
            List<ProductoPedidoProveedor> attachedProductoPedidoProveedorList = new ArrayList<ProductoPedidoProveedor>();
            for (ProductoPedidoProveedor productoPedidoProveedorListProductoPedidoProveedorToAttach : producto.getProductoPedidoProveedorList()) {
                productoPedidoProveedorListProductoPedidoProveedorToAttach = em.getReference(productoPedidoProveedorListProductoPedidoProveedorToAttach.getClass(), productoPedidoProveedorListProductoPedidoProveedorToAttach.getProductoPedidoProveedorPK());
                attachedProductoPedidoProveedorList.add(productoPedidoProveedorListProductoPedidoProveedorToAttach);
            }
            producto.setProductoPedidoProveedorList(attachedProductoPedidoProveedorList);
            List<ProductoPedidoVenta> attachedProductoPedidoVentaList = new ArrayList<ProductoPedidoVenta>();
            for (ProductoPedidoVenta productoPedidoVentaListProductoPedidoVentaToAttach : producto.getProductoPedidoVentaList()) {
                productoPedidoVentaListProductoPedidoVentaToAttach = em.getReference(productoPedidoVentaListProductoPedidoVentaToAttach.getClass(), productoPedidoVentaListProductoPedidoVentaToAttach.getIdProductoCliente());
                attachedProductoPedidoVentaList.add(productoPedidoVentaListProductoPedidoVentaToAttach);
            }
            producto.setProductoPedidoVentaList(attachedProductoPedidoVentaList);
            List<DevolucionClienteProducto> attachedDevolucionClienteProductoList = new ArrayList<DevolucionClienteProducto>();
            for (DevolucionClienteProducto devolucionClienteProductoListDevolucionClienteProductoToAttach : producto.getDevolucionClienteProductoList()) {
                devolucionClienteProductoListDevolucionClienteProductoToAttach = em.getReference(devolucionClienteProductoListDevolucionClienteProductoToAttach.getClass(), devolucionClienteProductoListDevolucionClienteProductoToAttach.getDevolucionClienteProductoPK());
                attachedDevolucionClienteProductoList.add(devolucionClienteProductoListDevolucionClienteProductoToAttach);
            }
            producto.setDevolucionClienteProductoList(attachedDevolucionClienteProductoList);
            em.persist(producto);
            if (salpa != null) {
                Producto oldProductoOfSalpa = salpa.getProducto();
                if (oldProductoOfSalpa != null) {
                    oldProductoOfSalpa.setSalpa(null);
                    oldProductoOfSalpa = em.merge(oldProductoOfSalpa);
                }
                salpa.setProducto(producto);
                salpa = em.merge(salpa);
            }
            if (tira != null) {
                Producto oldProductoOfTira = tira.getProducto();
                if (oldProductoOfTira != null) {
                    oldProductoOfTira.setTira(null);
                    oldProductoOfTira = em.merge(oldProductoOfTira);
                }
                tira.setProducto(producto);
                tira = em.merge(tira);
            }
            if (suela != null) {
                Producto oldProductoOfSuela = suela.getProducto();
                if (oldProductoOfSuela != null) {
                    oldProductoOfSuela.setSuela(null);
                    oldProductoOfSuela = em.merge(oldProductoOfSuela);
                }
                suela.setProducto(producto);
                suela = em.merge(suela);
            }
            if (plantilla != null) {
                Producto oldProductoOfPlantilla = plantilla.getProducto();
                if (oldProductoOfPlantilla != null) {
                    oldProductoOfPlantilla.setPlantilla(null);
                    oldProductoOfPlantilla = em.merge(oldProductoOfPlantilla);
                }
                plantilla.setProducto(producto);
                plantilla = em.merge(plantilla);
            }
            for (DevolucionProveedorProducto devolucionProveedorProductoListDevolucionProveedorProducto : producto.getDevolucionProveedorProductoList()) {
                Producto oldProductoOfDevolucionProveedorProductoListDevolucionProveedorProducto = devolucionProveedorProductoListDevolucionProveedorProducto.getProducto();
                devolucionProveedorProductoListDevolucionProveedorProducto.setProducto(producto);
                devolucionProveedorProductoListDevolucionProveedorProducto = em.merge(devolucionProveedorProductoListDevolucionProveedorProducto);
                if (oldProductoOfDevolucionProveedorProductoListDevolucionProveedorProducto != null) {
                    oldProductoOfDevolucionProveedorProductoListDevolucionProveedorProducto.getDevolucionProveedorProductoList().remove(devolucionProveedorProductoListDevolucionProveedorProducto);
                    oldProductoOfDevolucionProveedorProductoListDevolucionProveedorProducto = em.merge(oldProductoOfDevolucionProveedorProductoListDevolucionProveedorProducto);
                }
            }
            for (ProductoPedidoProveedor productoPedidoProveedorListProductoPedidoProveedor : producto.getProductoPedidoProveedorList()) {
                Producto oldProductoOfProductoPedidoProveedorListProductoPedidoProveedor = productoPedidoProveedorListProductoPedidoProveedor.getProducto();
                productoPedidoProveedorListProductoPedidoProveedor.setProducto(producto);
                productoPedidoProveedorListProductoPedidoProveedor = em.merge(productoPedidoProveedorListProductoPedidoProveedor);
                if (oldProductoOfProductoPedidoProveedorListProductoPedidoProveedor != null) {
                    oldProductoOfProductoPedidoProveedorListProductoPedidoProveedor.getProductoPedidoProveedorList().remove(productoPedidoProveedorListProductoPedidoProveedor);
                    oldProductoOfProductoPedidoProveedorListProductoPedidoProveedor = em.merge(oldProductoOfProductoPedidoProveedorListProductoPedidoProveedor);
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListProductoPedidoVenta : producto.getProductoPedidoVentaList()) {
                Producto oldProductoIdOfProductoPedidoVentaListProductoPedidoVenta = productoPedidoVentaListProductoPedidoVenta.getProductoId();
                productoPedidoVentaListProductoPedidoVenta.setProductoId(producto);
                productoPedidoVentaListProductoPedidoVenta = em.merge(productoPedidoVentaListProductoPedidoVenta);
                if (oldProductoIdOfProductoPedidoVentaListProductoPedidoVenta != null) {
                    oldProductoIdOfProductoPedidoVentaListProductoPedidoVenta.getProductoPedidoVentaList().remove(productoPedidoVentaListProductoPedidoVenta);
                    oldProductoIdOfProductoPedidoVentaListProductoPedidoVenta = em.merge(oldProductoIdOfProductoPedidoVentaListProductoPedidoVenta);
                }
            }
            for (DevolucionClienteProducto devolucionClienteProductoListDevolucionClienteProducto : producto.getDevolucionClienteProductoList()) {
                Producto oldProductoOfDevolucionClienteProductoListDevolucionClienteProducto = devolucionClienteProductoListDevolucionClienteProducto.getProducto();
                devolucionClienteProductoListDevolucionClienteProducto.setProducto(producto);
                devolucionClienteProductoListDevolucionClienteProducto = em.merge(devolucionClienteProductoListDevolucionClienteProducto);
                if (oldProductoOfDevolucionClienteProductoListDevolucionClienteProducto != null) {
                    oldProductoOfDevolucionClienteProductoListDevolucionClienteProducto.getDevolucionClienteProductoList().remove(devolucionClienteProductoListDevolucionClienteProducto);
                    oldProductoOfDevolucionClienteProductoListDevolucionClienteProducto = em.merge(oldProductoOfDevolucionClienteProductoListDevolucionClienteProducto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getId());
            Salpa salpaOld = persistentProducto.getSalpa();
            Salpa salpaNew = producto.getSalpa();
            Tira tiraOld = persistentProducto.getTira();
            Tira tiraNew = producto.getTira();
            Suela suelaOld = persistentProducto.getSuela();
            Suela suelaNew = producto.getSuela();
            Plantilla plantillaOld = persistentProducto.getPlantilla();
            Plantilla plantillaNew = producto.getPlantilla();
            List<DevolucionProveedorProducto> devolucionProveedorProductoListOld = persistentProducto.getDevolucionProveedorProductoList();
            List<DevolucionProveedorProducto> devolucionProveedorProductoListNew = producto.getDevolucionProveedorProductoList();
            List<ProductoPedidoProveedor> productoPedidoProveedorListOld = persistentProducto.getProductoPedidoProveedorList();
            List<ProductoPedidoProveedor> productoPedidoProveedorListNew = producto.getProductoPedidoProveedorList();
            List<ProductoPedidoVenta> productoPedidoVentaListOld = persistentProducto.getProductoPedidoVentaList();
            List<ProductoPedidoVenta> productoPedidoVentaListNew = producto.getProductoPedidoVentaList();
            List<DevolucionClienteProducto> devolucionClienteProductoListOld = persistentProducto.getDevolucionClienteProductoList();
            List<DevolucionClienteProducto> devolucionClienteProductoListNew = producto.getDevolucionClienteProductoList();
            List<String> illegalOrphanMessages = null;
            if (salpaOld != null && !salpaOld.equals(salpaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Salpa " + salpaOld + " since its producto field is not nullable.");
            }
            if (tiraOld != null && !tiraOld.equals(tiraNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tira " + tiraOld + " since its producto field is not nullable.");
            }
            if (suelaOld != null && !suelaOld.equals(suelaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Suela " + suelaOld + " since its producto field is not nullable.");
            }
            if (plantillaOld != null && !plantillaOld.equals(plantillaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Plantilla " + plantillaOld + " since its producto field is not nullable.");
            }
            for (DevolucionProveedorProducto devolucionProveedorProductoListOldDevolucionProveedorProducto : devolucionProveedorProductoListOld) {
                if (!devolucionProveedorProductoListNew.contains(devolucionProveedorProductoListOldDevolucionProveedorProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionProveedorProducto " + devolucionProveedorProductoListOldDevolucionProveedorProducto + " since its producto field is not nullable.");
                }
            }
            for (ProductoPedidoProveedor productoPedidoProveedorListOldProductoPedidoProveedor : productoPedidoProveedorListOld) {
                if (!productoPedidoProveedorListNew.contains(productoPedidoProveedorListOldProductoPedidoProveedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoPedidoProveedor " + productoPedidoProveedorListOldProductoPedidoProveedor + " since its producto field is not nullable.");
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListOldProductoPedidoVenta : productoPedidoVentaListOld) {
                if (!productoPedidoVentaListNew.contains(productoPedidoVentaListOldProductoPedidoVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoPedidoVenta " + productoPedidoVentaListOldProductoPedidoVenta + " since its productoId field is not nullable.");
                }
            }
            for (DevolucionClienteProducto devolucionClienteProductoListOldDevolucionClienteProducto : devolucionClienteProductoListOld) {
                if (!devolucionClienteProductoListNew.contains(devolucionClienteProductoListOldDevolucionClienteProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionClienteProducto " + devolucionClienteProductoListOldDevolucionClienteProducto + " since its producto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (salpaNew != null) {
                salpaNew = em.getReference(salpaNew.getClass(), salpaNew.getProductoId());
                producto.setSalpa(salpaNew);
            }
            if (tiraNew != null) {
                tiraNew = em.getReference(tiraNew.getClass(), tiraNew.getProductoId());
                producto.setTira(tiraNew);
            }
            if (suelaNew != null) {
                suelaNew = em.getReference(suelaNew.getClass(), suelaNew.getProductoId());
                producto.setSuela(suelaNew);
            }
            if (plantillaNew != null) {
                plantillaNew = em.getReference(plantillaNew.getClass(), plantillaNew.getProductoId());
                producto.setPlantilla(plantillaNew);
            }
            List<DevolucionProveedorProducto> attachedDevolucionProveedorProductoListNew = new ArrayList<DevolucionProveedorProducto>();
            for (DevolucionProveedorProducto devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach : devolucionProveedorProductoListNew) {
                devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach = em.getReference(devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach.getClass(), devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach.getDevolucionProveedorProductoPK());
                attachedDevolucionProveedorProductoListNew.add(devolucionProveedorProductoListNewDevolucionProveedorProductoToAttach);
            }
            devolucionProveedorProductoListNew = attachedDevolucionProveedorProductoListNew;
            producto.setDevolucionProveedorProductoList(devolucionProveedorProductoListNew);
            List<ProductoPedidoProveedor> attachedProductoPedidoProveedorListNew = new ArrayList<ProductoPedidoProveedor>();
            for (ProductoPedidoProveedor productoPedidoProveedorListNewProductoPedidoProveedorToAttach : productoPedidoProveedorListNew) {
                productoPedidoProveedorListNewProductoPedidoProveedorToAttach = em.getReference(productoPedidoProveedorListNewProductoPedidoProveedorToAttach.getClass(), productoPedidoProveedorListNewProductoPedidoProveedorToAttach.getProductoPedidoProveedorPK());
                attachedProductoPedidoProveedorListNew.add(productoPedidoProveedorListNewProductoPedidoProveedorToAttach);
            }
            productoPedidoProveedorListNew = attachedProductoPedidoProveedorListNew;
            producto.setProductoPedidoProveedorList(productoPedidoProveedorListNew);
            List<ProductoPedidoVenta> attachedProductoPedidoVentaListNew = new ArrayList<ProductoPedidoVenta>();
            for (ProductoPedidoVenta productoPedidoVentaListNewProductoPedidoVentaToAttach : productoPedidoVentaListNew) {
                productoPedidoVentaListNewProductoPedidoVentaToAttach = em.getReference(productoPedidoVentaListNewProductoPedidoVentaToAttach.getClass(), productoPedidoVentaListNewProductoPedidoVentaToAttach.getIdProductoCliente());
                attachedProductoPedidoVentaListNew.add(productoPedidoVentaListNewProductoPedidoVentaToAttach);
            }
            productoPedidoVentaListNew = attachedProductoPedidoVentaListNew;
            producto.setProductoPedidoVentaList(productoPedidoVentaListNew);
            List<DevolucionClienteProducto> attachedDevolucionClienteProductoListNew = new ArrayList<DevolucionClienteProducto>();
            for (DevolucionClienteProducto devolucionClienteProductoListNewDevolucionClienteProductoToAttach : devolucionClienteProductoListNew) {
                devolucionClienteProductoListNewDevolucionClienteProductoToAttach = em.getReference(devolucionClienteProductoListNewDevolucionClienteProductoToAttach.getClass(), devolucionClienteProductoListNewDevolucionClienteProductoToAttach.getDevolucionClienteProductoPK());
                attachedDevolucionClienteProductoListNew.add(devolucionClienteProductoListNewDevolucionClienteProductoToAttach);
            }
            devolucionClienteProductoListNew = attachedDevolucionClienteProductoListNew;
            producto.setDevolucionClienteProductoList(devolucionClienteProductoListNew);
            producto = em.merge(producto);
            if (salpaNew != null && !salpaNew.equals(salpaOld)) {
                Producto oldProductoOfSalpa = salpaNew.getProducto();
                if (oldProductoOfSalpa != null) {
                    oldProductoOfSalpa.setSalpa(null);
                    oldProductoOfSalpa = em.merge(oldProductoOfSalpa);
                }
                salpaNew.setProducto(producto);
                salpaNew = em.merge(salpaNew);
            }
            if (tiraNew != null && !tiraNew.equals(tiraOld)) {
                Producto oldProductoOfTira = tiraNew.getProducto();
                if (oldProductoOfTira != null) {
                    oldProductoOfTira.setTira(null);
                    oldProductoOfTira = em.merge(oldProductoOfTira);
                }
                tiraNew.setProducto(producto);
                tiraNew = em.merge(tiraNew);
            }
            if (suelaNew != null && !suelaNew.equals(suelaOld)) {
                Producto oldProductoOfSuela = suelaNew.getProducto();
                if (oldProductoOfSuela != null) {
                    oldProductoOfSuela.setSuela(null);
                    oldProductoOfSuela = em.merge(oldProductoOfSuela);
                }
                suelaNew.setProducto(producto);
                suelaNew = em.merge(suelaNew);
            }
            if (plantillaNew != null && !plantillaNew.equals(plantillaOld)) {
                Producto oldProductoOfPlantilla = plantillaNew.getProducto();
                if (oldProductoOfPlantilla != null) {
                    oldProductoOfPlantilla.setPlantilla(null);
                    oldProductoOfPlantilla = em.merge(oldProductoOfPlantilla);
                }
                plantillaNew.setProducto(producto);
                plantillaNew = em.merge(plantillaNew);
            }
            for (DevolucionProveedorProducto devolucionProveedorProductoListNewDevolucionProveedorProducto : devolucionProveedorProductoListNew) {
                if (!devolucionProveedorProductoListOld.contains(devolucionProveedorProductoListNewDevolucionProveedorProducto)) {
                    Producto oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto = devolucionProveedorProductoListNewDevolucionProveedorProducto.getProducto();
                    devolucionProveedorProductoListNewDevolucionProveedorProducto.setProducto(producto);
                    devolucionProveedorProductoListNewDevolucionProveedorProducto = em.merge(devolucionProveedorProductoListNewDevolucionProveedorProducto);
                    if (oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto != null && !oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto.equals(producto)) {
                        oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto.getDevolucionProveedorProductoList().remove(devolucionProveedorProductoListNewDevolucionProveedorProducto);
                        oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto = em.merge(oldProductoOfDevolucionProveedorProductoListNewDevolucionProveedorProducto);
                    }
                }
            }
            for (ProductoPedidoProveedor productoPedidoProveedorListNewProductoPedidoProveedor : productoPedidoProveedorListNew) {
                if (!productoPedidoProveedorListOld.contains(productoPedidoProveedorListNewProductoPedidoProveedor)) {
                    Producto oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor = productoPedidoProveedorListNewProductoPedidoProveedor.getProducto();
                    productoPedidoProveedorListNewProductoPedidoProveedor.setProducto(producto);
                    productoPedidoProveedorListNewProductoPedidoProveedor = em.merge(productoPedidoProveedorListNewProductoPedidoProveedor);
                    if (oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor != null && !oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor.equals(producto)) {
                        oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor.getProductoPedidoProveedorList().remove(productoPedidoProveedorListNewProductoPedidoProveedor);
                        oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor = em.merge(oldProductoOfProductoPedidoProveedorListNewProductoPedidoProveedor);
                    }
                }
            }
            for (ProductoPedidoVenta productoPedidoVentaListNewProductoPedidoVenta : productoPedidoVentaListNew) {
                if (!productoPedidoVentaListOld.contains(productoPedidoVentaListNewProductoPedidoVenta)) {
                    Producto oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta = productoPedidoVentaListNewProductoPedidoVenta.getProductoId();
                    productoPedidoVentaListNewProductoPedidoVenta.setProductoId(producto);
                    productoPedidoVentaListNewProductoPedidoVenta = em.merge(productoPedidoVentaListNewProductoPedidoVenta);
                    if (oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta != null && !oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta.equals(producto)) {
                        oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta.getProductoPedidoVentaList().remove(productoPedidoVentaListNewProductoPedidoVenta);
                        oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta = em.merge(oldProductoIdOfProductoPedidoVentaListNewProductoPedidoVenta);
                    }
                }
            }
            for (DevolucionClienteProducto devolucionClienteProductoListNewDevolucionClienteProducto : devolucionClienteProductoListNew) {
                if (!devolucionClienteProductoListOld.contains(devolucionClienteProductoListNewDevolucionClienteProducto)) {
                    Producto oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto = devolucionClienteProductoListNewDevolucionClienteProducto.getProducto();
                    devolucionClienteProductoListNewDevolucionClienteProducto.setProducto(producto);
                    devolucionClienteProductoListNewDevolucionClienteProducto = em.merge(devolucionClienteProductoListNewDevolucionClienteProducto);
                    if (oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto != null && !oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto.equals(producto)) {
                        oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto.getDevolucionClienteProductoList().remove(devolucionClienteProductoListNewDevolucionClienteProducto);
                        oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto = em.merge(oldProductoOfDevolucionClienteProductoListNewDevolucionClienteProducto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Salpa salpaOrphanCheck = producto.getSalpa();
            if (salpaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Salpa " + salpaOrphanCheck + " in its salpa field has a non-nullable producto field.");
            }
            Tira tiraOrphanCheck = producto.getTira();
            if (tiraOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Tira " + tiraOrphanCheck + " in its tira field has a non-nullable producto field.");
            }
            Suela suelaOrphanCheck = producto.getSuela();
            if (suelaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Suela " + suelaOrphanCheck + " in its suela field has a non-nullable producto field.");
            }
            Plantilla plantillaOrphanCheck = producto.getPlantilla();
            if (plantillaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Plantilla " + plantillaOrphanCheck + " in its plantilla field has a non-nullable producto field.");
            }
            List<DevolucionProveedorProducto> devolucionProveedorProductoListOrphanCheck = producto.getDevolucionProveedorProductoList();
            for (DevolucionProveedorProducto devolucionProveedorProductoListOrphanCheckDevolucionProveedorProducto : devolucionProveedorProductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DevolucionProveedorProducto " + devolucionProveedorProductoListOrphanCheckDevolucionProveedorProducto + " in its devolucionProveedorProductoList field has a non-nullable producto field.");
            }
            List<ProductoPedidoProveedor> productoPedidoProveedorListOrphanCheck = producto.getProductoPedidoProveedorList();
            for (ProductoPedidoProveedor productoPedidoProveedorListOrphanCheckProductoPedidoProveedor : productoPedidoProveedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the ProductoPedidoProveedor " + productoPedidoProveedorListOrphanCheckProductoPedidoProveedor + " in its productoPedidoProveedorList field has a non-nullable producto field.");
            }
            List<ProductoPedidoVenta> productoPedidoVentaListOrphanCheck = producto.getProductoPedidoVentaList();
            for (ProductoPedidoVenta productoPedidoVentaListOrphanCheckProductoPedidoVenta : productoPedidoVentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the ProductoPedidoVenta " + productoPedidoVentaListOrphanCheckProductoPedidoVenta + " in its productoPedidoVentaList field has a non-nullable productoId field.");
            }
            List<DevolucionClienteProducto> devolucionClienteProductoListOrphanCheck = producto.getDevolucionClienteProductoList();
            for (DevolucionClienteProducto devolucionClienteProductoListOrphanCheckDevolucionClienteProducto : devolucionClienteProductoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DevolucionClienteProducto " + devolucionClienteProductoListOrphanCheckDevolucionClienteProducto + " in its devolucionClienteProductoList field has a non-nullable producto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
