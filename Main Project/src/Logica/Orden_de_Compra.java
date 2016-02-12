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
public class Orden_de_Compra extends Proveedor{

    int numOrden;
    int cantidad;
    String tipoUnidad;
    String descripcion; //la descripcion del producto se va a jalar de la clase producto
    String codigo;//el codigo del producto se va a jalar de la clase producto
    float precioUnitario;
    float precioTotal;

    public Orden_de_Compra(int numOrden, int cantidad, String tipoUnidad, String descripcion, String codigo, float precioUnitario, float precioTotal, String empresa, String cedJuridica, String nombreContacto, String apellido, String tel, String codProveedor, String direccion) {
        super(empresa, cedJuridica, nombreContacto, apellido, tel, codProveedor, direccion);
        this.numOrden = numOrden;
        this.cantidad = cantidad;
        this.tipoUnidad = tipoUnidad;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    public Orden_de_Compra(int numOrden, int cantidad, String tipoUnidad, String descripcion, String codigo, float precioUnitario, float precioTotal) {
        this.numOrden = numOrden;
        this.cantidad = cantidad;
        this.tipoUnidad = tipoUnidad;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(int numOrden) {
        this.numOrden = numOrden;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }
    
    
    
}
