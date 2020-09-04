package smsgateway.pengaturan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Koneksi {
     String url = "jdbc:mysql://localhost/db_gateway";
     String user = "root";
     String password = "";
     
     public Connection getConnection(){
         Connection conn = null;
         try {
             conn = DriverManager.getConnection(url, user, password);
         } catch (SQLException ex) {
             Logger.getLogger(Koneksi.class.getName()).log(Level.SEVERE,null,ex);
         }
         return conn;
     }
}
