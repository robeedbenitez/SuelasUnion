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
import javax.validation.constraints.Size;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "plantilla")
@NamedQueries({
    @NamedQuery(name = "Plantilla.findAll", query = "SELECT p FROM Plantilla p"),
    @NamedQuery(name = "Plantilla.findByModelo", query = "SELECT p FROM Plantilla p WHERE p.modelo = :modelo"),
    @NamedQuery(name = "Plantilla.findByTalla", query = "SELECT p FROM Plantilla p WHERE p.talla = :talla"),
    @NamedQuery(name = "Plantilla.findByProductoId", query = "SELECT p FROM Plantilla p WHERE p.productoId = :productoId")})
public class Plantilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "modelo")
    private String modelo;
    @Size(max = 2)
    @Column(name = "talla")
    private String talla;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private Integer productoId;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Producto producto;

    public Plantilla() {
    }

    public Plantilla(Integer productoId) {
        this.productoId = productoId;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
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
        if (!(object instanceof Plantilla)) {
            return false;
        }
        Plantilla other = (Plantilla) object;
        if ((this.productoId == null && other.productoId != null) || (this.productoId != null && !this.productoId.equals(other.productoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.Plantilla[ productoId=" + productoId + " ]";
    }
    
}
