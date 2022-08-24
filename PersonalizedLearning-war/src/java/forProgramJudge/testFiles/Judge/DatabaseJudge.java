package forProgramJudge.testFiles.Judge;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.List;
import forProgramJudge.testFiles.tools.Tools;

/**
 *
 * @author haogs
 */
public class DatabaseJudge implements Judge{
   Tools tools=new Tools();
 Connection connection;
    Statement statement;
    ResultSet rs;
    public void judge(File file0, String stuNo,List fileNames, List sbscore,StringBuilder sbmemo) {
        /*
                1. 获得学生的学号
                2. 打开数据库服务
                3. 使用database="s学号ABC"，username=用户名为"s学号"，password=密码为"s学号"连接数据库或者使用桌面数据库的连接方式
                4. 连接到数据库，成功+5分
                5. 插入记录：
                insert into student学号DE values(8, ‘test’); （成功+8分）
                insert into subject学号FG values (8, ‘os’); （成功+7分）
                insert into score学号HI values(8,1,1,90); （成功+5分）
                insert into score学号HI values(8,18,18,90); （成功-5分因为外键不符合要求；成功+5分）
         */
            jdbcJudbePrepare(file0.getAbsolutePath(), fileNames, "S" + stuNo,sbscore,sbmemo);
    }

   

    private void jdbcJudbePrepare(String fileABCPath, List tables, String userName, List sbscore,StringBuilder sbmemo) {
        try {
            connection = DriverManager.getConnection("jdbc:derby:" + fileABCPath.replaceAll("\\\\", "/"));
            statement = connection.createStatement();
            sbscore.set(1,"5\t");//用JDBC连接到deby，成功+5分
            //查看3个表是否存在
            try {
                statement.executeUpdate("insert into " + userName + "." + (String)(tables.get(0)) + " values(8, 'test')");
                sbscore.set(2,"8\t");
            } catch (SQLSyntaxErrorException e) {
                try {
                    statement.executeUpdate("insert into APP." + (String)(tables.get(0)) + " values(8, 'test')");
                    sbscore.set(2,"8\t");
                } catch (SQLSyntaxErrorException e1) {
                    sbmemo.append("APP和学号都---student无法插入");
                }
            }
            try {
                statement.executeUpdate("insert into " + userName + "." + (String)(tables.get(1)) + " values(8, 'os')");
                sbscore.set(3,"7\t");
            } catch (SQLSyntaxErrorException e) {
                try {
                    statement.executeUpdate("insert into APP." + (String)(tables.get(1)) + " values(8, 'os')");
                    sbscore.set(3,"7\t");
                } catch (SQLSyntaxErrorException e1) {
                    sbmemo.append("APP和学号都---subject无法插入");
                }
            }
            try {
                statement.executeUpdate("insert into " + userName + "." + (String)(tables.get(2)) + " values(8, 1,1,90)");
                sbscore.set(4,"5\t");
            } catch (SQLSyntaxErrorException e) {
                try {
                    statement.executeUpdate("insert into APP." + (String)(tables.get(2)) + " values(8, 1,1,90)");
                    sbscore.set(4,"5\t");
                } catch (SQLSyntaxErrorException e1) {
                    sbmemo.append("APP和学号都---score无法插入；");
                }
            }
            try {
                statement.executeUpdate("insert into " + userName + "." + (String)(tables.get(2)) + " values(9, 18,18,90)");
                sbscore.set(5,"-5\t");//外键不正确
            } catch (SQLException ex) {
                try {
                    statement.executeUpdate("insert into APP." + (String)(tables.get(2)) + " values(9, 18,18,90)");
                    sbscore.set(5,"-5\t");//外键不正确
                } catch (SQLException e1) {
                    //System.out.println("外键正确");
                    sbscore.set(5,"5\t");
                }
            }
        } catch (SQLException ex) {
           sbmemo.append("访问数据库失败\t");
        } finally {
            try {
                // rs.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }
}
