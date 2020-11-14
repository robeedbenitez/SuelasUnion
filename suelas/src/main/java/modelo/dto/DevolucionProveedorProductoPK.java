/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MANUEL
 */
@Embeddable
public class DevolucionProveedorProductoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "devolucion_proveedor_id")
    private int devolucionProveedorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private int productoId;

    public DevolucionProveedorProductoPK() {
    }

    public DevolucionProveedorProductoPK(int devolucionProveedorId, int productoId) {
        this.devolucionProveedorId = devolucionProveedorId;
        this.productoId = productoId;
    }

    public int getDevolucionProveedorId() {
        return devolucionProveedorId;
    }

    public void setDevolucionProveedorId(int devolucionProveedorId) {
        this.devolucionProveedorId = devolucionProveedorId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) devolucionProveedorId;
        hash += (int) productoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DevolucionProveedorProductoPK)) {
            return false;
        }
        DevolucionProveedorProductoPK other = (DevolucionProveedorProductoPK) object;
        if (this.devolucionProveedorId != other.devolucionProveedorId) {
            return false;
        }
        if (this.productoId != other.productoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DevolucionProveedorProductoPK[ devolucionProveedorId=" + devolucionProveedorId + ", productoId=" + productoId + " ]";
    }
    
}
