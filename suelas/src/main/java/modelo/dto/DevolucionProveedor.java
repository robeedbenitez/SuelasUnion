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
import javax.validation.constraints.Size;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "devolucion_proveedor")
@NamedQueries({
    @NamedQuery(name = "DevolucionProveedor.findAll", query = "SELECT d FROM DevolucionProveedor d"),
    @NamedQuery(name = "DevolucionProveedor.findById", query = "SELECT d FROM DevolucionProveedor d WHERE d.id = :id"),
    @NamedQuery(name = "DevolucionProveedor.findByDescripcion", query = "SELECT d FROM DevolucionProveedor d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "DevolucionProveedor.findByFechaInicio", query = "SELECT d FROM DevolucionProveedor d WHERE d.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "DevolucionProveedor.findByFechaFin", query = "SELECT d FROM DevolucionProveedor d WHERE d.fechaFin = :fechaFin"),
    @NamedQuery(name = "DevolucionProveedor.findByValorTotal", query = "SELECT d FROM DevolucionProveedor d WHERE d.valorTotal = :valorTotal")})
public class DevolucionProveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_total")
    private Double valorTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "devolucionProveedor")
    private List<DevolucionProveedorProducto> devolucionProveedorProductoList;
    @JoinColumn(name = "pedido_proveedor_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PedidoProveedor pedidoProveedorId;

    public DevolucionProveedor() {
    }

    public DevolucionProveedor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<DevolucionProveedorProducto> getDevolucionProveedorProductoList() {
        return devolucionProveedorProductoList;
    }

    public void setDevolucionProveedorProductoList(List<DevolucionProveedorProducto> devolucionProveedorProductoList) {
        this.devolucionProveedorProductoList = devolucionProveedorProductoList;
    }

    public PedidoProveedor getPedidoProveedorId() {
        return pedidoProveedorId;
    }

    public void setPedidoProveedorId(PedidoProveedor pedidoProveedorId) {
        this.pedidoProveedorId = pedidoProveedorId;
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
        if (!(object instanceof DevolucionProveedor)) {
            return false;
        }
        DevolucionProveedor other = (DevolucionProveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.DevolucionProveedor[ id=" + id + " ]";
    }
    
}
