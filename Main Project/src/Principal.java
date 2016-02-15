
import DB.JavaConnection;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

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
    Calendar cal = Calendar.getInstance();
    /**
     * Creates new form NewJFrame
     */
    DefaultTableModel modeloTablaNuevoMaterial;
    DefaultTableModel modeloTablaBusquedaEspecifica;
    DefaultTableModel modeloTablaRegistrar;
    DB.JavaConnection con = new DB.JavaConnection();
    
    public Principal() {
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        modeloTablaNuevoMaterial = new DefaultTableModel(null,getColumnasInventario());
        modeloTablaBusquedaEspecifica = new DefaultTableModel(null,getColumnasInventario());
        modeloTablaRegistrar = new DefaultTableModel(null,getColumnasInventario());
        setUndecorated(false);
        initComponents();
       setExtendedState(MAXIMIZED_BOTH);
        setFilasInventario();//Inicializacion de los dos metodos para las tablas
        setLocationRelativeTo(null);
       this.getContentPane().setBackground(Color.white);
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
    
    void MostrarProveedor(String valor){
    DefaultTableModel proveedor= new DefaultTableModel();
    proveedor.addColumn("C. Proveedor");
    proveedor.addColumn("Empresa");
    proveedor.addColumn("Ced.Jurídica");
    proveedor.addColumn("Nom. Contacto");
    proveedor.addColumn("Apel. Contacto");
    proveedor.addColumn("Teléfono");
    jTable20.setModel(proveedor);
    
    String sql="";
    sql="SELECT * FROM proveedores";
    /*if(valor.equals(""))
    {
    sql="SELECT * FROM proveedor";
    }
    else{
    sql="SELECT * FROM proveedores WHERE codigo_proveedor='"+valor+"'";
    }*/
 
    String []datos = new String [6];
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                datos[0]=rs.getString(7);
                
                datos[1]=rs.getString(1);
                datos[2]=rs.getString(2);
                datos[3]=rs.getString(3);
                datos[4]=rs.getString(4);
                datos[5]=rs.getString(5);
                proveedor.addRow(datos);
            }
            jTable20.setModel(proveedor);
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
        String sp =  jSpinnerCantidad.getValue().toString();
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        TaPa1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        TadP3 = new javax.swing.JTabbedPane();
        Pan3 = new javax.swing.JPanel();
        jComboBoxInventario = new javax.swing.JComboBox();
        jButton7 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaNuevoMaterial = new javax.swing.JTable();
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
        Pan11 = new javax.swing.JPanel();
        lblCod = new javax.swing.JLabel();
        lblDescr = new javax.swing.JLabel();
        lblCategorias = new javax.swing.JLabel();
        jComboBoxCategoria = new javax.swing.JComboBox();
        jButton13 = new javax.swing.JButton();
        jtxtDescrip = new javax.swing.JFormattedTextField();
        jtxtCodigo = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxTipo = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jTextCantMin = new javax.swing.JTextField();
        Pan10 = new javax.swing.JPanel();
        lblCodes = new javax.swing.JLabel();
        lblDe = new javax.swing.JLabel();
        lblCanti = new javax.swing.JLabel();
        lblUbicaciones = new javax.swing.JLabel();
        jComboBoxUbicacionRegistro = new javax.swing.JComboBox();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jtxtCodigos = new javax.swing.JFormattedTextField();
        jtxtDescription = new javax.swing.JFormattedTextField();
        jSpinnerCantidad = new javax.swing.JSpinner();
        Pan9 = new javax.swing.JPanel();
        lblCodigos = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaBusquedaEspecifica = new javax.swing.JTable();
        lblD = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jtxtCodigos2 = new javax.swing.JFormattedTextField();
        jtxtDescription1 = new javax.swing.JFormattedTextField();
        lblInventario = new java.awt.Label();
        jPanel2 = new javax.swing.JPanel();
        lblOrdenes = new java.awt.Label();
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
        jTable6 = new javax.swing.JTable();
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
        jtxtNomb = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jtxtApe = new javax.swing.JFormattedTextField();
        jtxtCedJ = new javax.swing.JFormattedTextField();
        jtxtTele = new javax.swing.JFormattedTextField();
        jtxtDir = new javax.swing.JFormattedTextField();
        jtxtOrden = new javax.swing.JFormattedTextField();
        jtxtCantidades = new javax.swing.JFormattedTextField();
        jtxtPrecio = new javax.swing.JFormattedTextField();
        jtxtTotales = new javax.swing.JFormattedTextField();
        jTextField7 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
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
        jScrollPane24 = new javax.swing.JScrollPane();
        jTable20 = new javax.swing.JTable();
        jLabela10 = new javax.swing.JLabel();
        NProveedor01 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jtxtNombre01 = new javax.swing.JTextField();
        jtxtApe01 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable21 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTable22 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblAdministracion = new java.awt.Label();
        TadP4 = new javax.swing.JTabbedPane();
        Pan15 = new javax.swing.JPanel();
        jSeparator9 = new javax.swing.JSeparator();
        lblUserName = new javax.swing.JLabel();
        jtxtNom = new javax.swing.JFormattedTextField();
        jComboBox10 = new javax.swing.JComboBox();
        lblTipos = new javax.swing.JLabel();
        btnConsulta = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        lblFiltro = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        jtxtId = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblTipos1 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        lblUserName1 = new javax.swing.JLabel();
        jtxtNom1 = new javax.swing.JFormattedTextField();
        lblId1 = new javax.swing.JLabel();
        jtxtId1 = new javax.swing.JFormattedTextField();
        btnConsulta1 = new javax.swing.JButton();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable19 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jtxtUser1 = new javax.swing.JFormattedTextField();
        lblUser1 = new javax.swing.JLabel();
        lblUsers1 = new javax.swing.JLabel();
        jComboBox13 = new javax.swing.JComboBox();
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
        Pan13 = new javax.swing.JPanel();
        lblUsers = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        lblUser = new javax.swing.JLabel();
        jtxtUser = new javax.swing.JFormattedTextField();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
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
        btnDeshacer = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        lblUser2 = new javax.swing.JLabel();
        jtxtUser2 = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        lblEntradaSalida = new java.awt.Label();
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
        lblSolicitud = new java.awt.Label();
        lblBienvenido = new javax.swing.JLabel();
        nombreUsuario = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        lblSan = new javax.swing.JLabel();
        lblHeredia = new javax.swing.JLabel();
        lbl2016 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lblSBMF = new java.awt.Label();
        btnAyuda = new javax.swing.JButton();
        lblSistemaBodega = new javax.swing.JLabel();
        lblMunicipalidad = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Bodega Municipalidad de Flores");
        setBackground(new java.awt.Color(204, 204, 204));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Screenshot_1.png"))); // NOI18N

        TaPa1.setBackground(new java.awt.Color(255, 255, 255));
        TaPa1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        TaPa1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        TaPa1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TaPa1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
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

        jButton7.setText("Cargar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        tablaNuevoMaterial.setModel(modeloTablaNuevoMaterial);
        jScrollPane10.setViewportView(tablaNuevoMaterial);

        javax.swing.GroupLayout Pan3Layout = new javax.swing.GroupLayout(Pan3);
        Pan3.setLayout(Pan3Layout);
        Pan3Layout.setHorizontalGroup(
            Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jComboBoxInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE))
                .addContainerGap())
        );
        Pan3Layout.setVerticalGroup(
            Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(Pan3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                "Código ", "Tipo de Unidad", "Descripción", "Cantidad", "Ubicación", "Completo", "Cant Recibidad"
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
        jtxtOredesN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtOredesNActionPerformed(evt);
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
                .addContainerGap(737, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan18Layout.createSequentialGroup()
                .addContainerGap(500, Short.MAX_VALUE)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
            .addComponent(jScrollPane11)
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
                .addContainerGap(422, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("Con Orden de compra", Pan18);

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solicitudes Pendientes" }));

        lblSeleccionar.setText("Seleccionar solicitud");

        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Tipo de Unidad", "Descripcion", "Cantidad", "Completa", "Cant Entregada"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTable12);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane13.setViewportView(jTextArea1);

        lblJusticificacion.setText("Justificación de la Solicitud ");

        lblSolicitante.setText("Solicitante");

        btnDescontar.setText("Descontar del Inventario");

        lblDepar.setText("Departamento");

        try {
            jtxtDepar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtSolicitante.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtSolicitante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtSolicitanteActionPerformed(evt);
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
                .addContainerGap(297, Short.MAX_VALUE))
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
                .addContainerGap(449, Short.MAX_VALUE))
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

        lblCod.setText("Código ");

        lblDescr.setText("Nombre ");

        lblCategorias.setText("Categoría");

        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Acueducto", "Materiales", "Limpieza", "Suministros de oficina", "Herramientas" }));
        jComboBoxCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCategoriaActionPerformed(evt);
            }
        });

        jButton13.setText("Cargar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        try {
            jtxtDescrip.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*************************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDescrip.setToolTipText("25 caracteres maximo");
        jtxtDescrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDescripActionPerformed(evt);
            }
        });

        try {
            jtxtCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigo.setToolTipText("6 Caracteres Alfanumericos");
        jtxtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCodigoActionPerformed(evt);
            }
        });

        jLabel1.setText("Cantidad Mínima");

        jLabel20.setText("Tipo de unidad");

        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bolsa", "Caja", "Cubeta", "Galón", "1/4 Galón", "Laminas", "Litros", "Kit", "Kilo", "Paquete", "Pares", "Unidad" }));
        jComboBoxTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoActionPerformed(evt);
            }
        });

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "", null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Tipo De Unidad", "Descripción", "Cantidad", "Ubicación"
            }
        ));
        jScrollPane7.setViewportView(jTable7);

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
                            .addComponent(lblCategorias)
                            .addComponent(lblCod))
                        .addGap(49, 49, 49)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtxtDescrip)
                            .addComponent(jComboBoxCategoria, 0, 472, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan11Layout.createSequentialGroup()
                                .addComponent(jtxtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(35, 35, 35)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel1))
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan11Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan11Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextCantMin, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(131, 131, 131))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(291, 291, 291))
                    .addGroup(Pan11Layout.createSequentialGroup()
                        .addComponent(jScrollPane7)
                        .addContainerGap())))
        );
        Pan11Layout.setVerticalGroup(
            Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan11Layout.createSequentialGroup()
                .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan11Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lblCod)
                        .addGap(21, 21, 21)
                        .addComponent(lblDescr))
                    .addGroup(Pan11Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jTextCantMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtxtDescrip, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCategorias)
                    .addComponent(jComboBoxCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TadP3.addTab("Nuevo Material", Pan11);

        lblCodes.setText("Código  ");

        lblDe.setText("Nombre");

        lblCanti.setText("Cantidad");

        lblUbicaciones.setText("Ubicación");

        jComboBoxUbicacionRegistro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A1", "A2", "A3", "B1", "B2", "B3" }));
        jComboBoxUbicacionRegistro.setToolTipText("");
        jComboBoxUbicacionRegistro.setName(""); // NOI18N

        jTable9.setModel(modeloTablaRegistrar);
        jScrollPane9.setViewportView(jTable9);

        jButton11.setText("Cargar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        try {
            jtxtCodigos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigos.setToolTipText("6 Caracteres Alfanumericos");
        jtxtCodigos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCodigosActionPerformed(evt);
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

        javax.swing.GroupLayout Pan10Layout = new javax.swing.GroupLayout(Pan10);
        Pan10.setLayout(Pan10Layout);
        Pan10Layout.setHorizontalGroup(
            Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan10Layout.createSequentialGroup()
                        .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan10Layout.createSequentialGroup()
                                .addComponent(lblCodes, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(152, 152, 152)
                                .addComponent(lblDe)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCanti)
                            .addGroup(Pan10Layout.createSequentialGroup()
                                .addGap(108, 108, 108)
                                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtCodigos, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(Pan10Layout.createSequentialGroup()
                                        .addComponent(jSpinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(lblUbicaciones)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBoxUbicacionRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 430, Short.MAX_VALUE))
                    .addComponent(jScrollPane9))
                .addContainerGap())
            .addGroup(Pan10Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Pan10Layout.setVerticalGroup(
            Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCodigos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDe)
                    .addComponent(jtxtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Pan10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCanti)
                    .addComponent(jSpinnerCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxUbicacionRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUbicaciones))
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboBoxUbicacionRegistro.getAccessibleContext().setAccessibleName("");

        TadP3.addTab("Registro", Pan10);

        lblCodigos.setText("Código  ");

        tablaBusquedaEspecifica.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tablaBusquedaEspecifica.setModel(modeloTablaBusquedaEspecifica);
        jScrollPane8.setViewportView(tablaBusquedaEspecifica);

        lblD.setText("Nombre");

        jButton9.setText("Buscar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        try {
            jtxtCodigos2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCodigos2.setToolTipText("6 Caracteres Alfanumericos");
        jtxtCodigos2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCodigos2ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout Pan9Layout = new javax.swing.GroupLayout(Pan9);
        Pan9.setLayout(Pan9Layout);
        Pan9Layout.setHorizontalGroup(
            Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblD)
                    .addComponent(jLabel16)
                    .addComponent(lblCodigos))
                .addGap(18, 18, 18)
                .addGroup(Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Pan9Layout.createSequentialGroup()
                        .addComponent(jtxtCodigos2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtxtDescription1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Pan9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
        );
        Pan9Layout.setVerticalGroup(
            Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigos)
                    .addComponent(jtxtCodigos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addGap(9, 9, 9)
                .addGroup(Pan9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtDescription1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblD))
                .addGap(71, 71, 71)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(497, Short.MAX_VALUE))
        );

        TadP3.addTab("Búsqueda   Específica", Pan9);

        jPanel1.add(TadP3, java.awt.BorderLayout.CENTER);

        lblInventario.setAlignment(java.awt.Label.CENTER);
        lblInventario.setBackground(new java.awt.Color(0, 153, 255));
        lblInventario.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblInventario.setText("Inventario");
        jPanel1.add(lblInventario, java.awt.BorderLayout.PAGE_START);

        TaPa1.addTab("Inventario", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblOrdenes.setAlignment(java.awt.Label.CENTER);
        lblOrdenes.setBackground(new java.awt.Color(0, 153, 255));
        lblOrdenes.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblOrdenes.setForeground(new java.awt.Color(255, 255, 255));
        lblOrdenes.setText("Ordenes de Compra");

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

        javax.swing.GroupLayout Pan7Layout = new javax.swing.GroupLayout(Pan7);
        Pan7.setLayout(Pan7Layout);
        Pan7Layout.setHorizontalGroup(
            Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan7Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalOrde)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
            .addGroup(Pan7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan7Layout.createSequentialGroup()
                        .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnConsultar, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
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
                        .addComponent(lblPro, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(306, Short.MAX_VALUE))
            .addGroup(Pan7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 169, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
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
                            .addComponent(jFormattedTextField69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Pan7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTotalOrde)
                        .addComponent(jtxtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        TaP2.addTab("Modificar", Pan7);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(jTable6);

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

        lblCed.setText("Cedula Jurídica ");

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
                        .addGap(29, 29, 29)
                        .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pan8Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(282, 282, 282))
                            .addGroup(Pan8Layout.createSequentialGroup()
                                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblCed)
                                        .addComponent(lblContact, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTelefonos, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblDirections, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(lblNombres))
                                .addGap(59, 59, 59)
                                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan8Layout.createSequentialGroup()
                                        .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblApellidos)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                                        .addComponent(jtxtLast, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtCon)
                                        .addComponent(jtxtCed)
                                        .addComponent(jtxtTel)
                                        .addComponent(jtxtDirec, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(46, 46, 46))
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Pan8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalOrdenes)
                    .addComponent(jFormattedTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Cantidad", "Tipo De Unidad", "Descripción", "Código", "Precio unitario ¢", "Precio Total ¢"
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

        lblProveedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProveedor.setText("Codigo de Proveedor");

        lblCant.setText("Cantidad");

        lblTiposU.setText("Tipo de Unidad");

        lblDes.setText("Descripción");

        lblCode.setText("Código ");

        lblUnitario.setText("Precio Unitario ");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bolsa", "Caja", "Cubeta", "Galón", "1/4 Galón", "Laminas", "Litros", "Kit", "Kilo", "Paquete", "Pares", "Unidad" }));

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

        try {
            jtxtCode.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("AAAAAA")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtNomb.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNomb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNombActionPerformed(evt);
            }
        });

        jLabel14.setText("Apellido");

        try {
            jtxtApe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("ULLLLLLLLLLL")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
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

        try {
            jtxtTele.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtDir.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********************")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtOrden.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jtxtCantidades.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jtxtPrecio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¢###########"))));

        jtxtTotales.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("¢###########"))));

        javax.swing.GroupLayout Pan6Layout = new javax.swing.GroupLayout(Pan6);
        Pan6.setLayout(Pan6Layout);
        Pan6Layout.setHorizontalGroup(
            Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addComponent(lblProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(Pan6Layout.createSequentialGroup()
                                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblCedJu)
                                                .addComponent(lblTel, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblDirection, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(lblNames))
                                        .addGap(59, 59, 59))
                                    .addGroup(Pan6Layout.createSequentialGroup()
                                        .addComponent(lblContacto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(22, 22, 22)))
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtCedJ)
                                        .addComponent(jtxtTele)
                                        .addComponent(jtxtDir)
                                        .addGroup(Pan6Layout.createSequentialGroup()
                                            .addComponent(jtxtCont, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel14)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jtxtApe, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextField7)
                                        .addComponent(jtxtNomb, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)))))
                        .addGap(28, 38, Short.MAX_VALUE)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTiposU)
                            .addComponent(lblDes)
                            .addComponent(lblCode)
                            .addComponent(lblUnitario)
                            .addComponent(lblNum)
                            .addComponent(lblCant))))
                .addGap(32, 32, 32)
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtxtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtxtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCantidades, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
            .addGroup(Pan6Layout.createSequentialGroup()
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(lblTotalOr)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        Pan6Layout.setVerticalGroup(
            Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan6Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProveedor)
                    .addComponent(lblProductos)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNames)
                            .addComponent(lblNum)
                            .addComponent(jtxtNomb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblCedJu)
                                    .addComponent(jtxtCedJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblContacto)
                                    .addComponent(jtxtCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14)
                                    .addComponent(jtxtApe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTel)
                                    .addComponent(jtxtTele, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDirection)
                                    .addComponent(jtxtDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblCode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTiposU)
                                .addGap(12, 12, 12)
                                .addComponent(lblDes)
                                .addGap(15, 15, 15)
                                .addComponent(lblCant)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUnitario)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Pan6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotalOr)
                            .addComponent(jtxtTotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Pan6Layout.createSequentialGroup()
                        .addComponent(jtxtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jtxtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtCantidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pan6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(Pan6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        TaP2.addTab("Crear", jPanel27);

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jPanel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblProveedor01.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblProveedor01.setText("Proveedor");

        lblNames01.setText("Empresa");

        lblCedJu01.setText("Cedula Jurídica ");

        lblContacto01.setText("Nombre del Contacto");

        lblTel01.setText("Teléfono ");

        lblDirection01.setText("Dirección");

        try {
            jtxtTele01.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

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
        jtxtCedJ01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCedJ01ActionPerformed(evt);
            }
        });

        jLabela15.setText("Apellido");

        jTextAreaDir.setColumns(20);
        jTextAreaDir.setRows(5);
        jScrollPane23.setViewportView(jTextAreaDir);

        jTable20.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane24.setViewportView(jTable20);

        jLabela10.setText("Código de proveedor");

        jButton3.setText("Cargar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Registrar");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane24)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContacto01)
                            .addComponent(lblTel01)
                            .addComponent(lblNames01)
                            .addComponent(lblCedJu01)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProveedor01, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jtxtNombre01, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabela15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtxtApe01))
                            .addComponent(jtxtCedJ01, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                            .addComponent(jtxtTele01)
                            .addComponent(jTxtEmpresa01))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblDirection01)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabela10)
                                .addGap(18, 18, 18)
                                .addComponent(NProveedor01)))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(305, 305, 305))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProveedor01)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNames01)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabela10)
                            .addComponent(NProveedor01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTxtEmpresa01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblContacto01)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jtxtCedJ01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCedJu01))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabela15)
                                            .addComponent(jtxtNombre01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtApe01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblDirection01))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtTele01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTel01))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(498, 498, 498)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Crear", jPanel7);

        jLabel10.setText("Codigo de Proveedor");

        jLabel11.setText("Nombre de Proveedor");

        jTable21.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "C.Proveedor", "Empresa", "Ced.Jurídica", "N.Contacto", "Ape.Contacto", "Teléfono"
            }
        ));
        jScrollPane26.setViewportView(jTable21);

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton4.setText("Buscar ");

        jLabel12.setText("Dirección");

        jTextArea6.setColumns(20);
        jTextArea6.setRows(5);
        jScrollPane27.setViewportView(jTextArea6);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(jTextField3))
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
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consultar", jPanel8);

        jLabel13.setText("Codigo de Proveedor");

        jLabel15.setText("Nombre de Proveedor");

        jButton5.setText("Buscar");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel17.setText("Dirección");

        jTextArea7.setColumns(20);
        jTextArea7.setRows(5);
        jScrollPane28.setViewportView(jTextArea7);

        jTable22.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "C.Proveedor", "Empresa", "ced.Jurídica", "N.Contacto", "Ape.Contacto", "Teléfono"
            }
        ));
        jScrollPane29.setViewportView(jTable22);

        jButton8.setText("Modificar");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane29)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(309, 309, 309))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel13))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(128, 128, 128)
                                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel17))))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel17))
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
        );

        jTabbedPane1.addTab("Modificar", jPanel9);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 162, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        TaP2.addTab("Proveedores ", jPanel6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblOrdenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TaP2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lblOrdenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TaP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        TaP2.getAccessibleContext().setAccessibleName("Crear");

        TaPa1.addTab("Órdenes de Compra", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblAdministracion.setAlignment(java.awt.Label.CENTER);
        lblAdministracion.setBackground(new java.awt.Color(0, 153, 255));
        lblAdministracion.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblAdministracion.setForeground(new java.awt.Color(255, 255, 255));
        lblAdministracion.setText("Administración de Usuarios");

        TadP4.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N

        lblUserName.setText("Nombre de Usuario");

        try {
            jtxtNom.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));

        lblTipos.setText("Tipo de Usuario");

        btnConsulta.setText("Consultar");

        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id de Usuario", "Password", "Nombre ", "Apellido", "Categoría", "Puesto", "Descripción "
            }
        ));
        jScrollPane14.setViewportView(jTable13);

        lblFiltro.setText("Filtros de Búsqueda ");

        lblId.setText("Id de Usuario");

        try {
            jtxtId.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButton1.setText("Deshabilitar Usuario");

        javax.swing.GroupLayout Pan15Layout = new javax.swing.GroupLayout(Pan15);
        Pan15.setLayout(Pan15Layout);
        Pan15Layout.setHorizontalGroup(
            Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator10)
            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(Pan15Layout.createSequentialGroup()
                .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan15Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFiltro)
                            .addGroup(Pan15Layout.createSequentialGroup()
                                .addComponent(lblTipos)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblUserName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtxtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblId)
                                .addGap(27, 27, 27)
                                .addComponent(jtxtId, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(Pan15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Pan15Layout.createSequentialGroup()
                        .addGap(288, 288, 288)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        Pan15Layout.setVerticalGroup(
            Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFiltro)
                .addGap(22, 22, 22)
                .addGroup(Pan15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipos)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName)
                    .addComponent(jtxtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsulta)
                    .addComponent(lblId)
                    .addComponent(jtxtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(40, 40, 40))
        );

        TadP4.addTab("Deshabilitar Usuario", Pan15);

        lblTipos1.setText("Tipo de Usuario");

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));

        lblUserName1.setText("Nombre de Usuario");

        try {
            jtxtNom1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblId1.setText("Id de Usuario");

        try {
            jtxtId1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnConsulta1.setText("Consultar");

        jTable19.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id de Usuario", "Password", "Nombre ", "Apellido", "Categoría", "Puesto", "Descripción ", "Estado "
            }
        ));
        jScrollPane22.setViewportView(jTable19);

        jLabel8.setText("Filtro de Busqueda");

        try {
            jtxtUser1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lblUser1.setText("Nombre de Usuario");

        lblUsers1.setText("Tipo de Usuario");

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));

        lblContraseña1.setText("Contraseña");

        lblRepetir1.setText("Repetir Contraseña");

        lblPuesto1.setText("Puesto");

        lblDescri1.setText("Descripción ");

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

        lblLastName1.setText("Apellido");

        jLabel9.setText("Estatus ");

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Activar", "Desactivar" }));

        jButton2.setText("Guardar Cambios");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel8)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblUser1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtxtUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblUsers1)
                                            .addComponent(lblContraseña1))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtContraseña1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblRepetir1)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtRepetir1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblPuesto1)
                                .addComponent(lblLastName1)
                                .addComponent(lblDescri1)
                                .addComponent(jLabel9)))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtApel1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtPuesto1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDes1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lblTipos1)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblUserName1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtNom1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblId1)
                        .addGap(97, 97, 97)
                        .addComponent(jtxtId1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnConsulta1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipos1)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName1)
                    .addComponent(jtxtNom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConsulta1)
                    .addComponent(lblId1)
                    .addComponent(jtxtId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUser1)
                    .addComponent(jtxtUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLastName1)
                    .addComponent(jtxtApel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsers1)
                    .addComponent(lblPuesto1)
                    .addComponent(jtxtPuesto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescri1)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblContraseña1)
                        .addComponent(jtxtContraseña1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtxtDes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jtxtRepetir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRepetir1))
                .addGap(69, 69, 69)
                .addComponent(jButton2)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        TadP4.addTab("Modificar Usuario", jPanel3);

        lblUsers.setText("Tipo de Usuario");

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrador", "SuperUsuario\t", "Proveduria", "Bodega ", "Mantenimiento ", "Departamental" }));

        lblUser.setText("Nombre");

        try {
            jtxtUser.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jTable14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id de Usuario", "Password", "Nombre ", "Apellido", "Puesto", "Descripción "
            }
        ));
        jScrollPane15.setViewportView(jTable14);

        lblContraseña.setText("Contraseña");

        lblRepetir.setText("Repetir Contrasena");

        lblLastName.setText("Apellido");

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

        lblPuesto.setText("Puesto");

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

        lblDescri.setText("Descripción ");

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

        btnCrearUsuario.setText("Crear Usuario");
        btnCrearUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearUsuarioActionPerformed(evt);
            }
        });

        btnDeshacer.setText("Deshacer");

        lblUser2.setText("Nickname");

        try {
            jtxtUser2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("**********")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout Pan13Layout = new javax.swing.GroupLayout(Pan13);
        Pan13.setLayout(Pan13Layout);
        Pan13Layout.setHorizontalGroup(
            Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUser, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan13Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel21))
                    .addComponent(lblUser2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan13Layout.createSequentialGroup()
                        .addComponent(lblRepetir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pan13Layout.createSequentialGroup()
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsers)
                            .addComponent(lblContraseña))
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Pan13Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jtxtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Pan13Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtUser2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Pan13Layout.createSequentialGroup()
                        .addComponent(lblLastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtxtApel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Pan13Layout.createSequentialGroup()
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDescri)
                            .addComponent(lblPuesto))
                        .addGap(56, 56, 56)
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDes, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(Pan13Layout.createSequentialGroup()
                        .addComponent(btnCrearUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeshacer)))
                .addGap(283, 283, 283))
            .addGroup(Pan13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 189, Short.MAX_VALUE))
        );
        Pan13Layout.setVerticalGroup(
            Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan13Layout.createSequentialGroup()
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan13Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUser)
                            .addComponent(lblLastName)
                            .addComponent(jtxtApel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jtxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan13Layout.createSequentialGroup()
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPuesto)
                            .addComponent(jtxtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDescri)
                            .addComponent(jtxtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pan13Layout.createSequentialGroup()
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(Pan13Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUser2))
                            .addComponent(jtxtUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsers)
                            .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addGap(16, 16, 16)
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblContraseña)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pan13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtxtRepetir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRepetir))
                    .addComponent(btnCrearUsuario)
                    .addComponent(btnDeshacer))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        TadP4.addTab("Crear usuario", Pan13);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAdministracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(TadP4)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblAdministracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(TadP4, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(397, Short.MAX_VALUE))
        );

        TaPa1.addTab("Administración de Usuarios", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblEntradaSalida.setAlignment(java.awt.Label.CENTER);
        lblEntradaSalida.setBackground(new java.awt.Color(0, 153, 255));
        lblEntradaSalida.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblEntradaSalida.setForeground(new java.awt.Color(255, 255, 255));
        lblEntradaSalida.setText("Entradas y Salidas de Materiales");

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
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
                .addContainerGap())
        );
        Pan14Layout.setVerticalGroup(
            Pan14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMostrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        TadP7.addTab("Ordenes de compra", Pan14);

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
            .addComponent(jScrollPane18)
        );
        Pan24Layout.setVerticalGroup(
            Pan24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMuestra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                    .addGroup(Pan25Layout.createSequentialGroup()
                        .addGroup(Pan25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMuestras)
                            .addComponent(jLabel18)
                            .addGroup(Pan25Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(38, 38, 38)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(btnMuestras)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        TadP7.addTab("Usuarios Logueados", Pan25);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TadP7)
                .addGap(29, 29, 29))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TadP7)
        );

        TadP5.addTab("Registro ", jPanel16);

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
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Pan17Layout.setVerticalGroup(
            Pan17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pan17Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        TadP5.addTab("Registro básico", Pan17);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEntradaSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(TadP5)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(lblEntradaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TadP5, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        TaPa1.addTab("Bitácora", jPanel5);

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
        jtxtNumSolicitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNumSolicitudActionPerformed(evt);
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
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
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
                .addContainerGap(432, Short.MAX_VALUE))
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
                "Nº Solicitud", "Còdigo", "Cantidad", "Tipo de unidad", "Descripción"
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
        jtxtNmbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNmbrActionPerformed(evt);
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
        jScrollPane20.setViewportView(jTextArea2);

        jLabel4.setText("Cantidad");

        jLabel5.setText("Código");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Materiales");

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
                                    .addComponent(jtxtNmbr, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(jComboBox15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
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

        lblDescripcion.setText("Descripción");

        btnEnviaCambio.setText("Enviar cambios");

        try {
            jtxtNumSoli.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtNumSoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNumSoliActionPerformed(evt);
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
                        .addGap(0, 20, Short.MAX_VALUE)
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

        lblSolicitud.setAlignment(java.awt.Label.CENTER);
        lblSolicitud.setBackground(new java.awt.Color(0, 153, 255));
        lblSolicitud.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblSolicitud.setForeground(new java.awt.Color(255, 255, 255));
        lblSolicitud.setText("Solicitud de Materiales ");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTaPa9, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(lblSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTaPa9, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TaPa1.addTab("Solicitud de materiales", jPanel23);

        lblBienvenido.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblBienvenido.setText("Bienvenido");

        nombreUsuario.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        nombreUsuario.setText("Usuario");

        btnCerrarSesion.setText("Cerrar sesión ");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        lblSan.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblSan.setText("San Joaquín  de Flores ");

        lblHeredia.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblHeredia.setText("Heredia");

        lbl2016.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lbl2016.setText("2016");

        lblSBMF.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        lblSBMF.setText("S B M  F");

        btnAyuda.setText("Ayuda");
        btnAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAyudaActionPerformed(evt);
            }
        });

        lblSistemaBodega.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblSistemaBodega.setText("Sistema de Bodega Municipalidad de Flores");

        lblMunicipalidad.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblMunicipalidad.setText("Municipalidad de Flores");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSistemaBodega)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(lblMunicipalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(324, 324, 324))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TaPa1)
                            .addComponent(jSeparator8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblBienvenido)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nombreUsuario))
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblSan)))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSBMF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lbl2016))
                            .addComponent(lblHeredia))
                        .addGap(70, 70, 70))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBienvenido)
                    .addComponent(nombreUsuario))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAyuda)
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSBMF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(lblHeredia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl2016)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblMunicipalidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSistemaBodega)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TaPa1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 39, Short.MAX_VALUE))
        );

        lblSBMF.getAccessibleContext().setAccessibleName("S.B.M.F");

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

    private void jtxtSolicitanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtSolicitanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtSolicitanteActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

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

    private void jtxtDescripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDescripActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDescripActionPerformed

    private void jtxtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodigoActionPerformed

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

    private void jtxtNombActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNombActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNombActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        // TODO add your handling code here:
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

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
    LlenarNuevoMaterial();
    LimpiarNuevoMaterial();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jComboBoxCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCategoriaActionPerformed

    private void jComboBoxTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxTipoActionPerformed

    private void jComboBoxInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxInventarioActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        Object item = jComboBoxInventario.getSelectedItem();
        String categ = String.valueOf(item);
        System.out.println(categ);
        limpiarTabla(tablaNuevoMaterial);
        setFilasInventarioSelected(categ);
       
// TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

 Registrar();
 limpiarTabla(tablaNuevoMaterial);
 setFilasRegistrar(jtxtCodigos.getText());
 setFilasInventario();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jtxtCodigosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCodigosActionPerformed

         BuscarCod(jtxtDescription,jtxtCodigos);  // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodigosActionPerformed

    private void jtxtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDescriptionActionPerformed

    private void jtxtDescription1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDescription1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDescription1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //limpiarTabla(tablaBusquedaEspecifica); mejor no limpiarla porque talvez la persona quiera ver las busquedas recientes o asi en la sesion... 
        String cod = jtxtCodigos2.getText();
        setFilasBuscarEspecifico(cod);
    BuscarCod(jtxtDescription1,jtxtCodigos2);
// TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jtxtNumeroOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNumeroOrdenActionPerformed
      
    }//GEN-LAST:event_jtxtNumeroOrdenActionPerformed

    private void jtxtOredesNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtOredesNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtOredesNActionPerformed

    private void jtxtCodigos2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCodigos2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCodigos2ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
       
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jTxtEmpresa01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtEmpresa01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtEmpresa01ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         int codigoProve= Integer.parseInt(NProveedor01.getText());
    try{
           PreparedStatement us = cn.prepareStatement("INSERT INTO proveedores(empresa,ced_juridica,nombre_contacto,apel_contacto,telefono,direccion,codigo_proveedor) VALUES (?,?,?,?,?,?,?)");
          
 
            
            us.setString(1, jTxtEmpresa01.getText());
            us.setString(2, jtxtCedJ01.getText());
            us.setString(3, jtxtNombre01.getText());
            us.setString(4, jtxtApe01.getText());
            us.setString(5, jtxtTele01.getText());
            
            us.setString(6, jTextAreaDir.getText());  
            us.setInt(7, codigoProve);
            us.executeUpdate();
            MostrarProveedor("");

            JOptionPane.showMessageDialog(null, "Datos Guardados");
            
    }  catch(SQLException | HeadlessException e){
        System.out.println(e.getMessage());
        JOptionPane.showMessageDialog(null, "Datos Ingresados Incorrectamente");
        }
    
    //LimpiarNuevoProveedor();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtxtCedJ01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCedJ01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedJ01ActionPerformed
 

    
    
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
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField NProveedor01;
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
    private javax.swing.JPanel Pan9;
    private javax.swing.JTabbedPane TaP2;
    private javax.swing.JTabbedPane TaPa1;
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
    private javax.swing.JButton btnConsulta;
    private javax.swing.JButton btnConsulta1;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnCrearUsuario;
    private javax.swing.JButton btnDescontar;
    private javax.swing.JButton btnDeshacer;
    private javax.swing.JButton btnEnviaCambio;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnMuestra;
    private javax.swing.JButton btnMuestras;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabela10;
    private javax.swing.JLabel jLabela15;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinnerCantidad;
    private javax.swing.JTabbedPane jTaPa9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable17;
    private javax.swing.JTable jTable18;
    private javax.swing.JTable jTable19;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable20;
    private javax.swing.JTable jTable21;
    private javax.swing.JTable jTable22;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextArea jTextAreaDir;
    private javax.swing.JTextField jTextCantMin;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField jTxtEmpresa01;
    private javax.swing.JFormattedTextField jtxtApe;
    private javax.swing.JTextField jtxtApe01;
    private javax.swing.JFormattedTextField jtxtApel;
    private javax.swing.JFormattedTextField jtxtApel1;
    private javax.swing.JFormattedTextField jtxtApellido;
    private javax.swing.JFormattedTextField jtxtCantidades;
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
    private javax.swing.JFormattedTextField jtxtDescrip;
    private javax.swing.JFormattedTextField jtxtDescripti;
    private javax.swing.JFormattedTextField jtxtDescription;
    private javax.swing.JFormattedTextField jtxtDescription1;
    private javax.swing.JFormattedTextField jtxtDir;
    private javax.swing.JFormattedTextField jtxtDirec;
    private javax.swing.JTextField jtxtEstado;
    private javax.swing.JFormattedTextField jtxtId;
    private javax.swing.JFormattedTextField jtxtId1;
    private javax.swing.JFormattedTextField jtxtLast;
    private javax.swing.JFormattedTextField jtxtName;
    private javax.swing.JFormattedTextField jtxtNmbr;
    private javax.swing.JFormattedTextField jtxtNom;
    private javax.swing.JFormattedTextField jtxtNom1;
    private javax.swing.JFormattedTextField jtxtNomb;
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
    private java.awt.Label lblAdministracion;
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
    private java.awt.Label lblEntradaSalida;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstate;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblHeredia;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblId1;
    private java.awt.Label lblInventario;
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
    private java.awt.Label lblOrdenes;
    private javax.swing.JLabel lblPro;
    private javax.swing.JLabel lblProductos;
    private javax.swing.JLabel lblProveedor;
    private javax.swing.JLabel lblProveedor01;
    private javax.swing.JLabel lblProveedores;
    private javax.swing.JLabel lblPuesto;
    private javax.swing.JLabel lblPuesto1;
    private javax.swing.JLabel lblRepetir;
    private javax.swing.JLabel lblRepetir1;
    private java.awt.Label lblSBMF;
    private javax.swing.JLabel lblSan;
    private javax.swing.JLabel lblSeleccionar;
    private javax.swing.JLabel lblSistemaBodega;
    private javax.swing.JLabel lblSolicitante;
    private java.awt.Label lblSolicitud;
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
    private javax.swing.JLabel lblUserName1;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JLabel lblUsers1;
    private javax.swing.JLabel lblname;
    private javax.swing.JLabel nombreUsuario;
    private javax.swing.JTable tablaBusquedaEspecifica;
    private javax.swing.JTable tablaNuevoMaterial;
    // End of variables declaration//GEN-END:variables
    JavaConnection cc= new JavaConnection(); //probando
    Connection cn= cc.connect();
}
