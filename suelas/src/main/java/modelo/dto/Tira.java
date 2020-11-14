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
@Table(name = "tira")
@NamedQueries({
    @NamedQuery(name = "Tira.findAll", query = "SELECT t FROM Tira t"),
    @NamedQuery(name = "Tira.findByModelo", query = "SELECT t FROM Tira t WHERE t.modelo = :modelo"),
    @NamedQuery(name = "Tira.findByAnchura", query = "SELECT t FROM Tira t WHERE t.anchura = :anchura"),
    @NamedQuery(name = "Tira.findByProductoId", query = "SELECT t FROM Tira t WHERE t.productoId = :productoId")})
public class Tira implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "modelo")
    private String modelo;
    @Size(max = 2)
    @Column(name = "anchura")
    private String anchura;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_id")
    private Integer productoId;
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Color colorId;
    @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Producto producto;

    public Tira() {
    }

    public Tira(Integer productoId) {
        this.productoId = productoId;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnchura() {
        return anchura;
    }

    public void setAnchura(String anchura) {
        this.anchura = anchura;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoId != null ? productoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tira)) {
            return false;
        }
        Tira other = (Tira) object;
        if ((this.productoId == null && other.productoId != null) || (this.productoId != null && !this.productoId.equals(other.productoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.dto.Tira[ productoId=" + productoId + " ]";
    }
    
}
