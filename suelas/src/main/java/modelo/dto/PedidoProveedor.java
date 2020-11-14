/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "pedido_proveedor")
@NamedQueries({
    @NamedQuery(name = "PedidoProveedor.findAll", query = "SELECT p FROM PedidoProveedor p"),
    @NamedQuery(name = "PedidoProveedor.findById", query = "SELECT p FROM PedidoProveedor p WHERE p.id = :id"),
    @NamedQuery(name = "PedidoProveedor.findByFecha", query = "SELECT p FROM PedidoProveedor p WHERE p.fecha = :fecha")})
public class PedidoProveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidoProveedor")
    private List<ProductoPedidoProveedor> productoPedidoProveedorList;
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedor proveedorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidoProveedorId")
    private List<DevolucionProveedor> devolucionProveedorList;

    public PedidoProveedor() {
    }

    public PedidoProveedor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ProductoPedidoProveedor> getProductoPedidoProveedorList() {
        return productoPedidoProveedorList;
    }

    public void setProductoPedidoProveedorList(List<ProductoPedidoProveedor> productoPedidoProveedorList) {
        this.productoPedidoProveedorList = productoPedidoProveedorList;
    }

    public Proveedor getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Proveedor proveedorId) {
        this.proveedorId = proveedorId;
    }

    public List<DevolucionProveedor> getDevolucionProveedorList() {
        return devolucionProveedorList;
    }

    public void setDevolucionProveedorList(List<DevolucionProveedor> devolucionProveedorList) {
        this.devolucionProveedorList = devolucionProveedorList;
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
        if (!(object instanceof PedidoProveedor)) {
            return false;
        }
        PedidoProveedor other = (PedidoProveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.PedidoProveedor[ id=" + id + " ]";
    }
    
}
