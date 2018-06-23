package dbperf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.*;

public class QueryTest {
    /**
     * 前3个参数分别为url, usr, pass
     * 第4个参数为sql语句
     */
    public static void main(String[] args){
        if(args.length < 4){
            System.out.println("invalid parameter");
            return;
        }
        Connection con = null;
        Statement stmt = null;
        try{
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection(args[0], args[1], args[2]);
            stmt = con.createStatement();
            long t1 = System.currentTimeMillis();
            ResultSet r = stmt.executeQuery(args[3]);
            long t2 = System.currentTimeMillis();
            dump(r);
            System.out.println(String.format("total: %d milliseconds", t2 - t1));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void dump(ResultSet rs) throws SQLException {
        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o = null;
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }
}