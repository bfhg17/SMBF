/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


    package Logica;

/**
 *
 * @author Evans.C
 */

public class Admi_Usuarios extends  Usuarios{

    String idUsuario;
    String password;
    String Nombre;
    String apellido;
    String puesto;
    String descripcion;
    
    public Admi_Usuarios(String idUsuario, String password, String Nombre, String apellido, String puesto, String descripcion, 
            String nombre, String id_Usu, String contrasena, String nickname, String cargo, String departamento, String estado, String descripcionAdicional) {
     
        this.idUsuario = idUsuario;
        this.password = password;
        this.Nombre = Nombre;
        this.apellido = apellido;
        this.puesto = puesto;
        this.descripcion = descripcion;
    } 
    
    
    public Admi_Usuarios(String idUsuario, String password, String Nombre, String apellido, String puesto, String descripcion) {

        
        this.idUsuario = idUsuario;
        this.password = password;
        this.Nombre = Nombre;
        this.apellido = apellido;
        this.puesto = puesto;
        this.descripcion = descripcion;
    }

   public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto= puesto;
    }


    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

}

    
    
