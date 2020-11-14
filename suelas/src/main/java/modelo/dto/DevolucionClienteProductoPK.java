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
public class DevolucionClienteProductoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "devolucion_cliente_id")
    private int devolucionClienteId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private int productoId;

    public DevolucionClienteProductoPK() {
    }

    public DevolucionClienteProductoPK(int devolucionClienteId, int productoId) {
        this.devolucionClienteId = devolucionClienteId;
        this.productoId = productoId;
    }

    public int getDevolucionClienteId() {
        return devolucionClienteId;
    }

    public void setDevolucionClienteId(int devolucionClienteId) {
        this.devolucionClienteId = devolucionClienteId;
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
        hash += (int) devolucionClienteId;
        hash += (int) productoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DevolucionClienteProductoPK)) {
            return false;
        }
        DevolucionClienteProductoPK other = (DevolucionClienteProductoPK) object;
        if (this.devolucionClienteId != other.devolucionClienteId) {
            return false;
        }
        if (this.productoId != other.productoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DevolucionClienteProductoPK[ devolucionClienteId=" + devolucionClienteId + ", productoId=" + productoId + " ]";
    }
    
}
