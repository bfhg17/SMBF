package DB;
import java.sql.*;
/**
 *
 * @author Bradley
 */
public class JavaConnection {
    
    
    private Connection con = null;
   
      
        public Connection connect(){
   
            try{
            System.out.print("Trying to resolve to DB \n ");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/sbmf","root",""); 
             System.out.print("Sucessful Conection to DB \n ");        
             }
                catch(SQLException e){
                System.out.print("MYSQL ERROR");
                }
                catch(ClassNotFoundException e){
                }
                catch(Exception e){
                System.out.println("Se ha presentado el siguiente error"+e.getMessage());
                }
                return con;
        }
        
        
        public void disconnect(Connection con ){
            try{
            con.close();
            }catch(SQLException e){
            System.out.println("ERROR MYSQL");
            }
        
        }
}
 

