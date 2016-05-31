
package com.continuent.tungsten.replicator.mqtransform;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("value")
public class SQLValues
{
    private String             index;
    private String             type;
    private List<SQLOperation> operations = new ArrayList<SQLOperation>();

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public List<SQLOperation> getOperations()
    {
        return operations;
    }

    public void setOperations(List<SQLOperation> operations)
    {
        this.operations = operations;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

     
 
}
