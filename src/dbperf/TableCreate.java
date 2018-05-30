package dbperf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.*;


public class TableCreate {
    //文件名为参数的第一个
    //第2,3,4分别为连接的url, 用户名和密码
    public static void main(String[] args) {
        if (args.length < 4){
            System.out.println(String.format("only %d arguments given", args.length));
            System.out.println("filename, url, username, pass needed");
            return;
        }
        Connection con = null;
        Statement stmt = null;
        String line = null;
        try {
            FileReader reader = new FileReader(args[0]);
            BufferedReader bufferReader = new BufferedReader(reader);
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection(args[1], args[2], args[3]);
            if(con != null){
                System.out.println("connecting succeeded");
            }
            //逐条执行文件中的语句
            stmt = con.createStatement();
            int statementCount = 0;
            while((line = bufferReader.readLine()) != null){
                stmt.executeUpdate(line);
                ++statementCount;
                if(statementCount % 1000 == 0){
                    System.out.println(String.format("%d statements executed", statementCount));
                }
            }
            bufferReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("file not found:" + args[0]);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
