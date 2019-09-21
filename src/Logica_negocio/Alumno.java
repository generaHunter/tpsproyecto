/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica_negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author JoseM
 */
@Entity
@Table(name = "ALUMNO")
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a")})
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_id_alumno", sequenceName = "seq_id_alumno", allocationSize = 1) 
    @GeneratedValue(strategy= GenerationType.IDENTITY , generator="seq_id_alumno")
    @Column(name = "ID_ALUMNO")
    private BigDecimal idAlumno;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "FECHA_NAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNac;
    @Column(name = "ESTADO")
    private Character estado;
    @JoinColumn(name = "ID_ENCARGADO", referencedColumnName = "ID_ENCARGADO")
    @ManyToOne(optional = false)
    private Encargado idEncargado;

    public Alumno() {
    }

    public Alumno(BigDecimal idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Alumno(BigDecimal idAlumno, String nombre, String apellido) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public BigDecimal getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(BigDecimal idAlumno) {
        this.idAlumno = idAlumno;
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

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
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
        hash += (idAlumno != null ? idAlumno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.idAlumno == null && other.idAlumno != null) || (this.idAlumno != null && !this.idAlumno.equals(other.idAlumno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica_negocio.Alumno[ idAlumno=" + idAlumno + " ]";
    }
    
}
