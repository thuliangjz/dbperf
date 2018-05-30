package org.hsqldb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class TestConnection {
   public static void main(String[] args) {
      System.out.println("hello!");
      Connection con = null;
      Statement stmt = null;
      int result = 0;
      try {
         //Registering the HSQLDB JDBC driver
         Class.forName("org.hsqldb.jdbc.JDBCDriver");
         //Creating the connection with HSQLDB
         con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/db1", "SA", "");
         if (con!= null){
            System.out.println("Connection created successfully");
            
         }else{
            System.out.println("Problem with creating connection");
         }
         //create statement
         boolean isAutoCommit = con.getAutoCommit();
         if(isAutoCommit) {
        	 System.out.println("Auto Commit mode opened, close now");
        	 con.setAutoCommit(false);
         }
         stmt = con.createStatement();
         String tableName = "join_test_3";
         result = stmt.executeUpdate(String.format("DROP TABLE %s IF EXISTS", tableName));
         result = stmt.executeUpdate(String.format("CREATE cached TABLE %s (" +
                 "id INT NOT NULL, code VARCHAR(50) NOT NULL," +
                 "PRIMARY KEY (id));", tableName));
         int cnt_insert = 20;
         for(int i = 0; i < cnt_insert; ++i) {
        	 String s = String.format("insert into %s values (%d, 'codeabcdeffffghhhhhhicdeidfsa%d')",tableName, i, 3*i);
        	 stmt.executeUpdate(s);
         }
         con.commit();
         System.out.println("Insertion complete");
         //con.rollback();
      }  catch (Exception e) {
         //e.printStackTrace(System.out);
	 System.out.println("ooooooh, error seems occured when connecting database");

      }
      //
   }
}
