/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

/**
 *
 * @author  Evans.C
 */
public class Inventario extends Material{
    String tipoUnidad;
    int cantidad;
    String ubicacion;

    public Inventario(String codigo,String descripcion,String categoria,int cantidadMinima, String tipoUnidad, int cantidad, String ubicacion) {
        this.tipoUnidad = tipoUnidad;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        
    }

    public Inventario() {
    }
    

    
    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
}
