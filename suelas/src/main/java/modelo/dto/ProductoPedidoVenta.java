/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "producto_pedido_venta")
@NamedQueries({
    @NamedQuery(name = "ProductoPedidoVenta.findAll", query = "SELECT p FROM ProductoPedidoVenta p"),
    @NamedQuery(name = "ProductoPedidoVenta.findByIdProductoCliente", query = "SELECT p FROM ProductoPedidoVenta p WHERE p.idProductoCliente = :idProductoCliente"),
    @NamedQuery(name = "ProductoPedidoVenta.findByCantidad", query = "SELECT p FROM ProductoPedidoVenta p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "ProductoPedidoVenta.findByTotal", query = "SELECT p FROM ProductoPedidoVenta p WHERE p.total = :total")})
public class ProductoPedidoVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_producto_cliente")
    private Integer idProductoCliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;
    @Column(name = "total")
    private Double total;
    @JoinColumn(name = "pedido_de_venta_id_pedido_de_venta", referencedColumnName = "id_pedido_de_venta")
    @ManyToOne(optional = false)
    private PedidoDeVenta pedidoDeVentaIdPedidoDeVenta;
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto productoId;

    public ProductoPedidoVenta() {
    }

    public ProductoPedidoVenta(Integer idProductoCliente) {
        this.idProductoCliente = idProductoCliente;
    }

    public Integer getIdProductoCliente() {
        return idProductoCliente;
    }

    public void setIdProductoCliente(Integer idProductoCliente) {
        this.idProductoCliente = idProductoCliente;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public PedidoDeVenta getPedidoDeVentaIdPedidoDeVenta() {
        return pedidoDeVentaIdPedidoDeVenta;
    }

    public void setPedidoDeVentaIdPedidoDeVenta(PedidoDeVenta pedidoDeVentaIdPedidoDeVenta) {
        this.pedidoDeVentaIdPedidoDeVenta = pedidoDeVentaIdPedidoDeVenta;
    }

    public Producto getProductoId() {
        return productoId;
    }

    public void setProductoId(Producto productoId) {
        this.productoId = productoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductoCliente != null ? idProductoCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPedidoVenta)) {
            return false;
        }
        ProductoPedidoVenta other = (ProductoPedidoVenta) object;
        if ((this.idProductoCliente == null && other.idProductoCliente != null) || (this.idProductoCliente != null && !this.idProductoCliente.equals(other.idProductoCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.ProductoPedidoVenta[ idProductoCliente=" + idProductoCliente + " ]";
    }
    
}
