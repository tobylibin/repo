
package com.continuent.tungsten.replicator.mqconf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManager
{
    private final static String   key = "key";
    private static DBManager      instace;
    private ComboPooledDataSource cpds;

    public DBManager() throws ApplierException
    {
        c3po();
    }

    public void c3po() throws ApplierException
    {
        try
        {
            Connectcfg concfg = new Connectcfg();
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(concfg.getJdbcDriver()); // loads the jdbc
                                                         // driver
            cpds.setJdbcUrl(concfg.getUrl());
            cpds.setUser(concfg.getUser());
            cpds.setPassword(concfg.getPassword());
            // the settings below are optional -- c3p0 can work with defaults
            cpds.setMinPoolSize(concfg.getMinPoolSize());
            cpds.setAcquireIncrement(concfg.getIncrement());
            cpds.setMaxPoolSize(concfg.getMaxPoolSize());
            cpds.setInitialPoolSize(1);
            // jdbc:oracle:thin:@106.2.60.124:1521:orcl
        }
        catch (Exception e)
        {
            throw new ApplierException("1002", "数据库连接池失败", e);
        }
    }

    public static Connection getConn() throws ApplierException
    {
        try
        {
            return DBManager.getInstance().cpds.getConnection();
        }
        catch (Exception e)
        {
            throw new ApplierException("1003", "据库连接池获取连接失败", e);
        }
    }

    private static DBManager getInstance() throws ApplierException
    {
        if (instace == null)
        {
            synchronized (key)
            {
                if (instace == null)
                {
                    instace = new DBManager();
                    return instace;
                }
            }
        }
        return instace;
    }

    private static Connection getJDBCConn()
    {
        Connectcfg concfg = new Connectcfg();
        try
        {
            Class.forName(concfg.getJdbcDriver());
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ApplierException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try
        {
            Connection conn = DriverManager.getConnection(concfg.getUrl(),
                    concfg.getUser(), concfg.getPassword());
            conn.setAutoCommit(false);
            Statement st = conn.createStatement();
            st.addBatch("insert into t1(id,name)values(32,'dddd')");
            st.addBatch("insert into t1(id,name)values(33,'dddd')");
            // PreparedStatement ps =
            // conn.prepareStatement("insert into t1(id,name)values(31,'dddd')");
            // ps.addBatch("insert into t1(id,name)values(13,'dddd')");
            // ps.setString(2, "fdsfwefew");
            // ps.setInt(1, 1);
            // ps.addBatch();
            //
            // ps.setString(2, "fdsfwefew");
            // ps.setInt(1, 11);
            // ps.addBatch();

            st.executeBatch();
            conn.commit();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ApplierException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String args[])
    {
        DBManager.getJDBCConn();
    }

}
