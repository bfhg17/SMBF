/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Logica;

/**
 *
 * @author juaco_000
 */
public class Proveedor{
    String empresa;
    String cedJuridica;
    String nombreContacto;
    String apellido;
    String tel;
    String codProveedor;
    String direccion;
    
    public Proveedor(String empresa, String cedJuridica, String nombreContacto, String apellido, String tel, String codProveedor, String direccion) {
        this.empresa = empresa;
        this.cedJuridica = cedJuridica;
        this.nombreContacto = nombreContacto;
        this.apellido = apellido;
        this.tel = tel;
        this.codProveedor = codProveedor;
        this.direccion = direccion;
    }

    public Proveedor() {
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCedJuridica() {
        return cedJuridica;
    }

    public void setCedJuridica(String cedJuridica) {
        this.cedJuridica = cedJuridica;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(String codProveedor) {
        this.codProveedor = codProveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
