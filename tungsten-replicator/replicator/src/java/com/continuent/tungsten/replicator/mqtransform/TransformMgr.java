
package com.continuent.tungsten.replicator.mqtransform;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.continuent.tungsten.replicator.mqfilter.RuleObject;
import com.continuent.tungsten.replicator.mqutil.StringUtil;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;

public class TransformMgr
{
    private Long errorId;
    private String errorAction;
    //
    private RuleObject ruleObject;
    private String     exesql;
    
    public TransformMgr(RuleObject ruleObject)
    {
        setExeSql(ruleObject);

    }

    private void setExeSql(RuleObject ruleObject)
    {
        this.ruleObject = ruleObject;
        if (ActinionType.DELETE.name().equals(ruleObject.getAction()))
        {
            exesql = RuleManager.getRules(ruleObject.getCode()).getDelete();
        }
        else
        {
            exesql = RuleManager.getRules(ruleObject.getCode()).getStatement();
        }
    }

    public void setParam(PreparedStatement ps) throws SQLException
    {

        if (exesql.contains("?"))
        {
            SQLTransform trans = RuleManager.getRules(ruleObject.getCode());
            // 计算？号的个数，可直接从appliervalues属性中计算出来
            int valueLength = trans.getApplyValues().size();
            int insertPos = 1;
            while (true)
            {
                SQLValues sqlValue = trans.getApplyValues().get(insertPos - 1);
                Integer index = Integer.valueOf(sqlValue.getIndex());
                String pvalue = ruleObject.getValues().get(index);
                // 计算值符号
                operationValues(sqlValue, pvalue);
                //
                if (SQLType.STRING.name().equals(
                        sqlValue.getType().trim().toUpperCase()))
                {
                    ps.setString(insertPos, pvalue);
                }
                else if (SQLType.INTEGER.name().equals(
                        sqlValue.getType().trim().toUpperCase()))
                {
                    ps.setInt(insertPos, Integer.valueOf(pvalue));
                }
                else if (SQLType.LONG.name().equals(
                        sqlValue.getType().trim().toUpperCase()))
                {
                    ps.setLong(insertPos, Long.valueOf(pvalue));
                }

                else if (SQLType.DATE.name().equals(
                        sqlValue.getType().trim().toUpperCase()))
                {
                    ps.setTimestamp(insertPos, Timestamp.valueOf(pvalue));
                }
                //
                insertPos++;
                if (insertPos > valueLength)
                {
                    return;
                }
            }

        }
    }

    public String getPreparestatementSql()
    {
        SQLTransform trans = RuleManager.getRules(ruleObject.getCode());
        // 保存与更新数据
        for (SQLValues sqlValue : trans.getExtractValues())
        {
            // 获取"$"符号以便替换值
            Integer index = Integer.valueOf(sqlValue.getIndex());
            String pvalue = ruleObject.getValues().get(index);
            operationValues(sqlValue, pvalue);
            // 替换抽取值
            if (exesql.contains("$" + index))
            {
                if (StringUtil.empty(pvalue))
                {
                    pvalue = "null";
                }
                exesql = exesql.replace("$" + index, pvalue);
            }
            //
        }
        //
        return exesql;
    }

    private void operationValues(SQLValues sqlValue, String pvalue)
    {
        if (sqlValue.getOperations() != null
                && sqlValue.getOperations().size() > 0)
        {
            for (SQLOperation oper : sqlValue.getOperations())
            {
                if (StringUtil.notEmpty(oper.getConnect()))
                {
                    pvalue = oper.getConnect().replace("%%", pvalue);
                }
                if (StringUtil.notEmpty(oper.getLongToDate()))
                {

                }
                if (StringUtil.notEmpty(oper.getReplace()))
                {
                    pvalue = pvalue.replace(pvalue, oper.getReplace());
                }
            }
        }
    }

    public RuleObject getRuleObject()
    {
        return ruleObject;
    }

    public void setRuleObject(RuleObject ruleObject)
    {
        this.ruleObject = ruleObject;
    }

    public String getExesql()
    {
        return exesql;
    }

    public void setExesql(String exesql)
    {
        this.exesql = exesql;
    }

    public Long getErrorId()
    {
        return errorId;
    }

    public void setErrorId(Long errorId)
    {
        this.errorId = errorId;
    }

    public String getErrorAction()
    {
        return errorAction;
    }

    public void setErrorAction(String errorAction)
    {
        this.errorAction = errorAction;
    }

}
