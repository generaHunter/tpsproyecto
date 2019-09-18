/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica_negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author JoseM
 */
@Entity
@Table(name = "TELEFONO_ENCARGADO")
@NamedQueries({
    @NamedQuery(name = "TelefonoEncargado.findAll", query = "SELECT t FROM TelefonoEncargado t")})
public class TelefonoEncargado implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_TELEFONO")
    private BigDecimal idTelefono;
    @Basic(optional = false)
    @Column(name = "TELEFONO")
    private String telefono;
    @JoinColumn(name = "ID_ENCARGADO", referencedColumnName = "ID_ENCARGADO")
    @ManyToOne(optional = false)
    private Encargado idEncargado;

    public TelefonoEncargado() {
    }

    public TelefonoEncargado(BigDecimal idTelefono) {
        this.idTelefono = idTelefono;
    }

    public TelefonoEncargado(BigDecimal idTelefono, String telefono) {
        this.idTelefono = idTelefono;
        this.telefono = telefono;
    }

    public BigDecimal getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(BigDecimal idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Encargado getIdEncargado() {
        return idEncargado;
    }

    public void setIdEncargado(Encargado idEncargado) {
        this.idEncargado = idEncargado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTelefono != null ? idTelefono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelefonoEncargado)) {
            return false;
        }
        TelefonoEncargado other = (TelefonoEncargado) object;
        if ((this.idTelefono == null && other.idTelefono != null) || (this.idTelefono != null && !this.idTelefono.equals(other.idTelefono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica_negocio.TelefonoEncargado[ idTelefono=" + idTelefono + " ]";
    }
    
}
