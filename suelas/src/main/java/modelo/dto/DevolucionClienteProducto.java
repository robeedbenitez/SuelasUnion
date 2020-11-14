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
@Table(name = "devolucion_cliente_producto")
@NamedQueries({
    @NamedQuery(name = "DevolucionClienteProducto.findAll", query = "SELECT d FROM DevolucionClienteProducto d"),
    @NamedQuery(name = "DevolucionClienteProducto.findByCantidad", query = "SELECT d FROM DevolucionClienteProducto d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DevolucionClienteProducto.findByDevolucionClienteId", query = "SELECT d FROM DevolucionClienteProducto d WHERE d.devolucionClienteProductoPK.devolucionClienteId = :devolucionClienteId"),
    @NamedQuery(name = "DevolucionClienteProducto.findByProductoId", query = "SELECT d FROM DevolucionClienteProducto d WHERE d.devolucionClienteProductoPK.productoId = :productoId")})
public class DevolucionClienteProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DevolucionClienteProductoPK devolucionClienteProductoPK;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "devolucion_cliente_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DevolucionCliente devolucionCliente;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public DevolucionClienteProducto() {
    }

    public DevolucionClienteProducto(DevolucionClienteProductoPK devolucionClienteProductoPK) {
        this.devolucionClienteProductoPK = devolucionClienteProductoPK;
    }

    public DevolucionClienteProducto(int devolucionClienteId, int productoId) {
        this.devolucionClienteProductoPK = new DevolucionClienteProductoPK(devolucionClienteId, productoId);
    }

    public DevolucionClienteProductoPK getDevolucionClienteProductoPK() {
        return devolucionClienteProductoPK;
    }

    public void setDevolucionClienteProductoPK(DevolucionClienteProductoPK devolucionClienteProductoPK) {
        this.devolucionClienteProductoPK = devolucionClienteProductoPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DevolucionCliente getDevolucionCliente() {
        return devolucionCliente;
    }

    public void setDevolucionCliente(DevolucionCliente devolucionCliente) {
        this.devolucionCliente = devolucionCliente;
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
        hash += (devolucionClienteProductoPK != null ? devolucionClienteProductoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DevolucionClienteProducto)) {
            return false;
        }
        DevolucionClienteProducto other = (DevolucionClienteProducto) object;
        if ((this.devolucionClienteProductoPK == null && other.devolucionClienteProductoPK != null) || (this.devolucionClienteProductoPK != null && !this.devolucionClienteProductoPK.equals(other.devolucionClienteProductoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DevolucionClienteProducto[ devolucionClienteProductoPK=" + devolucionClienteProductoPK + " ]";
    }
    
}
