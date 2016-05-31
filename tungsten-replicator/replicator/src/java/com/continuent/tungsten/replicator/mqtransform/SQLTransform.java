
package com.continuent.tungsten.replicator.mqtransform;

import java.util.ArrayList;
import java.util.List;

import com.continuent.tungsten.replicator.mqfilter.RuleObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("rule")
public class SQLTransform
{
    @XStreamAlias("source.schema")
    private String          sourceSchema;
    @XStreamAlias("source.table")
    private String          sourceTable;
    @XStreamAlias("target.table")
    private String          targetTable;

    @XStreamAlias("extract.values")
    private List<SQLValues> extractValues = new ArrayList<SQLValues>();

    @XStreamAlias("apply.values")
    private List<SQLValues> applyValues   = new ArrayList<SQLValues>();

    private String          statement;

    private RuleObject      ruleObject;

    private String          delete;

    public String getSourceSchema()
    {
        return sourceSchema;
    }

    public void setSourceSchema(String sourceSchema)
    {
        this.sourceSchema = sourceSchema;
    }

    public String getSourceTable()
    {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable)
    {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable()
    {
        return targetTable;
    }

    public void setTargetTable(String targetTable)
    {
        this.targetTable = targetTable;
    }

    public List<SQLValues> getExtractValues()
    {
        return extractValues;
    }

    public void setExtractValues(List<SQLValues> extractValues)
    {
        this.extractValues = extractValues;
    }

    public String getStatement()
    {
        return statement;
    }

    public void setStatement(String statement)
    {
        this.statement = statement;
    }

    public List<SQLValues> getApplyValues()
    {
        return applyValues;
    }

    public void setApplyValues(List<SQLValues> applyValues)
    {
        this.applyValues = applyValues;
    }

    public RuleObject getRuleObject()
    {
        return ruleObject;
    }

    public void setRuleObject(RuleObject ruleObject)
    {
        this.ruleObject = ruleObject;
    }

    public String getDelete()
    {
        return delete;
    }

    public void setDelete(String delete)
    {
        this.delete = delete;
    }

    public static void main(String args[])
    {
        System.out.println(SQLType.STRING.name());
    }

}
