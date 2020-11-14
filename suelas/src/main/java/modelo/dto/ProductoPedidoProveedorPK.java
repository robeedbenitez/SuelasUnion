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
public class ProductoPedidoProveedorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private int productoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pedido_proveedor_id")
    private int pedidoProveedorId;

    public ProductoPedidoProveedorPK() {
    }

    public ProductoPedidoProveedorPK(int productoId, int pedidoProveedorId) {
        this.productoId = productoId;
        this.pedidoProveedorId = pedidoProveedorId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getPedidoProveedorId() {
        return pedidoProveedorId;
    }

    public void setPedidoProveedorId(int pedidoProveedorId) {
        this.pedidoProveedorId = pedidoProveedorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productoId;
        hash += (int) pedidoProveedorId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPedidoProveedorPK)) {
            return false;
        }
        ProductoPedidoProveedorPK other = (ProductoPedidoProveedorPK) object;
        if (this.productoId != other.productoId) {
            return false;
        }
        if (this.pedidoProveedorId != other.pedidoProveedorId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.ProductoPedidoProveedorPK[ productoId=" + productoId + ", pedidoProveedorId=" + pedidoProveedorId + " ]";
    }
    
}
