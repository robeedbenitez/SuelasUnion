/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "devolucion_proveedor_producto")
@NamedQueries({
    @NamedQuery(name = "DevolucionProveedorProducto.findAll", query = "SELECT d FROM DevolucionProveedorProducto d"),
    @NamedQuery(name = "DevolucionProveedorProducto.findByDevolucionProveedorId", query = "SELECT d FROM DevolucionProveedorProducto d WHERE d.devolucionProveedorProductoPK.devolucionProveedorId = :devolucionProveedorId"),
    @NamedQuery(name = "DevolucionProveedorProducto.findByProductoId", query = "SELECT d FROM DevolucionProveedorProducto d WHERE d.devolucionProveedorProductoPK.productoId = :productoId"),
    @NamedQuery(name = "DevolucionProveedorProducto.findByCantidad", query = "SELECT d FROM DevolucionProveedorProducto d WHERE d.cantidad = :cantidad")})
public class DevolucionProveedorProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DevolucionProveedorProductoPK devolucionProveedorProductoPK;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "devolucion_proveedor_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DevolucionProveedor devolucionProveedor;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public DevolucionProveedorProducto() {
    }

    public DevolucionProveedorProducto(DevolucionProveedorProductoPK devolucionProveedorProductoPK) {
        this.devolucionProveedorProductoPK = devolucionProveedorProductoPK;
    }

    public DevolucionProveedorProducto(int devolucionProveedorId, int productoId) {
        this.devolucionProveedorProductoPK = new DevolucionProveedorProductoPK(devolucionProveedorId, productoId);
    }

    public DevolucionProveedorProductoPK getDevolucionProveedorProductoPK() {
        return devolucionProveedorProductoPK;
    }

    public void setDevolucionProveedorProductoPK(DevolucionProveedorProductoPK devolucionProveedorProductoPK) {
        this.devolucionProveedorProductoPK = devolucionProveedorProductoPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DevolucionProveedor getDevolucionProveedor() {
        return devolucionProveedor;
    }

    public void setDevolucionProveedor(DevolucionProveedor devolucionProveedor) {
        this.devolucionProveedor = devolucionProveedor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (devolucionProveedorProductoPK != null ? devolucionProveedorProductoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DevolucionProveedorProducto)) {
            return false;
        }
        DevolucionProveedorProducto other = (DevolucionProveedorProducto) object;
        if ((this.devolucionProveedorProductoPK == null && other.devolucionProveedorProductoPK != null) || (this.devolucionProveedorProductoPK != null && !this.devolucionProveedorProductoPK.equals(other.devolucionProveedorProductoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DevolucionProveedorProducto[ devolucionProveedorProductoPK=" + devolucionProveedorProductoPK + " ]";
    }
    
}
