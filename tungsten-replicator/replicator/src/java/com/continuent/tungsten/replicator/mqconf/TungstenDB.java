
package com.continuent.tungsten.replicator.mqconf;

import java.sql.Connection;

import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TungstenDB
{
    private final static String   key = "key";
    private static TungstenDB      instace;
    private ComboPooledDataSource cpds;

    public TungstenDB() throws ApplierException
    {
        c3po();
    }

    public void c3po() throws ApplierException
    {
        try
        {
            TungstenCfg concfg = new TungstenCfg();
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
        }
        catch (Exception e)
        {
            throw new ApplierException("00001", "Tungsten数据库创建连接池失败", e);
        }
    }

    public static Connection getConn() throws ApplierException
    {
        try
        {
            return TungstenDB.getInstance().cpds.getConnection();
        }
        catch (Exception e)
        {
            throw new ApplierException("1003", "据库连接池获取连接失败", e);
        }
    }

    private static TungstenDB getInstance() throws ApplierException
    {
        if (instace == null)
        {
            synchronized (key)
            {
                if (instace == null)
                {
                    instace = new TungstenDB();
                    return instace;
                }
            }
        }
        return instace;
    }

    public static void main(String args[]) throws ApplierException
    {
        System.out.println(TungstenDB.getConn());
    }

}
