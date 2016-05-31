
package com.continuent.tungsten.replicator.mqutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.continuent.tungsten.replicator.mqfilter.Schema;
import com.continuent.tungsten.replicator.mqtransform.SQLTransform;
import com.continuent.tungsten.replicator.mqtransform.SQLOperation;
import com.continuent.tungsten.replicator.mqtransform.SQLRule;
import com.continuent.tungsten.replicator.mqtransform.SQLValues;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XMLUtils
{
    public static XStream xStream = new XStream(new DomDriver("utf8"));
    static{
        xStream.autodetectAnnotations(true);
    }
   
    public static String toXml(Object obj)
    {
        xStream.alias("dbschema", Schema.class);
        xStream.alias("sql.rule", SQLRule.class);
        xStream.alias("rule", SQLTransform.class);
        xStream.alias("value", SQLValues.class);
        xStream.alias("operation", SQLOperation.class);
        xStream.alias("sql.transform",Map.class);
        xStream.alias("key",String.class);
        return xStream.toXML(obj);
    }

    public static void writeFile(Object obj,String file)
    {
        PrintWriter pw = null;
        try
        {
            xStream.alias("sql.transform",Map.class);
            xStream.alias("key",String.class);
            pw = new PrintWriter( file, "UTF-8");
            xStream.toXML(obj, pw);
            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }
        }
        new File(file).renameTo(new File(file + ".xml"));
    }

    public static Object toObject(String xml)
    {
        xStream.alias("dbschema", Schema.class);
        xStream.alias("sql.rule", SQLRule.class);
        xStream.alias("rule", SQLTransform.class);
        xStream.alias("value", SQLValues.class);
        xStream.alias("operation", SQLOperation.class);
        xStream.alias("sql.transform",Map.class);
        xStream.alias("key",String.class);
        return xStream.fromXML(xml);
    }

    public static Object toObject(InputStream is)
    {
        
        xStream.alias("dbschema", Schema.class);
        xStream.alias("sql.rule", SQLRule.class);
        xStream.alias("rule", SQLTransform.class);
        xStream.alias("value", SQLValues.class);
        xStream.alias("operation", SQLOperation.class);
        xStream.alias("sql.transform",Map.class);
        xStream.alias("key",String.class);
        Object obj =  xStream.fromXML(is);
        
        
        return obj;
    }

    public static File[] readExtractorFile()
    {

        File file = new File("extractormsg");
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            return files;
        }
        return null;
    }

    public static void main(String args[])
    {
        File file = new File("extractormsg");
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            System.out.println(files);

        }
    }
}
