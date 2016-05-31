
package com.continuent.tungsten.replicator.mqexception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApplierException extends Exception
{
    private String errorMsg;
    private String name;
    private String key;

    public ApplierException(String key, String name, Exception e)
    {
        this.key = key;
        this.name = name;

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        errorMsg = sw.toString();
        try
        {
            if (sw != null)
            {
                sw.close();
            }
            if (pw != null)
            {
                pw.close();
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

    }

    @Override
    public String getMessage()
    {
        return errorMsg;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
