
package com.continuent.tungsten.replicator.mqconf;

import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class Connectcfg
{
    private String  url;
    private String  user;
    private String  password;
    private String  port;
    private String  jdbcDriver;
    private Integer minPoolSize;
    private Integer maxPoolSize;
    private Integer increment;

    public String getUrl() throws ApplierException
    {

        buildUrl();

        return url;

    }

    private void buildUrl() throws ApplierException
    {
        StringBuffer sb = new StringBuffer();

        if ("mysql".equals(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_type)))
        {
            sb.append("jdbc:mysql://");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.snode_db_host));
            sb.append(":");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.pnode_db_port));
            sb.append("/");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.snode_db_instance));
        }
        if ("oracle".equals(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_type)))
        {
            sb.append("jdbc:oracle:thin:@");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.snode_db_host));
            sb.append(":");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.snode_db_port));
            sb.append(":");
            sb.append(Propertiesmgr.instace
                    .getValue(PropertiesAttribute.snode_db_instance));
        }
        url = sb.toString();

    }

    public String getJdbcDriver() throws ApplierException
    {
        if ("mysql".equals(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_type)))
        {
            jdbcDriver = "com.mysql.jdbc.Driver";
        }
        else if ("oracle".equals(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_type)))
        {
            jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        }

        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver)
    {
        this.jdbcDriver = jdbcDriver;
    }

    public String getUser() throws ApplierException
    {
        return Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_user);

    }

    public String getPassword() throws ApplierException
    {

        return Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_password);
    }

    public Integer getMinPoolSize() throws ApplierException
    {
        return Integer.valueOf(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_minpoolsize));
    }

    public Integer getMaxPoolSize() throws ApplierException
    {
        return Integer.valueOf(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_maxpoolsize));
    }

    public Integer getIncrement() throws ApplierException
    {
        return Integer.valueOf(Propertiesmgr.instace
                .getValue(PropertiesAttribute.snode_db_increament));

    }

}
