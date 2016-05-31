
package com.continuent.tungsten.replicator.mqconf;

import java.io.IOException;

import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqfilter.FilterManager;
import com.continuent.tungsten.replicator.mqfilter.SchemaFilter;
import com.continuent.tungsten.replicator.mqtask.Node;
import com.continuent.tungsten.replicator.mqtask.PNode;
import com.continuent.tungsten.replicator.mqtask.SNode;
import com.continuent.tungsten.replicator.mqtransform.RuleManager;

public class Enviroment
{
    public static Node initNode() throws   ApplierException
    {
        //初始化转换规则
        RuleManager.init();
        //初始化过滤器
        FilterManager.getInstance().getFilters().add(new SchemaFilter());
        //初始化化节点
        if (Propertiesmgr.getValue(PropertiesAttribute.pnode_service_name) != null)
        {
            return new PNode();
        }
        else
        {
            return new SNode();
        }
    }
}
