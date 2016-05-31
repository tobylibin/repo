
package com.continuent.tungsten.replicator.mqfilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.continuent.tungsten.replicator.mqutil.XMLUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dbschema")
public class Schema
{
    private static String ruleFile     = "rule/jr.dbschema.xml";

    public static Schema  instance;

    private List<String>  schemaTable = new ArrayList<String>();

    public boolean pass(String mapper)
    {
        return schemaTable.contains(mapper);
    }

    public static void   parse()
    {
        if (instance == null && new File(ruleFile).exists())
        {
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(ruleFile);
                instance = (Schema) XMLUtils.toObject(fis);
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
    }

    public List<String> getSchemaTable()
    {
        return schemaTable;
    }

    public void setSchemaTable(List<String> schemaTable)
    {
        this.schemaTable = schemaTable;
    }

    public static void main(String args[])
    {
         Schema t = new Schema();
         t.schemaTable.add("fxs.testtable");
         t.schemaTable.add("fxs.testtable");
         XMLUtils.writeFile(t, "rule/jr.dbschema");
        //System.out.println(Schema.parse().pass("fxs.testtable"));
    }
}
