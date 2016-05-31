
package com.continuent.tungsten.replicator.mqfilter;


/**
 * 过滤需要监听的表 schema name + table name This class defines a TableFilter
 * 
 * @author <a href="mailto:jussi-pekka.kurikka@continuent.com">Jussi-Pekka
 *         Kurikka</a>
 * @version 1.0
 */
public class SchemaFilter implements Filter
{
    public SchemaFilter()
    {
        Schema.parse();
    }

    @Override
    public int filter(RuleObject rule)
    {
        if (Schema.instance != null)
        {

            if (Schema.instance.pass(rule.getTableName()))
            {
                return RuleStatus.success;
            }

            return RuleStatus.failed;
        }
        else
        {
            return RuleStatus.nochek;
        }
    }

}
