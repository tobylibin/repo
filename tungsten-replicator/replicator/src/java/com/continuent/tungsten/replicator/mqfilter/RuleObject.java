
package com.continuent.tungsten.replicator.mqfilter;

import java.util.ArrayList;
import java.util.List;

import com.continuent.tungsten.replicator.dbms.DBMSData;
import com.continuent.tungsten.replicator.dbms.OneRowChange;
import com.continuent.tungsten.replicator.dbms.OneRowChange.ColumnVal;
import com.continuent.tungsten.replicator.dbms.RowChangeData;
import com.continuent.tungsten.replicator.dbms.RowChangeData.ActionType;
import com.continuent.tungsten.replicator.event.DBMSEvent;
import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

public class RuleObject
{
    private String eventId;
//    private String schemaName;
    private String tableName;
    private String action;
    private List<String> values = new ArrayList<String>();
    
    public String getCode(){
        return tableName;
    }
    
    public RuleObject(){
        
    }

    public RuleObject(DBMSEvent event)
    {
        eventId = event.getEventId();
        for (DBMSData dataElem : event.getData())
        {
            if (dataElem instanceof RowChangeData)
            {
                for (OneRowChange row : ((RowChangeData) dataElem)
                        .getRowChanges())
                {
                   // schemaName = row.getSchemaName();
                    tableName = row.getTableName();
                    action = row.getAction().name();
                    //
                    if( ActionType.INSERT.name().equals(action)  ||  ActionType.UPDATE.name().equals(action)){
                        for (ArrayList<ColumnVal> cola : row.getColumnValues())
                        {
                             for(ColumnVal val :cola){
                                 values.add(String.valueOf(val.getValue()));
                             }
                        }
                    }else if( ActionType.DELETE.name().equals(action)){
                        for (ArrayList<ColumnVal> cola : row.getKeyValues())
                        {
                             for(ColumnVal val :cola){
                                 values.add(String.valueOf(val.getValue()));
                             }
                        } 
                    }
                }
            }
        }
    }

//    public String getSchemaName()
//    {
//        return schemaName;
//    }
//
//    public void setSchemaName(String schemaName)
//    {
//        this.schemaName = schemaName;
//    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getEventId()
    {
        return eventId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

}
