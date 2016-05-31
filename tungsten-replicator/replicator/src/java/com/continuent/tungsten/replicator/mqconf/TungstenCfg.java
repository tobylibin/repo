
package com.continuent.tungsten.replicator.mqconf;

import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class TungstenCfg
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

        sb.append("jdbc:mysql://");
        sb.append(Propertiesmgr.instace
                .getValue(PropertiesAttribute.tungsten_db_host));
        sb.append(":");
        sb.append(Propertiesmgr.instace
                .getValue(PropertiesAttribute.tungsten_db_port));
        sb.append("/tungsten");
        url = sb.toString();

    }

    public String getJdbcDriver() throws ApplierException
    {
        jdbcDriver = "com.mysql.jdbc.Driver";
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver)
    {
        this.jdbcDriver = jdbcDriver;
    }

    public String getUser() throws ApplierException
    {
        return Propertiesmgr.instace
                .getValue(PropertiesAttribute.tungsten_db_user);

    }

    public String getPassword() throws ApplierException
    {

        return Propertiesmgr.instace
                .getValue(PropertiesAttribute.tungsten_db_password);
    }

    public Integer getMinPoolSize() throws ApplierException
    {
        return 1;
    }

    public Integer getMaxPoolSize() throws ApplierException
    {
        return 5 ;
    }

    public Integer getIncrement() throws ApplierException
    {
        return 1;

    }

}
