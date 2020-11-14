/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "producto_pedido_proveedor")
@NamedQueries({
    @NamedQuery(name = "ProductoPedidoProveedor.findAll", query = "SELECT p FROM ProductoPedidoProveedor p"),
    @NamedQuery(name = "ProductoPedidoProveedor.findByCantidad", query = "SELECT p FROM ProductoPedidoProveedor p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "ProductoPedidoProveedor.findByPrecio", query = "SELECT p FROM ProductoPedidoProveedor p WHERE p.precio = :precio"),
    @NamedQuery(name = "ProductoPedidoProveedor.findByFecha", query = "SELECT p FROM ProductoPedidoProveedor p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "ProductoPedidoProveedor.findByProductoId", query = "SELECT p FROM ProductoPedidoProveedor p WHERE p.productoPedidoProveedorPK.productoId = :productoId"),
    @NamedQuery(name = "ProductoPedidoProveedor.findByPedidoProveedorId", query = "SELECT p FROM ProductoPedidoProveedor p WHERE p.productoPedidoProveedorPK.pedidoProveedorId = :pedidoProveedorId")})
public class ProductoPedidoProveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoPedidoProveedorPK productoPedidoProveedorPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;
    @Column(name = "precio")
    private Double precio;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "pedido_proveedor_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PedidoProveedor pedidoProveedor;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public ProductoPedidoProveedor() {
    }

    public ProductoPedidoProveedor(ProductoPedidoProveedorPK productoPedidoProveedorPK) {
        this.productoPedidoProveedorPK = productoPedidoProveedorPK;
    }

    public ProductoPedidoProveedor(int productoId, int pedidoProveedorId) {
        this.productoPedidoProveedorPK = new ProductoPedidoProveedorPK(productoId, pedidoProveedorId);
    }

    public ProductoPedidoProveedorPK getProductoPedidoProveedorPK() {
        return productoPedidoProveedorPK;
    }

    public void setProductoPedidoProveedorPK(ProductoPedidoProveedorPK productoPedidoProveedorPK) {
        this.productoPedidoProveedorPK = productoPedidoProveedorPK;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public PedidoProveedor getPedidoProveedor() {
        return pedidoProveedor;
    }

    public void setPedidoProveedor(PedidoProveedor pedidoProveedor) {
        this.pedidoProveedor = pedidoProveedor;
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
        hash += (productoPedidoProveedorPK != null ? productoPedidoProveedorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPedidoProveedor)) {
            return false;
        }
        ProductoPedidoProveedor other = (ProductoPedidoProveedor) object;
        if ((this.productoPedidoProveedorPK == null && other.productoPedidoProveedorPK != null) || (this.productoPedidoProveedorPK != null && !this.productoPedidoProveedorPK.equals(other.productoPedidoProveedorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.ProductoPedidoProveedor[ productoPedidoProveedorPK=" + productoPedidoProveedorPK + " ]";
    }
    
}
