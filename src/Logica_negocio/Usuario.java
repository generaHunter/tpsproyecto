/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica_negocio;
/**
 *
 * @author JoseM
 */
public class Usuario {
    private int idCliente;
    private String nombre;
    private String apellido;
    
     public void setNombre(String nombre) 
     {
        this.nombre = nombre;
     }
     
     public String getNombre() 
     {
        return nombre;
     }
      public void setApellido(String apellido) 
      {
        this.apellido = apellido;
      }
      public String getApellido() 
     {
        return apellido;
     }
      
}
