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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "salpa")
@NamedQueries({
    @NamedQuery(name = "Salpa.findAll", query = "SELECT s FROM Salpa s"),
    @NamedQuery(name = "Salpa.findByProductoId", query = "SELECT s FROM Salpa s WHERE s.productoId = :productoId")})
public class Salpa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private Integer productoId;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Producto producto;

    public Salpa() {
    }

    public Salpa(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
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
        hash += (productoId != null ? productoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salpa)) {
            return false;
        }
        Salpa other = (Salpa) object;
        if ((this.productoId == null && other.productoId != null) || (this.productoId != null && !this.productoId.equals(other.productoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.Salpa[ productoId=" + productoId + " ]";
    }
    
}
