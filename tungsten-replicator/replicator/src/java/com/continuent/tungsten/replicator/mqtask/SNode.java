
package com.continuent.tungsten.replicator.mqtask;

import com.continuent.tungsten.replicator.mqconf.PropertiesAttribute;
import com.continuent.tungsten.replicator.mqconf.Propertiesmgr;
import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqservice.MQConsummer;
import com.continuent.tungsten.replicator.mqservice.TungstenService;
import com.continuent.tungsten.replicator.mqutil.StringUtil;

public class SNode implements Node
{

    @Override
    public void start() throws ApplierException
    {
        doApplier();
        //new TungstenService().start();
    }

    private void doApplier() throws ApplierException
    {
        String queues = Propertiesmgr
                .getValue(PropertiesAttribute.middle_mq_queues);
        if (StringUtil.notEmpty(queues))
        {
            String[] queueNames = queues.split(",");
            for (String name : queueNames)
            {
                if (StringUtil.notEmpty(name))
                {
                    new MQConsummer(name.trim()).start();
                }
            }
        }

    }

}
