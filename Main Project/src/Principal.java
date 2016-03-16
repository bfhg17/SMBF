
import DB.JavaConnection;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bradley
 */
public class Principal extends javax.swing.JFrame {
    int x,y; 
    DateFormat dateFormat;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    Calendar cal = Calendar.getInstance();
     String datosX[]=new String[4];
    /**
     * Creates new form NewJFrame
     */
    DefaultTableModel modeloTablaNuevoMaterial;
    DefaultTableModel modeloTablaBusquedaEspecifica;
    DefaultTableModel modeloTablaRegistrar;
    DefaultTableModel modeloTablaCrearOrden;
    DB.JavaConnection con = new DB.JavaConnection();
    
    Connection cn= con.connect();
    int codProve=0;
    
    public Principal() {
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        modeloTablaNuevoMaterial = new DefaultTableModel(null,getColumnasInventario());
        modeloTablaBusquedaEspecifica = new DefaultTableModel(null,getColumnasInventario());
        modeloTablaRegistrar = new DefaultTableModel(null,getColumnasInventario());
        modeloTablaCrearOrden=new DefaultTableModel(null,getColumnasOrden());
        setUndecorated(true);
        initComponents();
       setExtendedState(MAXIMIZED_BOTH);
        setFilasInventario();//Inicializacion de los dos metodos para las tablas
        setLocationRelativeTo(null);
       this.getContentPane().setBackground(Color.white);
    }
public static String now() {
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
return sdf.format(cal.getTime());

}
  private String[] getColumnasOrden(){//b
    
    String columna[] = new String[]{"Fecha","Codigo Proveedor","Cantidad","Tipo de Unidad","Codigo Material","Descripcion","Número Orden","Precio unitario ¢","Precio Total ¢"};
    /*    modeloTablaCrearOrden.addColumn("Fecha");
    modeloTablaCrearOrden.addColumn("Código Proveedor");
    modeloTablaCrearOrden.addColumn("Cantidad");
    modeloTablaCrearOrden.addColumn("Tipo Unidad");
    modeloTablaCrearOrden.addColumn("Codigo Material");
    modeloTablaCrearOrden.addColumn("Descripción");
    modeloTablaCrearOrden.addColumn("Número Orden");
    modeloTablaCrearOrden.addColumn("Precio Unitario ¢");
    modeloTablaCrearOrden.addColumn("Precio Total ¢");*/
    return columna;
    }
    private String[] getColumnasInventario(){//b
    
    String columna[] = new String[]{"ID","Nombre","Categoría","Tipo de Unidad","Cantidad Mínima","Cantidad en Stock","Ubicación","Última Fecha de Registro"};
    return columna;
    }
    
    private void setFilasInventario(){//b
    
        try{  
       
      
            String sql = "SELECT id_material,nombre,categoria,tipoUnidad,cantidadMinima,cantidadStock,ubicacion,fecha FROM material WHERE estado = 1";
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
            try (ResultSet res = us.executeQuery()) {
                Object datos[]=new Object[8];     
                while(res.next()){
                    for(int i = 0; i < 8 ; i++){
                        datos[i] = res.getObject(i + 1);
                         
                    }
                    modeloTablaNuevoMaterial.addRow(datos);
                }
            }
    
        }   catch(SQLException e){
        System.out.println("Error Mysql");
     
        }
    }
    
     private void setFilasInventarioSelected(String c){//b
    
         if("General".equals(c)){
         setFilasInventario();
         }else{
        try{  
       
            String sql = "SELECT id_material,nombre,categoria,tipoUnidad,cantidadMinima,cantidadStock,ubicacion,fecha FROM material WHERE estado = 1 and categoria = '"+c+"'";
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
            try (ResultSet res = us.executeQuery()) {
                Object datos[]=new Object[8];     
                while(res.next()){
                    for(int i = 0; i < 8 ; i++){
                        datos[i] = res.getObject(i + 1);
                    }
                    modeloTablaNuevoMaterial.addRow(datos);
                }
            }
    
        }   catch(SQLException e){
        System.out.println("Error Mysql");
     
        }
        }
    }
 
     public void limpiarTabla(JTable tabla){//b
        try {
            DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }
     // METODOS PARA VALIDAR LETRAS O NUMEROS 
 void SoloNumeros(java.awt.event.KeyEvent evt){
    char c; 
//capturar el caracter digitado 
 c=evt.getKeyChar(); 
if(c<'0'|| c>'9') 
{ 
 evt.consume();
    }
         }    
    
  void SoloLetras(java.awt.event.KeyEvent evt){
char c; 
    c=evt.getKeyChar(); 
    if(!(c<'0'||c>'9')) 
evt.consume(); 
}

     
     
     // METODOS PARA NUEVO MATERIAL
    void LimpiarNuevoMaterial(){
    jtxtCodigo.setText("");
    jtxtDescrip.setText("");
    jTextCantMin.setText("");
    jComboBoxCategoria.setSelectedItem("Acueducto");
    jComboBoxTipo.setSelectedItem("Bolsa");
    }
    
    void LlenarNuevoMaterial(){
    try{
     String sql = "INSERT INTO material(id_material,nombre,categoria,tipoUnidad,cantidadMinima)" + "values (?,?,?,?,?)";
            PreparedStatement us = con.connect().prepareStatement(sql);
            
            us.setString(1, jtxtCodigo.getText());
            us.setString(2, jtxtDescrip.getText());
            us.setString(3, jComboBoxCategoria.getSelectedItem().toString());
            us.setString(4, jComboBoxTipo.getSelectedItem().toString());
            us.setString(5, jTextCantMin.getText());//no esta retornando a la DB
            int n = us.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Datos Guardados");
            }
    }  catch(SQLException e){
        System.out.println("Error Mysql");
     
        }
    }
    
    //metodo para ingresar proveedores
    
    void ingresaProveedor(){
         int codigoProve= Integer.parseInt(NProveedor01.getText());
    try{
           PreparedStatement us = cn.prepareStatement("INSERT INTO proveedores(empresa,ced_juridica,nombre_contacto,apel_contacto,telefono,direccion,codigo_proveedor,categoria) VALUES (?,?,?,?,?,?,?,?)");
          
 
            
            us.setString(1, jTxtEmpresa01.getText());
            us.setString(2, jtxtCedJ01.getText());
            us.setString(3, jtxtNombre01.getText());
            us.setString(4, jtxtApe01.getText());
            us.setString(5, jtxtTele01.getText());
            
            us.setString(6, jTextAreaDir.getText());  
            us.setInt(7, codigoProve);
            us.setString(8, jTextCategoria.getText());
            us.executeUpdate();
            //MostrarProveedor();  //Tabla retirada

            JOptionPane.showMessageDialog(null, "Datos Guardados");
            
    }  catch(SQLException | HeadlessException e){
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "Datos Ingresados Incorrectamente");
        }
    
    LimpiarNuevoProveedor();
}

void buscarProveedor(int codProve,String nombreProv ){
    DefaultTableModel buscaProveedor= new DefaultTableModel();
    buscaProveedor.addColumn("Cod. Proveedor");
    buscaProveedor.addColumn("Empresa");
    buscaProveedor.addColumn("Ced.Jurídica");
    buscaProveedor.addColumn("Nom. Contacto");
    buscaProveedor.addColumn("Apel. Contacto");
    buscaProveedor.addColumn("Teléfono");
    buscaProveedor.addColumn("Categoría");
    
    jTable21.setModel(buscaProveedor);
    
    String sql="";
    

       if(codProve != 0 && nombreProv.equals("")){
            sql="SELECT * FROM proveedores WHERE codigo_proveedor='"+codProve+"'";
    }else{
           sql="SELECT * FROM proveedores WHERE nombre_contacto='"+nombreProv+"'";
       }
    
    String []datos = new String [8];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                datos[0]=rs.getString(8);
                
                datos[1]=rs.getString(1);
                datos[2]=rs.getString(2);
                datos[3]=rs.getString(3);
                datos[4]=rs.getString(4);
                datos[5]=rs.getString(5);
                datos[6]=rs.getString(7);
                buscaProveedor.addRow(datos);
                jtxtDirecProveBuscar.setText(datos[7]=rs.getString(6));
            }
            jTable21.setModel(buscaProveedor);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
void modificaProveedor(int codModProve,String nombreModProv ){
    
   
    
   
        String sql="";
       if(codModProve != 0 && nombreModProv.equals("")){
            sql="SELECT * FROM proveedores WHERE codigo_proveedor='"+codModProve+"'";
    }else{
           sql="SELECT * FROM proveedores WHERE nombre_contacto='"+nombreModProv+"'";
       }
    
    String []datos = new String [8];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
              

                jModifCodigo.setText(datos[0]=rs.getString(8));
                jModifEmpresa.setText(datos[1]=rs.getString(1));
                jModifCedJur.setText(datos[2]=rs.getString(2));
                jModifNom.setText(datos[3]=rs.getString(3));
                jModifApel.setText(datos[4]=rs.getString(4));

                jModifTel.setText(datos[5]=rs.getString(5));
                jDirecProveModif.setText(datos[6]=rs.getString(6));
                jModifCategoria.setText(datos[7]=rs.getString(7));
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    void LimpiarNuevoProveedor(){
        jTxtEmpresa01.setText("");
        jtxtCedJ01.setText("");
        jtxtNombre01.setText("");
        jtxtApe01.setText("");
        jtxtTele01.setText("");
        NProveedor01.setText("");
        jTextAreaDir.setText("");
        jTextCategoria.setText("");
    }
    void actualizarProveedor(){
        
       int codigoProve= Integer.parseInt(jModifCodigo.getText());
       
      // us = cn.prepareStatement("DELETE * from proveedores WHERE codigo_proveedor=" +codigo);
    try{
        PreparedStatement us = cn.prepareStatement("INSERT INTO proveedores(empresa,ced_juridica,nombre_contacto,apel_contacto,telefono,direccion,codigo_proveedor,categoria) VALUES (?,?,?,?,?,?,?,?)");
          
 
            
            us.setString(1, jModifEmpresa.getText());
            us.setString(2, jModifCedJur.getText());
            us.setString(3, jModifNom.getText());
            us.setString(4, jModifApel.getText());
            us.setString(5, jModifTel.getText());
            
            us.setString(6, jDirecProveModif.getText());  
            us.setInt(7, codigoProve);
            us.setString(8, jModifCategoria.getText());
            us.executeUpdate();
            

            JOptionPane.showMessageDialog(null, "Datos Guardados");
            
    }  catch(SQLException | HeadlessException e){
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "Datos Ingresados Incorrectamente");
        }
    
    //LimpiarNuevoProveedor();
    }
    
    //METODOS PARA REGISTRAR
    
    void BuscarCod(JTextField h,JTextField g){
     try{
            String sql = "SELECT nombre FROM material WHERE id_material="+ g.getText();
            PreparedStatement us = con.connect().prepareStatement(sql);
            ResultSet res = us.executeQuery();
            
           if(res.next()){
           h.setText(res.getString("nombre"));
           }
    
        }   catch(SQLException e){
        System.out.println("Error Mysql");
     
        }
    }
    
    
    void setFilasBuscarEspecifico(String c){
    
    try{  
       
            String sql = "SELECT id_material,nombre,categoria,tipoUnidad,cantidadMinima,cantidadStock,ubicacion,fecha FROM material WHERE estado = 1 and id_material = '"+c+"'";
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
        try (ResultSet res = us.executeQuery()) {
            Object datos[]=new Object[8];
            while(res.next()){
                for(int i = 0; i < 8 ; i++){
                    datos[i] = res.getObject(i + 1);
                }
                modeloTablaBusquedaEspecifica.addRow(datos);
            }
        }
    
        }   catch(SQLException e){
        System.out.println("Error Mysql");
        }
    
    
    }
    void setFilasRegistrar(String c){
    
    try{  
       
            String sql = "SELECT id_material,nombre,categoria,tipoUnidad,cantidadMinima,cantidadStock,ubicacion,fecha FROM material WHERE estado = 1 and id_material = '"+c+"'";
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
        try (ResultSet res = us.executeQuery()) {
            Object datos[]=new Object[8];
            while(res.next()){
                for(int i = 0; i < 8 ; i++){
                    datos[i] = res.getObject(i + 1);
                }
                modeloTablaRegistrar.addRow(datos);
            }
        }
    
        }   catch(SQLException e){
        System.out.println("Error Mysql");
        }
    
    
    }
    void Registrar(){//b
    try{
        String hj = jtxtCodigos.getText();
        String ub = jComboBoxUbicacionRegistro.getSelectedItem().toString();
        String sp =  jSpinnerCantidad.getText();
        String sql=" UPDATE material SET `ubicacion` = '"+ ub +"',`cantidadStock` = '"+ sp +"',`fecha` = '"+ cal.getTime() +"',`estado` = 1  WHERE `material`.`id_material` = '"+hj+"'";
        System.out.println(cal.getTime());
        PreparedStatement us = con.connect().prepareStatement(sql);
       
            int n = us.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Inventario Actualizado");
            }
    }  catch(SQLException e){
        System.out.println("Error Mysql");
     
        }
    }
    
        String obtenerTipo(String Cod){
        String tipo="ERROR";
        String sql="SELECT tipoUnidad from material WHERE id_material ="+Cod;
        try{
        
        
        PreparedStatement us = con.connect().prepareStatement(sql);
        ResultSet rs=us.executeQuery();
        
     while(rs.next()){
      tipo=rs.getString("tipoUnidad");
     }
        return tipo;
       
    }  catch(SQLException e){
        System.out.println("Error Mysql");
     System.out.println(sql);
        }
        return tipo;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        TaPa1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        TaP2 = new javax.swing.JTabbedPane();
        Pan7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        lblNumCompra = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        lblTotalOrde = new javax.swing.JLabel();
        lblEstate = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        lblPro = new javax.swing.JLabel();
        lblNom = new javax.swing.JLabel();
        jtxtNombre = new javax.swing.JFormattedTextField();
        lblApe = new javax.swing.JLabel();
        jtxtApellido = new javax.swing.JFormattedTextField();
        lblCedJuri = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JFormattedTextField();
        lblCon = new javax.swing.JLabel();
        jtxtContacto = new javax.swing.JTextField();
        lblTele = new javax.swing.JLabel();
        jtxtTelefono = new javax.swing.JFormattedTextField();
        lblDir = new javax.swing.JLabel();
        jFormattedTextField69 = new javax.swing.JFormattedTextField();
        jtxtTotal = new javax.swing.JFormattedTextField();
        jtxtNumeroOrden = new javax.swing.JFormattedTextField();
        Pan8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableOrden = new javax.swing.JTable();
        lblOrdenCompra = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        jtxtEstado = new javax.swing.JTextField();
        lblTotalOrdenes = new javax.swing.JLabel();
        jtxtOrdenCOmpre = new javax.swing.JFormattedTextField();
        jtxtDirec = new javax.swing.JFormattedTextField();
        lblDirections = new javax.swing.JLabel();
        lblTelefonos = new javax.swing.JLabel();
        jtxtTel = new javax.swing.JFormattedTextField();
        jtxtCon = new javax.swing.JTextField();
        lblContact = new javax.swing.JLabel();
        lblCed = new javax.swing.JLabel();
        jtxtCed = new javax.swing.JFormattedTextField();
        jtxtName = new javax.swing.JFormattedTextField();
        lblNombres = new javax.swing.JLabel();
        jtxtLast = new javax.swing.JFormattedTextField();
        lblApellidos = new javax.swing.JLabel();
        lblProveedores = new javax.swing.JLabel();
        jFormattedTextField31 = new javax.swing.JFormattedTextField();
        jPanel27 = new javax.swing.JPanel();
        Pan6 = new javax.swing.JPanel();
        btnCargar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnCrear = new javax.swing.JButton();
        lblNames = new javax.swing.JLabel();
        lblCedJu = new javax.swing.JLabel();
        lblContacto = new javax.swing.JLabel();
        lblTel = new javax.swing.JLabel();
        lblDirection = new javax.swing.JLabel();
        jtxtCont = new javax.swing.JTextField();
        lblProveedor = new javax.swing.JLabel();
        lblCant = new javax.swing.JLabel();
        lblTiposU = new javax.swing.JLabel();
        lblDes = new javax.swing.JLabel();
        lblCode = new javax.swing.JLabel();
        lblUnitario = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        lblNum = new javax.swing.JLabel();
        lblProductos = new javax.swing.JLabel();
        lblTotalOr = new javax.swing.JLabel();
        jtxtDesc = new javax.swing.JFormattedTextField();
        jtxtCode = new javax.swing.JFormattedTextField();
        jTxtEmpresa = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jtxtApe = new javax.swing.JFormattedTextField();
        jtxtCedJ = new javax.swing.JFormattedTextField();
        jtxtTele = new javax.swing.JFormattedTextField();
        jtxtDir = new javax.swing.JFormattedTextField();
        jtxtOrden = new javax.swing.JFormattedTextField();
        jtxtTotales = new javax.swing.JFormattedTextField();
        jTextField7 = new javax.swing.JTextField();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        jTxtCate = new javax.swing.JTextField();
        jtxtPrecio = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        TadP4 = new javax.swing.JTabbedPane();
        Pan15 = new javax.swing.JPanel();
        jSeparator9 = new javax.swing.JSeparator();
        lblUserName = new javax.swing.JLabel();
        jtxtNom = new javax.swing.JFormattedTextField();
        jComboBox10 = new javax.swing.JComboBox();
        lblTipos = new javax.swing.JLabel();
        lblFiltro = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        jtxtId = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        lblTipos1 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        lblId1 = new javax.swing.JLabel();
        jtxtId1 = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtUser1 = new javax.swing.JFormattedTextField();
        lblUser1 = new javax.swing.JLabel();
        jtxtContraseña1 = new javax.swing.JPasswordField();
        lblContraseña1 = new javax.swing.JLabel();
        lblRepetir1 = new javax.swing.JLabel();
        jtxtRepetir1 = new javax.swing.JPasswordField();
        lblPuesto1 = new javax.swing.JLabel();
        lblDescri1 = new javax.swing.JLabel();
        jtxtDes1 = new javax.swing.JFormattedTextField();
        jtxtPuesto1 = new javax.swing.JFormattedTextField();
        jtxtApel1 = new javax.swing.JFormattedTextField();
        lblLastName1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        jLabel31 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        Pan13 = new javax.swing.JPanel();
        lblUsers = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        lblUser = new javax.swing.JLabel();
        jtxtUser = new javax.swing.JFormattedTextField();
        lblContraseña = new javax.swing.JLabel();
        jtxtContraseña = new javax.swing.JPasswordField();
        lblRepetir = new javax.swing.JLabel();
        jtxtRepetir = new javax.swing.JPasswordField();
        lblLastName = new javax.swing.JLabel();
        jtxtApel = new javax.swing.JFormattedTextField();
        lblPuesto = new javax.swing.JLabel();
        jtxtPuesto = new javax.swing.JFormattedTextField();
        lblDescri = new javax.swing.JLabel();
        jtxtDes = new javax.swing.JFormattedTextField();
        btnCrearUsuario = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        lblUser2 = new javax.swing.JLabel();
        jtxtUser2 = new javax.swing.JFormattedTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        TadP5 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        TadP7 = new javax.swing.JTabbedPane();
        Pan14 = new javax.swing.JPanel();
        btnMostrar = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable16 = new javax.swing.JTable();
        Pan24 = new javax.swing.JPanel();
        btnMuestra = new javax.swing.JButton();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable17 = new javax.swing.JTable();
        Pan25 = new javax.swing.JPanel();
        btnMuestras = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable18 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        Pan17 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jTaPa9 = new javax.swing.JTabbedPane();
        Pan21 = new javax.swing.JPanel();
        lblSolicitudes = new javax.swing.JLabel();
        btnBusqueda = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jtxtNumSolicitud = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPan19 = new javax.swing.JPanel();
        lblDepartamento = new javax.swing.JLabel();
        lblTipoUnidad = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        btnCarga = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        btnEnviar = new javax.swing.JButton();
        lblname = new javax.swing.JLabel();
        jtxtNmbr = new javax.swing.JFormattedTextField();
        jSpinner1 = new javax.swing.JSpinner();
        jtxtDescripti = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox15 = new javax.swing.JComboBox<>();
        Pan20 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        lblNumero = new javax.swing.JLabel();
        btnBusca = new javax.swing.JButton();
        lblDescripcion = new javax.swing.JLabel();
        btnEnviaCambio = new javax.swing.JButton();
        jtxtNumSoli = new javax.swing.JFormattedTextField();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        TadP3 = new javax.swing.JTabbedPane();
        Pan3 = new javax.swing.JPanel();
        jComboBoxInventario = new javax.swing.JComboBox();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaNuevoMaterial = new javax.swing.JTable();
        jtxtDescription1 = new javax.swing.JFormattedTextField();
        jtxtCodigos2 = new javax.swing.JFormattedTextField();
        lblCodigos = new javax.swing.JLabel();
        lblD = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        Pan18 = new javax.swing.JPanel();
        lblNumOrde = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jButton18 = new javax.swing.JButton();
        jtxtOredesN = new javax.swing.JFormattedTextField();
        Pan22 = new javax.swing.JPanel();
        jComboBox7 = new javax.swing.JComboBox();
        lblSeleccionar = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        lblJusticificacion = new javax.swing.JLabel();
        lblSolicitante = new javax.swing.JLabel();
        btnDescontar = new javax.swing.JButton();
        lblDepar = new javax.swing.JLabel();
        jtxtDepar = new javax.swing.JFormattedTextField();
        jtxtSolicitante = new javax.swing.JFormattedTextField();
        Pan10 = new javax.swing.JPanel();
        lblCodes = new javax.swing.JLabel();
        lblDe = new javax.swing.JLabel();
        lblCanti = new javax.swing.JLabel();
        lblUbicaciones = new javax.swing.JLabel();
        jComboBoxUbicacionRegistro = new javax.swing.JComboBox();
        jButton11 = new javax.swing.JButton();
        jtxtDescription = new javax.swing.JFormattedTextField();
        jtxtCodigos = new javax.swing.JFormattedTextField();
        jSpinnerCantidad = new javax.swing.JTextField();
        Pan11 = new javax.swing.JPanel();
        lblCod = new javax.swing.JLabel();
        lblDescr = new javax.swing.JLabel();
        lblCategorias = new javax.swing.JLabel();
        jComboBoxCategoria = new javax.swing.JComboBox();
        jButton13 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxTipo = new javax.swing.JComboBox();
        jTextCantMin = new javax.swing.JTextField();
        jtxtCodigo = new javax.swing.JFormattedTextField();
        jtxtDescrip = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        lblProveedor01 = new javax.swing.JLabel();
        lblNames01 = new javax.swing.JLabel();
        lblCedJu01 = new javax.swing.JLabel();
        lblContacto01 = new javax.swing.JLabel();
        lblTel01 = new javax.swing.JLabel();
        lblDirection01 = new javax.swing.JLabel();
        jtxtTele01 = new javax.swing.JFormattedTextField();
        jTxtEmpresa01 = new javax.swing.JTextField();
        jtxtCedJ01 = new javax.swing.JFormattedTextField();
        jLabela15 = new javax.swing.JLabel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTextAreaDir = new javax.swing.JTextArea();
        jLabela10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jtxtNombre01 = new javax.swing.JTextField();
        jtxtApe01 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jTextCategoria = new javax.swing.JTextField();
        NProveedor01 = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jBotonBuscaMod = new javax.swing.JButton();
        jModifCodProve = new javax.swing.JTextField();
        jModifNomProve = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jDirecProveModif = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jModifNom = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jModifApel = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jModifEmpresa = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jModifCategoria = new javax.swing.JTextField();
        jModifTel = new javax.swing.JFormattedTextField();
        jModifCedJur = new javax.swing.JFormattedTextField();
        jModifCodigo = new javax.swing.JFormattedTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable21 = new javax.swing.JTable();
        jBuscaCodProve = new javax.swing.JTextField();
        jBuscaNomProve = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jtxtDirecProveBuscar = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        lblBienvenido = new javax.swing.JLabel();
        nombreUsuario = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        lblSan = new javax.swing.JLabel();
        lblHeredia = new javax.swing.JLabel();
        lbl2016 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnAyuda = new javax.swing.JButton();
        lblSistemaBodega = new javax.swing.JLabel();
        lblMunicipalidad = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel30 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Bodega Municipalidad de Flores");
        setBackground(new java.awt.Color(204, 204, 204));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        getContentPane().setLayout(null);

        TaPa1.setBackground(new java.awt.Color(0, 51, 102));
        TaPa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        TaPa1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TaPa1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TaPa1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        TaPa1.setOpaque(true);

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TaP2.setToolTipText("");
        TaP2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Descripción", "Cantidad", "Tipo De Unidad", "Precio unitario ¢", "Precio Total ¢", "Cant Entregada"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        lblNumCompra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNumCompra.setText("Numero de Orden de Compra");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        lblTotalOrde.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalOrde.setText("Total de la Orden ¢");

        lblEstate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEstate.setText("Estado");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pendiente", "Incompleta", "Entregada" }));

        lblPro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPro.setText("Proveedor");

        lblNom.setText("Nombre");

        try {
            jtxtNombre.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNombreActionPerformed(evt);
            }
        });

        lblApe.setText("Apellido");

        try {
            jtxtApellido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtApellidoActionPerformed(evt);
            }
        });

        lblCedJuri.setText("Cedula Jurídica ");

        try {
            jtxtCedula.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("3-AAA-AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblCon.setText("Contacto");

        lblTele.setText("Teléfono ");

        try {
            jtxtTelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblDir.setText("Dirección");

        try {
            jFormattedTextField69.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jtxtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¢###########"))));

        try {
            jtxtNumeroOrden.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNumeroOrden.setToolTipText("6 caracteres alfanumericos");
        jtxtNumeroOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNumeroOrdenActionPerformed(evt);
            }
        });
        jtxtNumeroOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNumeroOrdenKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout Pan7Layout = new javax.swing.GroupLayout(Pan7);
        Pan7.setLayout(Pan7Layout);
        Pan7Layout.setHorizontalGroup(
            Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan7Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 329, Short.MAX_VALUE)
                .addComponent(lblTotalOrde)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(282, 282, 282))
            .addGroup(Pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan7Layout.createSequentialGroup()
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnConsultar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(Pan7Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(lblEstate))
                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(183, 183, 183)
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan7Layout.createSequentialGroup()
                                .addComponent(lblNom)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblApe)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan7Layout.createSequentialGroup()
                                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblCedJuri)
                                    .addComponent(lblCon, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTele, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDir, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(59, 59, 59)
                                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtxtContacto)
                                    .addComponent(jtxtCedula)
                                    .addComponent(jtxtTelefono)
                                    .addComponent(jFormattedTextField69, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(Pan7Layout.createSequentialGroup()
                        .addComponent(lblNumCompra)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtNumeroOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPro, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(333, Short.MAX_VALUE))
        );
        Pan7Layout.setVerticalGroup(
            Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumCompra)
                    .addComponent(lblPro)
                    .addComponent(jtxtNumeroOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNom)
                        .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblApe)))
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblEstate)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(Pan7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCedJuri)
                            .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCon)
                            .addComponent(jtxtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTele)
                            .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDir)
                            .addComponent(jFormattedTextField69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotalOrde)
                        .addComponent(jtxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        TaP2.addTab("Modificar", Pan7);

        TableOrden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Descripción", "Cantidad", "Tipo De Unidad", "Precio unitario ¢", "Precio Total ¢", "Cant Entregada"
            }
        ));
        jScrollPane6.setViewportView(TableOrden);

        lblOrdenCompra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblOrdenCompra.setText("Número de orden de compra");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        lblEstado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEstado.setText("Estado");

        jtxtEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtEstadoActionPerformed(evt);
            }
        });

        lblTotalOrdenes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalOrdenes.setText("Total de la orden ¢");

        try {
            jtxtOrdenCOmpre.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtOrdenCOmpre.setToolTipText("Sólo valores numéricos");
        jtxtOrdenCOmpre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtOrdenCOmpreKeyTyped(evt);
            }
        });

        try {
            jtxtDirec.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblDirections.setText("Dirección");

        lblTelefonos.setText("Teléfono ");

        try {
            jtxtTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblContact.setText("Contacto");

        lblCed.setText("Cédula Jurídica ");

        try {
            jtxtCed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("3-AAA-AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtName.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNameActionPerformed(evt);
            }
        });

        lblNombres.setText("Nombre");

        try {
            jtxtLast.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtLastActionPerformed(evt);
            }
        });

        lblApellidos.setText("Apellido");

        lblProveedores.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProveedores.setText("Proveedor");

        jFormattedTextField31.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¢###########"))));

        javax.swing.GroupLayout Pan8Layout = new javax.swing.GroupLayout(Pan8);
        Pan8.setLayout(Pan8Layout);
        Pan8Layout.setHorizontalGroup(
            Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan8Layout.createSequentialGroup()
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan8Layout.createSequentialGroup()
                                .addComponent(lblOrdenCompra)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtOrdenCOmpre, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan8Layout.createSequentialGroup()
                                    .addComponent(lblEstado)
                                    .addGap(18, 18, 18)
                                    .addComponent(jtxtEstado))
                                .addComponent(btnBuscar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan8Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(lblProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(282, 282, 282))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan8Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblCed)
                                        .addComponent(lblContact, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTelefonos, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDirections, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(lblNombres))
                                .addGap(59, 59, 59)
                                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtCon)
                                        .addComponent(jtxtCed)
                                        .addComponent(jtxtTel)
                                        .addComponent(jtxtDirec, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(Pan8Layout.createSequentialGroup()
                                        .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblApellidos)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtxtLast, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(345, Short.MAX_VALUE))))
                    .addGroup(Pan8Layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(lblTotalOrdenes, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(Pan8Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Pan8Layout.setVerticalGroup(
            Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan8Layout.createSequentialGroup()
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOrdenCompra)
                            .addComponent(jtxtOrdenCOmpre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEstado)
                            .addComponent(jtxtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan8Layout.createSequentialGroup()
                        .addComponent(lblProveedores)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNombres)
                            .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblApellidos)
                            .addComponent(jtxtLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCed)
                            .addComponent(jtxtCed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblContact)
                            .addComponent(jtxtCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTelefonos)
                            .addComponent(jtxtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDirections)
                            .addComponent(jtxtDirec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalOrdenes)
                    .addComponent(jFormattedTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(227, Short.MAX_VALUE))
        );

        TaP2.addTab("Consultar", Pan8);

        btnCargar.setText("Cargar");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        lblNames.setText("Empresa");

        lblCedJu.setText("Cedula Jurídica ");

        lblContacto.setText("Nombre del Contacto");

        lblTel.setText("Teléfono ");

        lblDirection.setText("Dirección");

        jtxtCont.setToolTipText("Digite el nombre del contacto");

        lblProveedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProveedor.setText("Codigo de Proveedor");

        lblCant.setText("Cantidad");

        lblTiposU.setText("Tipo de Unidad");

        lblDes.setText("Descripción");

        lblCode.setText("Código de Material ");

        lblUnitario.setText("Precio Unitario ");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bolsa", "Caja", "Cubeta", "Galón", "1/4 Galón", "Laminas", "Litros", "Kit", "Kilo", "Paquete", "Pares", "Unidad" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        lblNum.setText("Numero de Orden");

        lblProductos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProductos.setText("Productos");

        lblTotalOr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalOr.setText("Total de la Orden ¢");

        try {
            jtxtDesc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*****************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDesc.setToolTipText("Digite el nombre del material");

        try {
            jtxtCode.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCode.setToolTipText("Sólo caracteres numéricos");
        jtxtCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtCodeFocusLost(evt);
            }
        });
        jtxtCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCodeKeyTyped(evt);
            }
        });

        try {
            jTxtEmpresa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTxtEmpresa.setToolTipText("Digite el nombre de la empresa");
        jTxtEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtEmpresaActionPerformed(evt);
            }
        });

        jLabel14.setText("Apellido");

        try {
            jtxtApe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtApe.setToolTipText("Digite el apellido del contacto");
        jtxtApe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtApeActionPerformed(evt);
            }
        });

        try {
            jtxtCedJ.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("3-AAA-AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCedJ.setToolTipText("Sólo valores numéricos");
        jtxtCedJ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCedJKeyTyped(evt);
            }
        });

        try {
            jtxtTele.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtTele.setToolTipText("Digite el número de teléfono del proveedor");
        jtxtTele.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTeleKeyTyped(evt);
            }
        });

        try {
            jtxtDir.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDir.setToolTipText("Digite la dirección del proveedor");

        try {
            jtxtOrden.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jtxtTotales.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¢###########"))));

        jTextField7.setToolTipText("6 caracteres alfanumericos");
        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel34.setText("Categoría");

        jtxtPrecio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("######"))));

        javax.swing.GroupLayout Pan6Layout = new javax.swing.GroupLayout(Pan6);
        Pan6.setLayout(Pan6Layout);
        Pan6Layout.setHorizontalGroup(
            Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan6Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(Pan6Layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(lblTotalOr)
                .addGap(18, 18, 18)
                .addComponent(jtxtTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Pan6Layout.createSequentialGroup()
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblCedJu)
                                        .addComponent(lblDirection, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(lblNames)))
                            .addComponent(lblProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTel)
                            .addComponent(lblContacto))
                        .addGap(18, 18, 18)))
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtCedJ)
                            .addComponent(jtxtTele)
                            .addComponent(jtxtDir)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addComponent(jtxtCont, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtxtApe, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTxtEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtCate, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addComponent(lblNum)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan6Layout.createSequentialGroup()
                                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblUnitario)
                                            .addComponent(lblDes)
                                            .addComponent(lblCant)
                                            .addComponent(lblTiposU))
                                        .addGap(23, 23, 23))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan6Layout.createSequentialGroup()
                                        .addComponent(lblCode)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtDesc)
                                        .addComponent(jtxtPrecio)
                                        .addComponent(jSpinner2)
                                        .addComponent(jtxtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(268, Short.MAX_VALUE))
        );
        Pan6Layout.setVerticalGroup(
            Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProductos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addComponent(lblProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNames))
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtCedJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCedJu))
                        .addGap(9, 9, 9)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jtxtApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblContacto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtTele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDirection))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTxtCate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)))
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNum)
                            .addComponent(jtxtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTiposU))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDes))
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lblCant)
                                .addGap(18, 18, 18)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jtxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblUnitario))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalOr)
                    .addComponent(jtxtTotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pan6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pan6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        TaP2.addTab("Crear", jPanel27);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(TaP2, javax.swing.GroupLayout.PREFERRED_SIZE, 1082, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TaP2)
        );

        TaP2.getAccessibleContext().setAccessibleName("Crear");

        TaPa1.addTab("Órdenes de Compra", jPanel2);

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TadP4.setBackground(new java.awt.Color(0, 51, 102));
        TadP4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        lblUserName.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblUserName.setText("Nombre de Usuario");

        try {
            jtxtNom.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jComboBox10.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));

        lblTipos.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblTipos.setText("Tipo de Usuario");

        lblFiltro.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        lblFiltro.setText("Filtros de Búsqueda ");

        lblId.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblId.setText("Id de Usuario");

        try {
            jtxtId.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jButton1.setText("Deshabilitar Usuario");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/removetheuser_elimina_3541-2.png"))); // NOI18N

        javax.swing.GroupLayout Pan15Layout = new javax.swing.GroupLayout(Pan15);
        Pan15.setLayout(Pan15Layout);
        Pan15Layout.setHorizontalGroup(
            Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan15Layout.createSequentialGroup()
                .addGap(1290, 1290, 1290)
                .addComponent(jSeparator9, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator7))
            .addGroup(Pan15Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblFiltro)
                        .addGroup(Pan15Layout.createSequentialGroup()
                            .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblUserName)
                                .addComponent(lblId))
                            .addGap(18, 18, 18)
                            .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jtxtId)
                                .addComponent(jtxtNom)))
                        .addGroup(Pan15Layout.createSequentialGroup()
                            .addComponent(lblTipos)
                            .addGap(54, 54, 54)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Pan15Layout.setVerticalGroup(
            Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan15Layout.createSequentialGroup()
                        .addComponent(lblFiltro)
                        .addGap(25, 25, 25)
                        .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTipos)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUserName)
                            .addComponent(jtxtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblId)
                            .addComponent(jtxtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(225, 225, 225))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan15Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(30, 30, 30)))
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        TadP4.addTab("Deshabilitar Usuario", Pan15);

        jPanel3.setLayout(null);

        lblTipos1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblTipos1.setText("Tipo de Usuario");
        jPanel3.add(lblTipos1);
        lblTipos1.setBounds(10, 70, 164, 28);

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));
        jPanel3.add(jComboBox12);
        jComboBox12.setBounds(240, 70, 98, 20);

        lblId1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblId1.setText("Id de Usuario");
        jPanel3.add(lblId1);
        lblId1.setBounds(10, 150, 139, 28);

        try {
            jtxtId1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel3.add(jtxtId1);
        jtxtId1.setBounds(240, 150, 167, 20);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel8.setText("Filtro de Busqueda");
        jPanel3.add(jLabel8);
        jLabel8.setBounds(10, 10, 191, 28);

        try {
            jtxtUser1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel3.add(jtxtUser1);
        jtxtUser1.setBounds(240, 110, 294, 20);

        lblUser1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblUser1.setText("Nombre de Usuario");
        jPanel3.add(lblUser1);
        lblUser1.setBounds(10, 110, 200, 28);
        jPanel3.add(jtxtContraseña1);
        jtxtContraseña1.setBounds(240, 230, 294, 20);

        lblContraseña1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblContraseña1.setText("Contraseña");
        jPanel3.add(lblContraseña1);
        lblContraseña1.setBounds(10, 230, 118, 28);

        lblRepetir1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblRepetir1.setText("Repetir Contraseña");
        jPanel3.add(lblRepetir1);
        lblRepetir1.setBounds(10, 280, 202, 28);
        jPanel3.add(jtxtRepetir1);
        jtxtRepetir1.setBounds(240, 280, 294, 20);

        lblPuesto1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblPuesto1.setText("Puesto");
        jPanel3.add(lblPuesto1);
        lblPuesto1.setBounds(10, 370, 68, 28);

        lblDescri1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblDescri1.setText("Descripción ");
        jPanel3.add(lblDescri1);
        lblDescri1.setBounds(10, 410, 128, 28);

        try {
            jtxtDes1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDes1ActionPerformed(evt);
            }
        });
        jPanel3.add(jtxtDes1);
        jtxtDes1.setBounds(240, 420, 167, 20);

        try {
            jtxtPuesto1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtPuesto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtPuesto1ActionPerformed(evt);
            }
        });
        jPanel3.add(jtxtPuesto1);
        jtxtPuesto1.setBounds(240, 370, 167, 20);

        try {
            jtxtApel1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtApel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtApel1ActionPerformed(evt);
            }
        });
        jPanel3.add(jtxtApel1);
        jtxtApel1.setBounds(240, 330, 167, 20);

        lblLastName1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblLastName1.setText("Apellido");
        jPanel3.add(lblLastName1);
        lblLastName1.setBounds(10, 320, 87, 28);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel9.setText("Estatus ");
        jPanel3.add(jLabel9);
        jLabel9.setBounds(10, 470, 81, 28);

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activar", "Desactivar" }));
        jPanel3.add(jComboBox14);
        jComboBox14.setBounds(240, 470, 108, 20);

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jButton2.setText("Guardar Cambios");
        jPanel3.add(jButton2);
        jButton2.setBounds(1110, 823, 352, 66);
        jPanel3.add(jSeparator14);
        jSeparator14.setBounds(10, 200, 520, 30);

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/editar-icono-de-usuario-61596.png"))); // NOI18N
        jPanel3.add(jLabel31);
        jLabel31.setBounds(470, 30, 480, 520);
        jPanel3.add(jSeparator16);
        jSeparator16.setBounds(0, 572, 1290, 10);

        TadP4.addTab("Modificar Usuario", jPanel3);

        Pan13.setLayout(null);

        lblUsers.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblUsers.setText("Tipo de Usuario");
        Pan13.add(lblUsers);
        lblUsers.setBounds(20, 180, 164, 28);

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));
        Pan13.add(jComboBox11);
        jComboBox11.setBounds(240, 180, 150, 20);

        lblUser.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblUser.setText("Nombre");
        Pan13.add(lblUser);
        lblUser.setBounds(20, 80, 83, 28);

        try {
            jtxtUser.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        Pan13.add(jtxtUser);
        jtxtUser.setBounds(240, 80, 200, 20);

        lblContraseña.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblContraseña.setText("Contraseña");
        Pan13.add(lblContraseña);
        lblContraseña.setBounds(20, 220, 118, 28);
        Pan13.add(jtxtContraseña);
        jtxtContraseña.setBounds(240, 220, 200, 20);

        lblRepetir.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblRepetir.setText("Repetir Contrasena");
        Pan13.add(lblRepetir);
        lblRepetir.setBounds(20, 260, 202, 28);
        Pan13.add(jtxtRepetir);
        jtxtRepetir.setBounds(240, 270, 200, 20);

        lblLastName.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblLastName.setText("Apellido");
        Pan13.add(lblLastName);
        lblLastName.setBounds(20, 320, 87, 28);

        try {
            jtxtApel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtApel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtApelActionPerformed(evt);
            }
        });
        Pan13.add(jtxtApel);
        jtxtApel.setBounds(240, 330, 200, 20);

        lblPuesto.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblPuesto.setText("Puesto");
        Pan13.add(lblPuesto);
        lblPuesto.setBounds(20, 380, 68, 28);

        try {
            jtxtPuesto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtPuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtPuestoActionPerformed(evt);
            }
        });
        Pan13.add(jtxtPuesto);
        jtxtPuesto.setBounds(240, 380, 108, 20);

        lblDescri.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblDescri.setText("Descripción ");
        Pan13.add(lblDescri);
        lblDescri.setBounds(20, 440, 128, 28);

        try {
            jtxtDes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDesActionPerformed(evt);
            }
        });
        Pan13.add(jtxtDes);
        jtxtDes.setBounds(240, 440, 108, 20);

        btnCrearUsuario.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        btnCrearUsuario.setText("Crear Usuario");
        btnCrearUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearUsuarioActionPerformed(evt);
            }
        });
        Pan13.add(btnCrearUsuario);
        btnCrearUsuario.setBounds(20, 500, 238, 57);
        Pan13.add(jLabel21);
        jLabel21.setBounds(30, 33, 0, 0);

        lblUser2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblUser2.setText("Nickname");
        Pan13.add(lblUser2);
        lblUser2.setBounds(20, 120, 103, 28);

        try {
            jtxtUser2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        Pan13.add(jtxtUser2);
        jtxtUser2.setBounds(240, 120, 200, 20);

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adduser_añadir_3553-2.png"))); // NOI18N
        Pan13.add(jLabel32);
        jLabel32.setBounds(420, 20, 520, 520);

        jLabel33.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel33.setText("Ingrese los datos del nuevo Usuario");
        Pan13.add(jLabel33);
        jLabel33.setBounds(20, 20, 360, 28);
        Pan13.add(jSeparator15);
        jSeparator15.setBounds(-10, 580, 1290, 20);

        TadP4.addTab("Crear usuario", Pan13);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TadP4)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TadP4)
        );

        TaPa1.addTab("Administración de Usuarios", jPanel4);

        jPanel5.setBackground(new java.awt.Color(153, 204, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TadP5.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        btnMostrar.setText("Mostrar");

        jTable16.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero de OC", "Hora de ingreso", "Persona quien ingresa", "Proveedor", "Estado de la Orden"
            }
        ));
        jScrollPane17.setViewportView(jTable16);

        javax.swing.GroupLayout Pan14Layout = new javax.swing.GroupLayout(Pan14);
        Pan14.setLayout(Pan14Layout);
        Pan14Layout.setHorizontalGroup(
            Pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan14Layout.createSequentialGroup()
                        .addComponent(btnMostrar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE))
                .addContainerGap())
        );
        Pan14Layout.setVerticalGroup(
            Pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMostrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(373, Short.MAX_VALUE))
        );

        TadP7.addTab("Órdenes de compra", Pan14);

        btnMuestra.setText("Mostrar");

        jTable17.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Número de Solicitud", "Hora de ingreso", "Quien ingresa", "Quien autoriza"
            }
        ));
        jScrollPane18.setViewportView(jTable17);

        javax.swing.GroupLayout Pan24Layout = new javax.swing.GroupLayout(Pan24);
        Pan24.setLayout(Pan24Layout);
        Pan24Layout.setHorizontalGroup(
            Pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMuestra)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
        );
        Pan24Layout.setVerticalGroup(
            Pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMuestra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(372, Short.MAX_VALUE))
        );

        TadP7.addTab("Solicitudes de materiales", Pan24);

        btnMuestras.setText("Mostrar Todo");

        jTable18.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Hora de ingreso", "Hora de salida", "Nombre del Usuario", "Cargo"
            }
        ));
        jScrollPane19.setViewportView(jTable18);

        jLabel18.setText("Busqueda específica");

        jLabel19.setText("Identificación:");

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jButton10.setText("Buscar Usuario");

        javax.swing.GroupLayout Pan25Layout = new javax.swing.GroupLayout(Pan25);
        Pan25.setLayout(Pan25Layout);
        Pan25Layout.setHorizontalGroup(
            Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE)
                    .addGroup(Pan25Layout.createSequentialGroup()
                        .addGroup(Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMuestras)
                            .addComponent(jLabel18)
                            .addGroup(Pan25Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(38, 38, 38)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Pan25Layout.setVerticalGroup(
            Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(48, 48, 48)
                .addComponent(btnMuestras)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(252, Short.MAX_VALUE))
        );

        TadP7.addTab("Usuarios Logueados", Pan25);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(TadP7)
                .addGap(29, 29, 29))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(TadP7, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3782, Short.MAX_VALUE))
        );

        TadP5.addTab("Registro  de Activos", jPanel16);

        jTable15.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Usuarios conectados", "Ordenes de compra ingresadas", "Solicitudes de materiales ingresadas", "Modificaciones realizadas"
            }
        ));
        jScrollPane16.setViewportView(jTable15);

        javax.swing.GroupLayout Pan17Layout = new javax.swing.GroupLayout(Pan17);
        Pan17.setLayout(Pan17Layout);
        Pan17Layout.setHorizontalGroup(
            Pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan17Layout.createSequentialGroup()
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 1063, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Pan17Layout.setVerticalGroup(
            Pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan17Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(4156, Short.MAX_VALUE))
        );

        TadP5.addTab("Registro del sistema", Pan17);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TadP5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(TadP5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        TaPa1.addTab("Bitácora", jPanel5);

        jPanel23.setBackground(new java.awt.Color(153, 204, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTaPa9.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        lblSolicitudes.setText("Número de solicitud");

        btnBusqueda.setText("Buscar");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nº  Solicitud", "Còdigo", "Cantidad", "Tipo de unidad", "Descripción", "Solicitante"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        try {
            jtxtNumSolicitud.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNumSolicitud.setToolTipText("Sólo valores numéricos");
        jtxtNumSolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNumSolicitudActionPerformed(evt);
            }
        });
        jtxtNumSolicitud.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNumSolicitudKeyTyped(evt);
            }
        });

        jLabel7.setText("Justificación");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane21.setViewportView(jTextArea3);

        javax.swing.GroupLayout Pan21Layout = new javax.swing.GroupLayout(Pan21);
        Pan21.setLayout(Pan21Layout);
        Pan21Layout.setHorizontalGroup(
            Pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                    .addGroup(Pan21Layout.createSequentialGroup()
                        .addGroup(Pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(Pan21Layout.createSequentialGroup()
                                .addComponent(lblSolicitudes)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtNumSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBusqueda))
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane21))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Pan21Layout.setVerticalGroup(
            Pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan21Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(Pan21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSolicitudes)
                    .addComponent(jtxtNumSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBusqueda))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(442, Short.MAX_VALUE))
        );

        jTaPa9.addTab("Consultar", Pan21);

        jPan19.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        lblDepartamento.setText("Departamento");

        lblTipoUnidad.setText("Tipo de unidad");

        lblDescription.setText("Descripción");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bolsa", "Caja", "Cubeta", "Galón", "1/4 Galón", "Laminas", "Litros", "Kit", "Kilo", "Paquete", "Pares", "Unidad" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nº Solicitud", "Código", "Cantidad", "Tipo de unidad", "Descripción"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        btnCarga.setText("Cargar");
        btnCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargaActionPerformed(evt);
            }
        });

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        lblname.setText("Solicitante");

        try {
            jtxtNmbr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNmbr.setToolTipText("Nombre y apellido de quien solicita el material");
        jtxtNmbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNmbrActionPerformed(evt);
            }
        });
        jtxtNmbr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNmbrKeyTyped(evt);
            }
        });

        try {
            jtxtDescripti.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*************************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDescripti.setToolTipText("25 caracteres maximo");

        jLabel3.setText("Justificación");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setToolTipText("Digite una justificación de porqué necesita el material");
        jScrollPane20.setViewportView(jTextArea2);

        jLabel4.setText("Cantidad");

        jLabel5.setText("Código");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Materiales");

        jTextField2.setToolTipText("Sólo valores numéricos");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alcaldía", "Vicealcaldía", "Proveeduría", "Bodega", "Departamento de TI" }));
        jComboBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPan19Layout = new javax.swing.GroupLayout(jPan19);
        jPan19.setLayout(jPan19Layout);
        jPan19Layout.setHorizontalGroup(
            jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPan19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPan19Layout.createSequentialGroup()
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPan19Layout.createSequentialGroup()
                                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDepartamento)
                                    .addComponent(lblname))
                                .addGap(18, 18, 18)
                                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtxtNmbr)
                                    .addComponent(jComboBox15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPan19Layout.createSequentialGroup()
                                    .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPan19Layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(49, 49, 49))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPan19Layout.createSequentialGroup()
                                            .addComponent(lblTipoUnidad)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox8, 0, 162, Short.MAX_VALUE)
                                        .addComponent(jTextField2))))
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addGroup(jPan19Layout.createSequentialGroup()
                                .addComponent(lblDescription)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtxtDescripti, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(136, 136, 136)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPan19Layout.setVerticalGroup(
            jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPan19Layout.createSequentialGroup()
                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPan19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPan19Layout.createSequentialGroup()
                                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPan19Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblname)
                                            .addComponent(jtxtNmbr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPan19Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPan19Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblDepartamento)
                                            .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPan19Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPan19Layout.createSequentialGroup()
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblTipoUnidad))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblDescription)
                                            .addComponent(jtxtDescripti, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                        .addGroup(jPan19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnEnviar)
                                            .addComponent(btnCarga))
                                        .addGap(9, 9, 9))))))
                    .addGroup(jPan19Layout.createSequentialGroup()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(396, 396, 396))
        );

        jTaPa9.addTab("Crear", jPan19);

        Pan20.setName("Modificar"); // NOI18N

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nº Solicitud", "Codigo", "Cantidad", "Tipo de unidad", "Descripción", "Solicitante"
            }
        ));
        jScrollPane5.setViewportView(jTable5);

        lblNumero.setText("Número de solicitud");

        btnBusca.setText("Buscar");
        btnBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaActionPerformed(evt);
            }
        });

        lblDescripcion.setText("Descripción");

        btnEnviaCambio.setText("Enviar cambios");
        btnEnviaCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviaCambioActionPerformed(evt);
            }
        });

        try {
            jtxtNumSoli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNumSoli.setToolTipText("Sólo valores numéricos");
        jtxtNumSoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNumSoliActionPerformed(evt);
            }
        });
        jtxtNumSoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNumSoliKeyTyped(evt);
            }
        });

        jTextArea8.setColumns(20);
        jTextArea8.setRows(5);
        jScrollPane30.setViewportView(jTextArea8);

        javax.swing.GroupLayout Pan20Layout = new javax.swing.GroupLayout(Pan20);
        Pan20.setLayout(Pan20Layout);
        Pan20Layout.setHorizontalGroup(
            Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan20Layout.createSequentialGroup()
                .addGroup(Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblNumero)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtNumSoli, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan20Layout.createSequentialGroup()
                        .addGap(0, 206, Short.MAX_VALUE)
                        .addGroup(Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan20Layout.createSequentialGroup()
                                .addComponent(lblDescripcion)
                                .addGap(64, 64, 64)
                                .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEnviaCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(270, 270, 270))
        );
        Pan20Layout.setVerticalGroup(
            Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBusca)
                    .addComponent(jtxtNumSoli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Pan20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan20Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lblDescripcion))
                    .addGroup(Pan20Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43)
                .addComponent(btnEnviaCambio)
                .addGap(5, 5, 5))
        );

        jTaPa9.addTab("Modificar", Pan20);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTaPa9)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jTaPa9, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        TaPa1.addTab("Solicitud de materiales", jPanel23);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        TadP3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jComboBoxInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxInventario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "General", "Materiales", "Acueducto", "Limpieza", "Suministros de oficina", "Herramientas", "Material mínimo" }));
        jComboBoxInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxInventarioActionPerformed(evt);
            }
        });

        tablaNuevoMaterial.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tablaNuevoMaterial.setModel(modeloTablaNuevoMaterial);
        jScrollPane10.setViewportView(tablaNuevoMaterial);

        jtxtDescription1.setEditable(false);
        try {
            jtxtDescription1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDescription1.setToolTipText("25 caracteres maximo");
        jtxtDescription1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDescription1ActionPerformed(evt);
            }
        });

        try {
            jtxtCodigos2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#.#.##.##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigos2.setToolTipText("Sólo caracteres numéricos.");

        lblCodigos.setText("Código  ");

        lblD.setText("Nombre");

        jButton9.setText("Buscar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Pan3Layout = new javax.swing.GroupLayout(Pan3);
        Pan3.setLayout(Pan3Layout);
        Pan3Layout.setHorizontalGroup(
            Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jComboBoxInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblD)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtDescription1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCodigos)
                        .addGap(39, 39, 39)
                        .addComponent(jtxtCodigos2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 126, Short.MAX_VALUE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE))
                .addContainerGap())
        );
        Pan3Layout.setVerticalGroup(
            Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jtxtCodigos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCodigos)
                    .addComponent(jtxtDescription1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblD)
                    .addComponent(jComboBoxInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        jComboBoxInventario.getAccessibleContext().setAccessibleName("");

        TadP3.addTab("Inventario General", Pan3);

        jTabbedPane6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblNumOrde.setText("Numero de orden");

        jButton20.setText("Buscar");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código ", "Tipo de Unidad", "Descripción", "Cantidad", "Ubicación", "Completo", "Cant Recibida"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable11);

        jButton18.setText("Registrar");

        try {
            jtxtOredesN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtOredesN.setToolTipText("Sólo caracteres numéricos.");
        jtxtOredesN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtOredesNActionPerformed(evt);
            }
        });
        jtxtOredesN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtOredesNKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout Pan18Layout = new javax.swing.GroupLayout(Pan18);
        Pan18.setLayout(Pan18Layout);
        Pan18Layout.setHorizontalGroup(
            Pan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Pan18Layout.createSequentialGroup()
                        .addComponent(lblNumOrde)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtOredesN, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(744, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan18Layout.createSequentialGroup()
                .addContainerGap(507, Short.MAX_VALUE)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 925, Short.MAX_VALUE)
        );
        Pan18Layout.setVerticalGroup(
            Pan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumOrde)
                    .addComponent(jtxtOredesN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton20)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(211, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("Con Orden de compra", Pan18);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solicitudes Pendientes" }));

        lblSeleccionar.setText("Seleccionar solicitud");

        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Tipo de Unidad", "Descripción", "Cantidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTable12);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane13.setViewportView(jTextArea1);

        lblJusticificacion.setText("Justificación de la Solicitud ");

        lblSolicitante.setText("Solicitante");

        btnDescontar.setText("Descontar del Inventario");

        lblDepar.setText("Departamento");

        jtxtDepar.setEditable(false);
        try {
            jtxtDepar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDepar.setToolTipText("Nombre del departamento.");

        jtxtSolicitante.setEditable(false);
        try {
            jtxtSolicitante.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtSolicitante.setToolTipText("Nombre del Solicitante.");
        jtxtSolicitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtSolicitanteActionPerformed(evt);
            }
        });
        jtxtSolicitante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtSolicitanteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout Pan22Layout = new javax.swing.GroupLayout(Pan22);
        Pan22.setLayout(Pan22Layout);
        Pan22Layout.setHorizontalGroup(
            Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan22Layout.createSequentialGroup()
                .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan22Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblSeleccionar))
                    .addGroup(Pan22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblJusticificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(Pan22Layout.createSequentialGroup()
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDepar)
                            .addComponent(lblSolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtDepar, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jtxtSolicitante)))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(304, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan22Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(265, 265, 265))
            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        Pan22Layout.setVerticalGroup(
            Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSeleccionar)
                    .addGroup(Pan22Layout.createSequentialGroup()
                        .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDepar)
                            .addComponent(jtxtDepar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSolicitante)
                            .addComponent(jtxtSolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(Pan22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan22Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblJusticificacion))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("Con Solicitud de Materiales", Pan22);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane6)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane6)
        );

        TadP3.addTab("Entradas y Salidas", jPanel12);

        lblCodes.setText("Código  ");

        lblDe.setText("Nombre");

        lblCanti.setText("Cantidad");

        lblUbicaciones.setText("Ubicación");

        jComboBoxUbicacionRegistro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A1", "A2", "A3", "B1", "B2", "B3" }));
        jComboBoxUbicacionRegistro.setToolTipText("");
        jComboBoxUbicacionRegistro.setName(""); // NOI18N

        jButton11.setText("Cargar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jtxtDescription.setEditable(false);
        try {
            jtxtDescription.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDescription.setToolTipText("25 caracteres maximo");
        jtxtDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDescriptionActionPerformed(evt);
            }
        });

        try {
            jtxtCodigos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#.#.##.##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigos.setToolTipText("Sólo caracteres numéricos. ");

        jSpinnerCantidad.setToolTipText("Sólo caracteres numéricos.");
        jSpinnerCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jSpinnerCantidadKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout Pan10Layout = new javax.swing.GroupLayout(Pan10);
        Pan10.setLayout(Pan10Layout);
        Pan10Layout.setHorizontalGroup(
            Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Pan10Layout.createSequentialGroup()
                        .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodes, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCanti)
                            .addComponent(lblDe)
                            .addComponent(lblUbicaciones))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxUbicacionRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCodigos, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(650, Short.MAX_VALUE))
        );
        Pan10Layout.setVerticalGroup(
            Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCodigos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCanti)
                    .addComponent(jSpinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDe)
                    .addComponent(jtxtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxUbicacionRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUbicaciones))
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(406, Short.MAX_VALUE))
        );

        jComboBoxUbicacionRegistro.getAccessibleContext().setAccessibleName("");

        TadP3.addTab("Registro", Pan10);

        lblCod.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblCod.setText("Código ");

        lblDescr.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblDescr.setText("Nombre ");

        lblCategorias.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblCategorias.setText("Categoría");

        jComboBoxCategoria.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Acueducto", "Materiales", "Limpieza", "Suministros de oficina", "Herramientas" }));
        jComboBoxCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCategoriaActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton13.setText("Cargar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setText("Cantidad Mínima");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel20.setText("Tipo de unidad");

        jComboBoxTipo.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bolsa", "Caja", "Cubeta", "Galón", "1/4 Galón", "Laminas", "Litros", "Kit", "Kilo", "Paquete", "Pares", "Unidad" }));
        jComboBoxTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoActionPerformed(evt);
            }
        });

        jTextCantMin.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jTextCantMin.setToolTipText("Sólo caracteres numéricos. ");
        jTextCantMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextCantMinKeyTyped(evt);
            }
        });

        try {
            jtxtCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#.#.##.##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigo.setToolTipText("Sólo caracteres numéricos.");
        jtxtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCodigoKeyTyped(evt);
            }
        });

        jtxtDescrip.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jtxtDescrip.setToolTipText("Digite el nombre del material.");

        javax.swing.GroupLayout Pan11Layout = new javax.swing.GroupLayout(Pan11);
        Pan11.setLayout(Pan11Layout);
        Pan11Layout.setHorizontalGroup(
            Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan11Layout.createSequentialGroup()
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescr)
                            .addComponent(lblCod))
                        .addGap(32, 32, 32)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan11Layout.createSequentialGroup()
                                .addComponent(jtxtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 296, Short.MAX_VALUE))
                            .addComponent(jtxtDescrip))
                        .addGap(35, 35, 35)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxTipo, 0, 123, Short.MAX_VALUE)
                            .addComponent(jTextCantMin))
                        .addGap(112, 112, 112))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan11Layout.createSequentialGroup()
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pan11Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan11Layout.createSequentialGroup()
                                .addComponent(lblCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxCategoria, 0, 426, Short.MAX_VALUE)))
                        .addGap(398, 398, 398))))
        );
        Pan11Layout.setVerticalGroup(
            Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan11Layout.createSequentialGroup()
                .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan11Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblCod)
                        .addGap(25, 25, 25)
                        .addComponent(lblDescr))
                    .addGroup(Pan11Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jTextCantMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtxtCodigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDescrip, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCategorias))
                .addGap(57, 57, 57)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(395, Short.MAX_VALUE))
        );

        TadP3.addTab("Nuevo Material", Pan11);

        jPanel1.add(TadP3, java.awt.BorderLayout.CENTER);

        TaPa1.addTab("Inventario", jPanel1);

        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jPanel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblProveedor01.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProveedor01.setText("Proveedor");

        lblNames01.setText("Empresa");

        lblCedJu01.setText("Cédula Jurídica ");

        lblContacto01.setText("Nombre del Contacto");

        lblTel01.setText("Teléfono ");

        lblDirection01.setText("Dirección");

        try {
            jtxtTele01.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtTele01.setToolTipText("Digite el teléfono del proveedor");
        jtxtTele01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTele01KeyTyped(evt);
            }
        });

        jTxtEmpresa01.setToolTipText("Digite el nombre de la empresa del proveedor");
        jTxtEmpresa01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtEmpresa01ActionPerformed(evt);
            }
        });

        try {
            jtxtCedJ01.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("3-AAA-AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCedJ01.setToolTipText("Sólo valores numéricos");
        jtxtCedJ01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCedJ01ActionPerformed(evt);
            }
        });
        jtxtCedJ01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCedJ01KeyTyped(evt);
            }
        });

        jLabela15.setText("Apellido");

        jTextAreaDir.setColumns(20);
        jTextAreaDir.setRows(5);
        jTextAreaDir.setToolTipText("Digite la dirección del proveedor");
        jScrollPane23.setViewportView(jTextAreaDir);

        jLabela10.setText("Código de proveedor");

        jButton3.setText("Crear Proveedor");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jtxtNombre01.setToolTipText("Digite el nombre del contacto");

        jtxtApe01.setToolTipText("Digite el apellido del contacto");

        jLabel28.setText("Categoría");

        jTextCategoria.setToolTipText("Por ejemplo: Herramientas,acueductos,limpieza, etc");

        try {
            NProveedor01.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        NProveedor01.setToolTipText("Sólo valores numéricos");
        NProveedor01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NProveedor01KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblNames01)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblProveedor01, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(119, 119, 119)
                                        .addComponent(jTxtEmpresa01, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblContacto01)
                                    .addComponent(lblCedJu01)
                                    .addComponent(jLabel28)
                                    .addComponent(lblTel01))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                            .addComponent(jtxtNombre01, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabela15)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jtxtApe01))
                                        .addComponent(jtxtCedJ01, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                        .addComponent(jtxtTele01, javax.swing.GroupLayout.Alignment.LEADING)))))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDirection01)
                            .addComponent(jLabela10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(NProveedor01))))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblProveedor01)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabela10)
                                    .addComponent(NProveedor01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(lblDirection01))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblContacto01))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(lblNames01)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTxtEmpresa01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jTextCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtCedJ01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCedJu01, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtNombre01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabela15)
                            .addComponent(jtxtApe01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtTele01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTel01))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(382, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crear", jPanel7);

        jLabel13.setText("Codigo de Proveedor");

        jLabel15.setText("Nombre de Proveedor");

        jBotonBuscaMod.setText("Buscar");
        jBotonBuscaMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBotonBuscaModActionPerformed(evt);
            }
        });

        jModifCodProve.setToolTipText("Sólo valores numéricos");
        jModifCodProve.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jModifCodProveKeyTyped(evt);
            }
        });

        jModifNomProve.setToolTipText("Digite el nombre del proveedor");
        jModifNomProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jModifNomProveActionPerformed(evt);
            }
        });

        jLabel17.setText("Dirección");

        jDirecProveModif.setColumns(20);
        jDirecProveModif.setRows(5);
        jDirecProveModif.setToolTipText("Digite la dirección del proveedor");
        jScrollPane28.setViewportView(jDirecProveModif);

        jButton8.setText("Modificar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel22.setText("Nuevo Codigo de Proveedor");

        jLabel23.setText("Empresa");

        jLabel24.setText("Cedula Jurídica");

        jLabel25.setText("Nuevo Nombre del Contacto");

        jModifNom.setToolTipText("Digite el nombre del contacto");
        jModifNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jModifNomActionPerformed(evt);
            }
        });

        jLabel26.setText("Apellido");

        jModifApel.setToolTipText("Digite el apellido del contacto");

        jLabel27.setText("Teléfono");

        jModifEmpresa.setToolTipText("Digite el nombre del proveedor");

        jLabel29.setText("Categoría");

        jModifCategoria.setToolTipText("Por ejemplo: Herramientas,acueductos,limpieza, etc");

        try {
            jModifTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jModifTel.setText("    -    ");

        try {
            jModifCedJur.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("3-AAA-AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jModifCedJur.setToolTipText("Sólo valores numéricos");
        jModifCedJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jModifCedJurActionPerformed(evt);
            }
        });
        jModifCedJur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jModifCedJurKeyTyped(evt);
            }
        });

        try {
            jModifCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jModifCodigo.setToolTipText("Sólo valores numéricos");
        jModifCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jModifCodigoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(37, 37, 37)
                                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(43, 43, 43)
                                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jModifCodProve, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jModifNomProve, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jBotonBuscaMod, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel9Layout.createSequentialGroup()
                                            .addGap(60, 60, 60)
                                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel22)
                                                .addComponent(jLabel23)
                                                .addComponent(jLabel24)
                                                .addComponent(jLabel27)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel25)))
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addComponent(jLabel29)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jModifCategoria)
                                    .addComponent(jModifEmpresa)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jModifNom, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jModifApel, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                                    .addComponent(jModifTel)
                                    .addComponent(jModifCedJur, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                    .addComponent(jModifCodigo)))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(205, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jModifCodProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jModifCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jModifNomProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jModifNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(jModifApel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel29))
                    .addComponent(jModifCategoria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jModifEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jBotonBuscaMod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jModifCedJur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jModifTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(338, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Modificar", jPanel9);

        jLabel10.setText("Codigo de Proveedor");

        jLabel11.setText("Nombre de Proveedor");

        jTable21.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        jScrollPane26.setViewportView(jTable21);

        jBuscaCodProve.setToolTipText("Sólo valores numéricos");
        jBuscaCodProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBuscaCodProveActionPerformed(evt);
            }
        });
        jBuscaCodProve.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jBuscaCodProveKeyTyped(evt);
            }
        });

        jBuscaNomProve.setToolTipText("Digite el nombre del proveedor para realizar la búsqueda");

        jButton4.setText("Buscar ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel12.setText("Dirección");

        jtxtDirecProveBuscar.setColumns(20);
        jtxtDirecProveBuscar.setRows(5);
        jScrollPane27.setViewportView(jtxtDirecProveBuscar);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBuscaNomProve, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(jBuscaCodProve))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane27)
                        .addGap(24, 24, 24))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jBuscaCodProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jBuscaNomProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel12))
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(355, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consultar", jPanel8);

        TaPa1.addTab("Cartel de Proveedores", jTabbedPane1);

        getContentPane().add(TaPa1);
        TaPa1.setBounds(20, 110, 1160, 710);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshot_1.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(1190, 70, 155, 173);

        lblBienvenido.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblBienvenido.setText("Bienvenido");
        getContentPane().add(lblBienvenido);
        lblBienvenido.setBounds(1190, 290, 61, 20);

        nombreUsuario.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        nombreUsuario.setText("Usuario");
        getContentPane().add(nombreUsuario);
        nombreUsuario.setBounds(1290, 290, 43, 20);

        btnCerrarSesion.setText("Cerrar sesión ");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrarSesion);
        btnCerrarSesion.setBounds(1190, 340, 148, 40);

        lblSan.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblSan.setText("San Joaquín  de Flores ");
        getContentPane().add(lblSan);
        lblSan.setBounds(1190, 460, 140, 20);

        lblHeredia.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblHeredia.setText("Heredia");
        getContentPane().add(lblHeredia);
        lblHeredia.setBounds(1240, 500, 46, 20);

        lbl2016.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lbl2016.setText("2016");
        getContentPane().add(lbl2016);
        lbl2016.setBounds(1250, 530, 28, 20);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(1190, 440, 148, 20);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(1190, 260, 146, 13);

        btnAyuda.setText("Ayuda");
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });
        getContentPane().add(btnAyuda);
        btnAyuda.setBounds(1190, 400, 148, 30);

        lblSistemaBodega.setBackground(new java.awt.Color(255, 255, 255));
        lblSistemaBodega.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        lblSistemaBodega.setForeground(new java.awt.Color(255, 255, 255));
        lblSistemaBodega.setText("Sistema de Bodega Municipalidad de Flores");
        getContentPane().add(lblSistemaBodega);
        lblSistemaBodega.setBounds(430, 50, 660, 36);

        lblMunicipalidad.setBackground(new java.awt.Color(255, 255, 255));
        lblMunicipalidad.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        lblMunicipalidad.setForeground(new java.awt.Color(255, 255, 255));
        lblMunicipalidad.setText("Municipalidad de Flores");
        getContentPane().add(lblMunicipalidad);
        lblMunicipalidad.setBounds(580, 10, 410, 42);
        getContentPane().add(jSeparator4);
        jSeparator4.setBounds(1190, 320, 146, 20);

        jSeparator8.setForeground(new java.awt.Color(0, 51, 102));
        getContentPane().add(jSeparator8);
        jSeparator8.setBounds(10, 90, 1160, 20);

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BG.jpg"))); // NOI18N
        jLabel30.setText("jLabel30");
        getContentPane().add(jLabel30);
        jLabel30.setBounds(0, 0, 1560, 820);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        Point point = MouseInfo.getPointerInfo().getLocation() ; 
        setLocation(point.x - x, point.y - y)  ;
    }//GEN-LAST:event_formMouseDragged

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
    new Login().setVisible(true);
        this.setVisible(false);       
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAyudaActionPerformed
    new Ayuda().setVisible(true);
        this.setVisible(true);    
        
// TODO add your handling code here:
    }//GEN-LAST:event_btnAyudaActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void btnCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargaActionPerformed

    private void jtxtNumSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNumSolicitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNumSolicitudActionPerformed

    private void jtxtNmbrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNmbrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNmbrActionPerformed

    private void jtxtNumSoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNumSoliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNumSoliActionPerformed

    private void btnCrearUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCrearUsuarioActionPerformed

    private void jtxtDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDesActionPerformed

    private void jtxtPuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtPuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtPuestoActionPerformed

    private void jtxtApelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtApelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtApelActionPerformed

    private void jtxtDes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDes1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDes1ActionPerformed

    private void jtxtPuesto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtPuesto1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtPuesto1ActionPerformed

    private void jtxtApel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtApel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtApel1ActionPerformed

    private void jtxtApeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtApeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtApeActionPerformed

    private void jTxtEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtEmpresaActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        // int numero_orden=insertarOrden(datos, Integer.parseInt(jtxtOrden.getText()));
int numero_orden = Integer.parseInt(jtxtOrden.getText());
        	for(int row = 0;row < modeloTablaCrearOrden.getRowCount();row++) {
String datos[]=new String[9];

	    for(int col = 0;col < modeloTablaCrearOrden.getColumnCount();col++) {

	        
                datos[col]=modeloTablaCrearOrden.getValueAt(row, col).toString();
                System.out.println(datos[col]);
               
	    }
enviarOrden(datos,numero_orden);
insertarOrden(datos,numero_orden );
	}
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed

int precio = Integer.parseInt(jtxtPrecio.getText());
int cant = Integer.parseInt(jSpinner2.getValue().toString());
String codProveedor = jTextField7.getText();
int numeroOrden = Integer.parseInt(jtxtOrden.getText());
jtxtOrden.setEditable(false);
int codMaterial = Integer.parseInt(jtxtCode.getText());
int precioTotal = precio * cant;
String tipo = jComboBox5.getSelectedItem().toString();
String desc = jtxtDesc.getText();




modeloTablaCrearOrden.addRow(new Object[]{now(), codProveedor, cant, tipo, codMaterial, desc, numeroOrden, precio, precioTotal});
jTable1.setModel(modeloTablaCrearOrden);


    }//GEN-LAST:event_btnCargarActionPerformed

    private void jtxtLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtLastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtLastActionPerformed

    private void jtxtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameActionPerformed

    private void jtxtEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtEstadoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
DefaultTableModel modelo = new DefaultTableModel();
modelo.addColumn("Codigo");
modelo.addColumn("Descripcion");
modelo.addColumn("Cantidad");
modelo.addColumn("Tipo de unidad");
modelo.addColumn("Precio unitario");
modelo.addColumn("Precio Total");
modelo.addColumn("Cant entregada");
try{
Statement s = con.connect().createStatement();
ResultSet rs = s.executeQuery("select * from Orden_Compra where numero_orden = "+jtxtOrdenCOmpre.getText());
while (rs.next())
{
   // Se crea un array que será una de las filas de la tabla.
   Object [] fila = new Object[7]; 

   // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
   for (int i=0;i<7;i++)
      fila[i] = rs.getObject(i+1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.

   // Se añade al modelo la fila completa.
   modelo.addRow(fila);
}

}
catch(SQLException e){
 System.out.println(e.getMessage());
}        
TableOrden.setModel(modelo);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jtxtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtApellidoActionPerformed

    private void jtxtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNombreActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void jBuscaCodProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBuscaCodProveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBuscaCodProveActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jtxtNumeroOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNumeroOrdenActionPerformed
      
    }//GEN-LAST:event_jtxtNumeroOrdenActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jTxtEmpresa01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtEmpresa01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtEmpresa01ActionPerformed
//BotonInsertar Proveedor
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
ingresaProveedor();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtxtCedJ01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCedJ01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedJ01ActionPerformed
//Boton Buscar Proveedores
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
   
    int codProve = 0;
    String nomProve = "";
    if(!jBuscaCodProve.getText().equals("")){
    codProve= Integer.parseInt(jBuscaCodProve.getText());
    }else{
        codProve = 0;
    }
    if(!jBuscaNomProve.getText().equals("")){
     nomProve = jBuscaNomProve.getText();
    }else{
     nomProve = "";
    }
    
     try{  
     if(codProve != 0 && nomProve.equals("")){
        buscarProveedor(codProve, "");
        jBuscaCodProve.setText("");
        }else{
            if(codProve == 0 && !nomProve.equals("")){
            buscarProveedor(0, nomProve);
            jBuscaNomProve.setText("");
            }else{
                JOptionPane.showMessageDialog(null, "Proveedor no encontrado");
            }
        }
     }
     catch(Exception e){
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "Error desconocido"); 
             }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jModifNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jModifNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jModifNomActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
        codProve = Integer.parseInt(jModifCodProve.getText());
        
       
        try {
            String sql="DELETE FROM proveedores WHERE codigo_proveedor=" +codProve;
            PreparedStatement us = con.connect().prepareStatement(sql);
            System.out.println(sql);
            int n = us.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Actualizando datos");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        actualizarProveedor();

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jModifNomProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jModifNomProveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jModifNomProveActionPerformed

    private void jBotonBuscaModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBotonBuscaModActionPerformed
        //Boton buscar Para modificar Proveedores

        int codModProve = 0;
        String nomModProve = "";

        if(!jModifCodProve.getText().equals("")){
            codModProve= Integer.parseInt(jModifCodProve.getText());
        }else{
            codModProve = 0;
        }
        if(!jModifNomProve.getText().equals("")){
            nomModProve = jModifNomProve.getText();
        }else{
            nomModProve = "";
        }

        try{
            if(codModProve != 0 && nomModProve.equals("")){
                modificaProveedor(codModProve, "");
                /*                jModifCodProve.setEditable(false);
                jModifNomProve.setEditable(false);*/
            }else{
                if(codModProve == 0 && !nomModProve.equals("")){
                    modificaProveedor(0, nomModProve);
                    /*                    jModifNomProve.setEditable(false);
                    jModifCodProve.setEditable(false);*/
                }else{
                    JOptionPane.showMessageDialog(null, "Proveedor no encontrado");
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error desconocido");
        }
    }//GEN-LAST:event_jBotonBuscaModActionPerformed

    private void btnBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscaActionPerformed

    private void btnEnviaCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviaCambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnviaCambioActionPerformed

    private void jtxtNumeroOrdenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNumeroOrdenKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNumeroOrdenKeyTyped

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
                // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        
        SoloNumeros(evt);  
        char teclaPress = evt.getKeyChar();
        if(teclaPress==KeyEvent.VK_ENTER){
            int codProvee = Integer.parseInt(jTextField7.getText()); 
            mostrarProveedor(codProvee);
        }
        
    }//GEN-LAST:event_jTextField7KeyTyped

    private void jtxtTeleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTeleKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTeleKeyTyped

    private void jtxtCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCodeKeyTyped
       
        SoloNumeros(evt);   
        char teclaPress = evt.getKeyChar();
        if(teclaPress==KeyEvent.VK_ENTER){
            int codMater = Integer.parseInt(jtxtCode.getText()); 
            mostrarMaterial(codMater);
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodeKeyTyped

    private void jtxtCedJKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedJKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedJKeyTyped

    private void jtxtOrdenCOmpreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtOrdenCOmpreKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtOrdenCOmpreKeyTyped

    private void jtxtNumSolicitudKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNumSolicitudKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNumSolicitudKeyTyped

    private void jtxtNmbrKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNmbrKeyTyped
                // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNmbrKeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jtxtNumSoliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNumSoliKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNumSoliKeyTyped

    private void jtxtCedJ01KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedJ01KeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedJ01KeyTyped

    private void jtxtTele01KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTele01KeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTele01KeyTyped

    private void NProveedor01KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NProveedor01KeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_NProveedor01KeyTyped

    private void jModifCodProveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jModifCodProveKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jModifCodProveKeyTyped

    private void jBuscaCodProveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBuscaCodProveKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jBuscaCodProveKeyTyped

    private void jtxtCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCodigoKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodigoKeyTyped

    private void jTextCantMinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextCantMinKeyTyped
        SoloNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jTextCantMinKeyTyped

    private void jComboBoxTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxTipoActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String texto=jTextCantMin.getText();
        String texto1=jtxtCodigo.getText();
        String texto2=jtxtDescrip.getText();
        texto=texto.replaceAll(" ", "");
        texto1=texto1.replaceAll(" ", "");
        texto1=texto2.replaceAll(" ", "");
        if(texto.length()==0 || texto1.length()==0 || texto2.length()==0  ){

            JOptionPane.showMessageDialog(null, "hay un espacio en blanco");
        }
        else
        {
            LlenarNuevoMaterial();
            LimpiarNuevoMaterial();

        }

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jComboBoxCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCategoriaActionPerformed

    private void jSpinnerCantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSpinnerCantidadKeyTyped
        // TODO add your handling code here:
        SoloNumeros(evt);
    }//GEN-LAST:event_jSpinnerCantidadKeyTyped

    private void jtxtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDescriptionActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        String texto=jtxtCodigos.getText();
        String texto1=jSpinnerCantidad.getText(); // revisar que esto este bien
        texto=texto.replaceAll(" ", "");
        texto1=texto1.replaceAll(" ", "");
        if(texto.length()==0 || texto1.length()==0 ){

            JOptionPane.showMessageDialog(null, "hay un espacio en blanco");
        }
        else
        {
            Registrar();
        }

        // limpiarTabla(tablaNuevoMaterial);   ver que hacemos con este metodooo

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jtxtSolicitanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtSolicitanteKeyTyped
        SoloLetras(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtSolicitanteKeyTyped

    private void jtxtSolicitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtSolicitanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtSolicitanteActionPerformed

    private void jtxtOredesNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtOredesNKeyTyped
        // TODO add your handling code here:
        SoloNumeros(evt);
    }//GEN-LAST:event_jtxtOredesNKeyTyped

    private void jtxtOredesNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtOredesNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtOredesNActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:

        String texto=jtxtOredesN.getText();
        texto=texto.replaceAll(" ", "");

        if(texto.length()==0){

            JOptionPane.showMessageDialog(null, "Digite el Numero de Orden a Buscar ");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "ACA VA LA SENTENCIA DE SQL PARA BUSCAR");
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //limpiarTabla(tablaBusquedaEspecifica); mejor no limpiarla porque talvez la persona quiera ver las busquedas recientes o asi en la sesion...
        String cod = jtxtCodigos2.getText();
        setFilasBuscarEspecifico(cod);
        BuscarCod(jtxtDescription1,jtxtCodigos2);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jtxtDescription1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDescription1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDescription1ActionPerformed

    private void jComboBoxInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxInventarioActionPerformed

    private void jModifCedJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jModifCedJurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jModifCedJurActionPerformed

    private void jModifCedJurKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jModifCedJurKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jModifCedJurKeyTyped

    private void jModifCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jModifCodigoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jModifCodigoKeyTyped

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
        int codProvee = Integer.parseInt(jTextField7.getText()); 
        mostrarProveedor(codProvee);
    }//GEN-LAST:event_jTextField7FocusLost

    private void jtxtCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCodeFocusLost
                                                 
        int codMater = Integer.parseInt(jtxtCode.getText()); 
        mostrarMaterial(codMater);
    }//GEN-LAST:event_jtxtCodeFocusLost

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
               UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");   
                               new Principal().setVisible(true);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
     
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField NProveedor01;
    private javax.swing.JPanel Pan10;
    private javax.swing.JPanel Pan11;
    private javax.swing.JPanel Pan13;
    private javax.swing.JPanel Pan14;
    private javax.swing.JPanel Pan15;
    private javax.swing.JPanel Pan17;
    private javax.swing.JPanel Pan18;
    private javax.swing.JPanel Pan20;
    private javax.swing.JPanel Pan21;
    private javax.swing.JPanel Pan22;
    private javax.swing.JPanel Pan24;
    private javax.swing.JPanel Pan25;
    private javax.swing.JPanel Pan3;
    private javax.swing.JPanel Pan6;
    private javax.swing.JPanel Pan7;
    private javax.swing.JPanel Pan8;
    private javax.swing.JTabbedPane TaP2;
    private javax.swing.JTabbedPane TaPa1;
    private javax.swing.JTable TableOrden;
    private javax.swing.JTabbedPane TadP3;
    private javax.swing.JTabbedPane TadP4;
    private javax.swing.JTabbedPane TadP5;
    private javax.swing.JTabbedPane TadP7;
    private javax.swing.JButton btnAyuda;
    private javax.swing.JButton btnBusca;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnCarga;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnCrearUsuario;
    private javax.swing.JButton btnDescontar;
    private javax.swing.JButton btnEnviaCambio;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnMuestra;
    private javax.swing.JButton btnMuestras;
    private javax.swing.JButton jBotonBuscaMod;
    private javax.swing.JTextField jBuscaCodProve;
    private javax.swing.JTextField jBuscaNomProve;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBoxCategoria;
    private javax.swing.JComboBox jComboBoxInventario;
    private javax.swing.JComboBox jComboBoxTipo;
    private javax.swing.JComboBox jComboBoxUbicacionRegistro;
    private javax.swing.JTextArea jDirecProveModif;
    private javax.swing.JFormattedTextField jFormattedTextField31;
    private javax.swing.JFormattedTextField jFormattedTextField69;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabela10;
    private javax.swing.JLabel jLabela15;
    private javax.swing.JTextField jModifApel;
    private javax.swing.JTextField jModifCategoria;
    private javax.swing.JFormattedTextField jModifCedJur;
    private javax.swing.JTextField jModifCodProve;
    private javax.swing.JFormattedTextField jModifCodigo;
    private javax.swing.JTextField jModifEmpresa;
    private javax.swing.JTextField jModifNom;
    private javax.swing.JTextField jModifNomProve;
    private javax.swing.JFormattedTextField jModifTel;
    private javax.swing.JPanel jPan19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextField jSpinnerCantidad;
    private javax.swing.JTabbedPane jTaPa9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable17;
    private javax.swing.JTable jTable18;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable21;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextArea jTextAreaDir;
    private javax.swing.JTextField jTextCantMin;
    private javax.swing.JTextField jTextCategoria;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTxtCate;
    private javax.swing.JFormattedTextField jTxtEmpresa;
    private javax.swing.JTextField jTxtEmpresa01;
    private javax.swing.JFormattedTextField jtxtApe;
    private javax.swing.JTextField jtxtApe01;
    private javax.swing.JFormattedTextField jtxtApel;
    private javax.swing.JFormattedTextField jtxtApel1;
    private javax.swing.JFormattedTextField jtxtApellido;
    private javax.swing.JFormattedTextField jtxtCed;
    private javax.swing.JFormattedTextField jtxtCedJ;
    private javax.swing.JFormattedTextField jtxtCedJ01;
    private javax.swing.JFormattedTextField jtxtCedula;
    private javax.swing.JFormattedTextField jtxtCode;
    private javax.swing.JFormattedTextField jtxtCodigo;
    private javax.swing.JFormattedTextField jtxtCodigos;
    private javax.swing.JFormattedTextField jtxtCodigos2;
    private javax.swing.JTextField jtxtCon;
    private javax.swing.JTextField jtxtCont;
    private javax.swing.JTextField jtxtContacto;
    private javax.swing.JPasswordField jtxtContraseña;
    private javax.swing.JPasswordField jtxtContraseña1;
    private javax.swing.JFormattedTextField jtxtDepar;
    private javax.swing.JFormattedTextField jtxtDes;
    private javax.swing.JFormattedTextField jtxtDes1;
    private javax.swing.JFormattedTextField jtxtDesc;
    private javax.swing.JTextField jtxtDescrip;
    private javax.swing.JFormattedTextField jtxtDescripti;
    private javax.swing.JFormattedTextField jtxtDescription;
    private javax.swing.JFormattedTextField jtxtDescription1;
    private javax.swing.JFormattedTextField jtxtDir;
    private javax.swing.JFormattedTextField jtxtDirec;
    private javax.swing.JTextArea jtxtDirecProveBuscar;
    private javax.swing.JTextField jtxtEstado;
    private javax.swing.JFormattedTextField jtxtId;
    private javax.swing.JFormattedTextField jtxtId1;
    private javax.swing.JFormattedTextField jtxtLast;
    private javax.swing.JFormattedTextField jtxtName;
    private javax.swing.JFormattedTextField jtxtNmbr;
    private javax.swing.JFormattedTextField jtxtNom;
    private javax.swing.JFormattedTextField jtxtNombre;
    private javax.swing.JTextField jtxtNombre01;
    private javax.swing.JFormattedTextField jtxtNumSoli;
    private javax.swing.JFormattedTextField jtxtNumSolicitud;
    private javax.swing.JFormattedTextField jtxtNumeroOrden;
    private javax.swing.JFormattedTextField jtxtOrden;
    private javax.swing.JFormattedTextField jtxtOrdenCOmpre;
    private javax.swing.JFormattedTextField jtxtOredesN;
    private javax.swing.JFormattedTextField jtxtPrecio;
    private javax.swing.JFormattedTextField jtxtPuesto;
    private javax.swing.JFormattedTextField jtxtPuesto1;
    private javax.swing.JPasswordField jtxtRepetir;
    private javax.swing.JPasswordField jtxtRepetir1;
    private javax.swing.JFormattedTextField jtxtSolicitante;
    private javax.swing.JFormattedTextField jtxtTel;
    private javax.swing.JFormattedTextField jtxtTele;
    private javax.swing.JFormattedTextField jtxtTele01;
    private javax.swing.JFormattedTextField jtxtTelefono;
    private javax.swing.JFormattedTextField jtxtTotal;
    private javax.swing.JFormattedTextField jtxtTotales;
    private javax.swing.JFormattedTextField jtxtUser;
    private javax.swing.JFormattedTextField jtxtUser1;
    private javax.swing.JFormattedTextField jtxtUser2;
    private javax.swing.JLabel lbl2016;
    private javax.swing.JLabel lblApe;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblBienvenido;
    private javax.swing.JLabel lblCant;
    private javax.swing.JLabel lblCanti;
    private javax.swing.JLabel lblCategorias;
    private javax.swing.JLabel lblCed;
    private javax.swing.JLabel lblCedJu;
    private javax.swing.JLabel lblCedJu01;
    private javax.swing.JLabel lblCedJuri;
    private javax.swing.JLabel lblCod;
    private javax.swing.JLabel lblCode;
    private javax.swing.JLabel lblCodes;
    private javax.swing.JLabel lblCodigos;
    private javax.swing.JLabel lblCon;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblContacto;
    private javax.swing.JLabel lblContacto01;
    private javax.swing.JLabel lblContraseña;
    private javax.swing.JLabel lblContraseña1;
    private javax.swing.JLabel lblD;
    private javax.swing.JLabel lblDe;
    private javax.swing.JLabel lblDepar;
    private javax.swing.JLabel lblDepartamento;
    private javax.swing.JLabel lblDes;
    private javax.swing.JLabel lblDescr;
    private javax.swing.JLabel lblDescri;
    private javax.swing.JLabel lblDescri1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDir;
    private javax.swing.JLabel lblDirection;
    private javax.swing.JLabel lblDirection01;
    private javax.swing.JLabel lblDirections;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstate;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblHeredia;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private javax.swing.JLabel lblJusticificacion;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblLastName1;
    private javax.swing.JLabel lblMunicipalidad;
    private javax.swing.JLabel lblNames;
    private javax.swing.JLabel lblNames01;
    private javax.swing.JLabel lblNom;
    private javax.swing.JLabel lblNombres;
    private javax.swing.JLabel lblNum;
    private javax.swing.JLabel lblNumCompra;
    private javax.swing.JLabel lblNumOrde;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblOrdenCompra;
    private javax.swing.JLabel lblPro;
    private javax.swing.JLabel lblProductos;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lblProveedor01;
    private javax.swing.JLabel lblProveedores;
    private javax.swing.JLabel lblPuesto;
    private javax.swing.JLabel lblPuesto1;
    private javax.swing.JLabel lblRepetir;
    private javax.swing.JLabel lblRepetir1;
    private javax.swing.JLabel lblSan;
    private javax.swing.JLabel lblSeleccionar;
    private javax.swing.JLabel lblSistemaBodega;
    private javax.swing.JLabel lblSolicitante;
    private javax.swing.JLabel lblSolicitudes;
    private javax.swing.JLabel lblTel;
    private javax.swing.JLabel lblTel01;
    private javax.swing.JLabel lblTele;
    private javax.swing.JLabel lblTelefonos;
    private javax.swing.JLabel lblTipoUnidad;
    private javax.swing.JLabel lblTipos;
    private javax.swing.JLabel lblTipos1;
    private javax.swing.JLabel lblTiposU;
    private javax.swing.JLabel lblTotalOr;
    private javax.swing.JLabel lblTotalOrde;
    private javax.swing.JLabel lblTotalOrdenes;
    private javax.swing.JLabel lblUbicaciones;
    private javax.swing.JLabel lblUnitario;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUser1;
    private javax.swing.JLabel lblUser2;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JLabel lblname;
    private javax.swing.JLabel nombreUsuario;
    private javax.swing.JTable tablaNuevoMaterial;
    // End of variables declaration//GEN-END:variables



    private void mostrarProveedor(int codigoProve) {
        String sql="";

            sql="SELECT * FROM proveedores WHERE codigo_proveedor='"+codigoProve+"'";

    
    String []datos = new String [8];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
              

                jTextField7.setText(datos[0]=rs.getString(8));
                jTxtEmpresa.setText(datos[1]=rs.getString(1));
                jTxtEmpresa.setEditable(false);
                jtxtCedJ.setText(datos[2]=rs.getString(2));
                jtxtCedJ.setEditable(false);
                jtxtCont.setText(datos[3]=rs.getString(3));
                jtxtCont.setEditable(false);
                jtxtApe.setText(datos[4]=rs.getString(4));
                jtxtApe.setEditable(false);

                jtxtTele.setText(datos[5]=rs.getString(5));
                jtxtTele.setEditable(false);
                jtxtDir.setText(datos[6]=rs.getString(6));
                jtxtDir.setEditable(false);
                jTxtCate.setText(datos[7]=rs.getString(7));
                jTxtCate.setEditable(false);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void mostrarMaterial(int codMater) {
        String sql="";

            sql="SELECT * FROM material WHERE id_material='"+codMater+"'";

    
    String []datos = new String [3];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
              

                jtxtCode.setText(datos[0]=rs.getString(1));
                
                jComboBox5.setSelectedItem(datos[1]=rs.getString(4));
                jComboBox5.setEnabled(false);
                jtxtDesc.setText(datos[2]=rs.getString(2));
                jtxtDesc.setEditable(false);

            }
           
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }   }

    private void enviarOrden(String[] datos, int numero_orden) {
         String sql2="INSERT INTO detalle_orden(cantidad,id_material, numero_orden, precio_unit)"
                + "values("+datos[2]+","+datos[4]+","+numero_orden+","+datos[7]+")";
            System.out.println(sql2);
        try{
        
        PreparedStatement us1 = con.connect().prepareStatement(sql2);
      us1.executeUpdate();
      System.out.println("Exito");
        }
        catch(SQLException e)
        { System.out.println("Error Mysql");
        System.out.println(sql2);}    }

    private int insertarOrden(String[] datos, int numeroOrden) {
int num=-1;
ResultSet rs;
    String sql="";
    Statement stmt=null;
    
      sql="INSERT INTO orden_compra (codigo_proveedor, estado, fecha_entrega, numero_orden) VALUES("+datos[1]+",'pendiente',"+now()+ ","+datos[6]+")";
      try{
        stmt = con.connect().createStatement();
      
        stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);

        rs = stmt.getGeneratedKeys();
if (rs.next()){
    num=rs.getInt(1);
}
System.out.println(num);
 }
catch(SQLException e){ 
    System.out.println("Error Mysql");
    System.out.println(sql);}
    
    return num;   }
    
}
