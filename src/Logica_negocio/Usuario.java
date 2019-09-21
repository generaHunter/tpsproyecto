/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica_negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 *
 * @author JoseM
 */
@Entity
@Table(name = "USUARIO")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_id_usuario", sequenceName = "seq_id_usuario", allocationSize = 1) 
    @GeneratedValue(strategy= GenerationType.IDENTITY , generator="seq_id_usuario")
    @Column(name = "ID_USUARIO")
    @Ignore
    private BigDecimal idUsuario;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "APELLIDO")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "PASS")
    private String pass;
    @Column(name = "FECHA_NAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNac;
    @Column(name = "DUI")
    private String dui;
    @Column(name = "ESTADO")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<UsuarioGrado> usuarioGradoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<TelefonoUsuario> telefonoUsuarioList;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "ID_TIPO")
    @ManyToOne
    private TipoUsuario idTipo;

    public Usuario() {
    }

    public Usuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(BigDecimal idUsuario, String nombre, String apellido, String username, String pass) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.pass = pass;
    }

    public BigDecimal getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public List<UsuarioGrado> getUsuarioGradoList() {
        return usuarioGradoList;
    }

    public void setUsuarioGradoList(List<UsuarioGrado> usuarioGradoList) {
        this.usuarioGradoList = usuarioGradoList;
    }

    public List<TelefonoUsuario> getTelefonoUsuarioList() {
        return telefonoUsuarioList;
    }

    public void setTelefonoUsuarioList(List<TelefonoUsuario> telefonoUsuarioList) {
        this.telefonoUsuarioList = telefonoUsuarioList;
    }

    public TipoUsuario getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(TipoUsuario idTipo) {
        this.idTipo = idTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "Logica_negocio.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
