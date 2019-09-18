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
@Table(name = "USUARIO_GRADO")
@NamedQueries({
    @NamedQuery(name = "UsuarioGrado.findAll", query = "SELECT u FROM UsuarioGrado u")})
public class UsuarioGrado implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_GRADO")
    private BigDecimal idUsuarioGrado;
    @JoinColumn(name = "ID_GRADO", referencedColumnName = "ID_GRADO")
    @ManyToOne(optional = false)
    private Grado idGrado;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public UsuarioGrado() {
    }

    public UsuarioGrado(BigDecimal idUsuarioGrado) {
        this.idUsuarioGrado = idUsuarioGrado;
    }

    public BigDecimal getIdUsuarioGrado() {
        return idUsuarioGrado;
    }

    public void setIdUsuarioGrado(BigDecimal idUsuarioGrado) {
        this.idUsuarioGrado = idUsuarioGrado;
    }

    public Grado getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Grado idGrado) {
        this.idGrado = idGrado;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarioGrado != null ? idUsuarioGrado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioGrado)) {
            return false;
        }
        UsuarioGrado other = (UsuarioGrado) object;
        if ((this.idUsuarioGrado == null && other.idUsuarioGrado != null) || (this.idUsuarioGrado != null && !this.idUsuarioGrado.equals(other.idUsuarioGrado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica_negocio.UsuarioGrado[ idUsuarioGrado=" + idUsuarioGrado + " ]";
    }
    
}
