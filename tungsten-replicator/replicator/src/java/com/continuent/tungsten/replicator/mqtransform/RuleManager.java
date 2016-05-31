
package com.continuent.tungsten.replicator.mqtransform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.database.DatabaseFactory;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;

public class RuleManager
{
    private static String             transFile   = "rule/jr.sql.xml";
    private final static String       key         = "key";
    private static RuleManager        instace;
    private Map<String, SQLTransform> jrtransform = null;

    public RuleManager()
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(transFile);
            jrtransform = (Map<String, SQLTransform>) XMLUtils.toObject(fis);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void init()
    {
        RuleManager.getInstance();
    }

    public static SQLTransform getRules(String key)
    {
        return RuleManager.getInstance().jrtransform.get(key);
    }

    private static RuleManager getInstance()
    {
        if (instace == null)
        {
            synchronized (key)
            {
                if (instace == null)
                {
                    instace = new RuleManager();
                    return instace;
                }
            }
        }
        return instace;
    }

}
