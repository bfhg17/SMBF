


 
package DB;
import java.sql.*;
/**
 *
 * @author Bradley
 */
public class JavaConnection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        try{
        System.out.print("Trying to resolve to DB \n ");
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sbmf","root",""); 
        System.out.print("Sucessful Conection to DB \n ");
        
        
        //Devolver datos desde la base de datos para recapturarlos en la app 
        Statement estado = con.createStatement();
        ResultSet resultado = estado.executeQuery("SELECT * FROM material");
        System.out.println("ID \t Nombre \t Categoria \t Tipo de Unidad \t Cantidad Minima \n ");
            while(resultado.next()){
                System.out.println(resultado.getString("id_Material")+ "\t"+resultado.getString("nombre")
                + "\t\t"+resultado.getString("categoria")+ "\t\t"+resultado.getString("tipoUnidad")
                + "\t\t\t"+resultado.getString("cantidadMinima"));
                }
            
        }
        catch(SQLException e){
        System.out.print("MYSQL ERROR");
        }
        catch(ClassNotFoundException e){
        e.printStackTrace();
        }
        catch(Exception e){
        System.out.println("Se ha presentado el siguiente error"+e.getMessage());
        }
    }
    
}
