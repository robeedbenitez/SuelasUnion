/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findById", query = "SELECT p FROM Producto p WHERE p.id = :id"),
    @NamedQuery(name = "Producto.findByTipoProducto", query = "SELECT p FROM Producto p WHERE p.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Producto.findByCantidadDisponible", query = "SELECT p FROM Producto p WHERE p.cantidadDisponible = :cantidadDisponible"),
    @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio"),
    @NamedQuery(name = "Producto.findByCantidadMinRequerida", query = "SELECT p FROM Producto p WHERE p.cantidadMinRequerida = :cantidadMinRequerida"),
    @NamedQuery(name = "Producto.findByProveedorid", query = "SELECT p FROM Producto p WHERE p.proveedorid = :proveedorid")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1)
    @Column(name = "tipo_producto")
    private String tipoProducto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_disponible")
    private Double cantidadDisponible;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "cantidad_min_requerida")
    private Double cantidadMinRequerida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Proveedor_id")
    private int proveedorid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private List<DevolucionProveedorProducto> devolucionProveedorProductoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private List<ProductoPedidoProveedor> productoPedidoProveedorList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "producto")
    private Salpa salpa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoId")
    private List<ProductoPedidoVenta> productoPedidoVentaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "producto")
    private Tira tira;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "producto")
    private Suela suela;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private List<DevolucionClienteProducto> devolucionClienteProductoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "producto")
    private Plantilla plantilla;

    public Producto() {
    }

    public Producto(Integer id) {
        this.id = id;
    }

    public Producto(Integer id, int proveedorid) {
        this.id = id;
        this.proveedorid = proveedorid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Double getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Double cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCantidadMinRequerida() {
        return cantidadMinRequerida;
    }

    public void setCantidadMinRequerida(Double cantidadMinRequerida) {
        this.cantidadMinRequerida = cantidadMinRequerida;
    }

    public int getProveedorid() {
        return proveedorid;
    }

    public void setProveedorid(int proveedorid) {
        this.proveedorid = proveedorid;
    }

    public List<DevolucionProveedorProducto> getDevolucionProveedorProductoList() {
        return devolucionProveedorProductoList;
    }

    public void setDevolucionProveedorProductoList(List<DevolucionProveedorProducto> devolucionProveedorProductoList) {
        this.devolucionProveedorProductoList = devolucionProveedorProductoList;
    }

    public List<ProductoPedidoProveedor> getProductoPedidoProveedorList() {
        return productoPedidoProveedorList;
    }

    public void setProductoPedidoProveedorList(List<ProductoPedidoProveedor> productoPedidoProveedorList) {
        this.productoPedidoProveedorList = productoPedidoProveedorList;
    }

    public Salpa getSalpa() {
        return salpa;
    }

    public void setSalpa(Salpa salpa) {
        this.salpa = salpa;
    }

    public List<ProductoPedidoVenta> getProductoPedidoVentaList() {
        return productoPedidoVentaList;
    }

    public void setProductoPedidoVentaList(List<ProductoPedidoVenta> productoPedidoVentaList) {
        this.productoPedidoVentaList = productoPedidoVentaList;
    }

    public Tira getTira() {
        return tira;
    }

    public void setTira(Tira tira) {
        this.tira = tira;
    }

    public Suela getSuela() {
        return suela;
    }

    public void setSuela(Suela suela) {
        this.suela = suela;
    }

    public List<DevolucionClienteProducto> getDevolucionClienteProductoList() {
        return devolucionClienteProductoList;
    }

    public void setDevolucionClienteProductoList(List<DevolucionClienteProducto> devolucionClienteProductoList) {
        this.devolucionClienteProductoList = devolucionClienteProductoList;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.Producto[ id=" + id + " ]";
    }
    
}
