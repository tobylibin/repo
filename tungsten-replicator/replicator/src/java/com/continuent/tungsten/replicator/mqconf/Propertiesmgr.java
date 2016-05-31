
package com.continuent.tungsten.replicator.mqconf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class Propertiesmgr
{

    private final static String key              = "key";

    public static Propertiesmgr instace;

    private Map<String, Object> properties       = new HashMap<String, Object>(); ;

    public final String         dbpropertiesFile = "replicator.db.properties";

    private static Propertiesmgr instance() throws ApplierException  
    {
        if (instace == null)
        {
            synchronized (key)
            {
                if (instace == null)
                {
                    instace = new Propertiesmgr();
                    instace.loadProperties();
                    return instace;
                }
            }
        }
        return instace;
    }

    public static String getValue(String key) throws ApplierException  
    {
        return (String) instance().properties.get(key);
    }

    public void loadProperties() throws ApplierException  
    {
        if (properties.isEmpty())
        {
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(dbpropertiesFile);
            }
            catch (FileNotFoundException e)
            {
                throw new ApplierException("1004","replicator.db.properties文件未找到", e);
            }
            load(fis, false);
        }

    }

    public void load(InputStream is, boolean doSubstitutions)

    {
        try
        {
            // Load the properties file.
            Properties props = new Properties();
            props.load(is);
            // if (doSubstitutions)
            // substituteSystemValues(props);
            add(props);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    public void add(Properties props)
    {
        Enumeration<?> keys = props.propertyNames();
        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            String value = props.getProperty(key).toString();
            if (properties.get(key) != null)
            {
                /*
                 * if (logger.isDebugEnabled()) {
                 * logger.debug(String.format("Replacing %s=%s with %s=%s", key,
                 * properties.get(key), key, value)); }
                 */
            }

            properties.put(key, value);
        }
    }

    public static void main(String args[]) throws ApplierException  
    {
        Propertiesmgr pmgr = Propertiesmgr.instance();
        System.out.println(pmgr.properties.get("pnode.db.type"));
    }
}
