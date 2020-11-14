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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author MANUEL
 */
@Entity
@Table(name = "suela")
@NamedQueries({
    @NamedQuery(name = "Suela.findAll", query = "SELECT s FROM Suela s"),
    @NamedQuery(name = "Suela.findByModelo", query = "SELECT s FROM Suela s WHERE s.modelo = :modelo"),
    @NamedQuery(name = "Suela.findByTipoSuela", query = "SELECT s FROM Suela s WHERE s.tipoSuela = :tipoSuela"),
    @NamedQuery(name = "Suela.findByProductoId", query = "SELECT s FROM Suela s WHERE s.productoId = :productoId"),
    @NamedQuery(name = "Suela.findByTalla", query = "SELECT s FROM Suela s WHERE s.talla = :talla")})
public class Suela implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 10)
    @Column(name = "modelo")
    private String modelo;
    @Size(max = 10)
    @Column(name = "tipo_suela")
    private String tipoSuela;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private Integer productoId;
    @Size(max = 2)
    @Column(name = "talla")
    private String talla;
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Color colorId;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "tipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipo tipoId;

    public Suela() {
    }

    public Suela(Integer productoId) {
        this.productoId = productoId;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipoSuela() {
        return tipoSuela;
    }

    public void setTipoSuela(String tipoSuela) {
        this.tipoSuela = tipoSuela;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Color getColorId() {
        return colorId;
    }

    public void setColorId(Color colorId) {
        this.colorId = colorId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Tipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(Tipo tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof Suela)) {
            return false;
        }
        Suela other = (Suela) object;
        if ((this.productoId == null && other.productoId != null) || (this.productoId != null && !this.productoId.equals(other.productoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.Suela[ productoId=" + productoId + " ]";
    }
    
}
