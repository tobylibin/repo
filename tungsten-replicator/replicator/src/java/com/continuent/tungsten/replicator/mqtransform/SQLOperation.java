
package com.continuent.tungsten.replicator.mqtransform;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("operation")
public class SQLOperation
{
    // 连接  
    private String connect;
    // 直接填写替换的值
    private String replace;
    private String longToDate;
    // 数字加
    private String plus;
    // 数字减
    private String sub;

    public String getConnect()
    {
        return connect;
    }

    public void setConnect(String connect)
    {
        this.connect = connect;
    }

    public String getReplace()
    {
        return replace;
    }

    public void setReplace(String replace)
    {
        this.replace = replace;
    }

    public String getLongToDate()
    {
        return longToDate;
    }

    public void setLongToDate(String longToDate)
    {
        this.longToDate = longToDate;
    }

    public String getPlus()
    {
        return plus;
    }

    public void setPlus(String plus)
    {
        this.plus = plus;
    }

    public String getSub()
    {
        return sub;
    }

    public void setSub(String sub)
    {
        this.sub = sub;
    }

}
