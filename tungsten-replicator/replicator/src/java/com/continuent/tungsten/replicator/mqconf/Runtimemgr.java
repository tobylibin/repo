
package com.continuent.tungsten.replicator.mqconf;

import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.conf.ReplicatorMonitor;
import com.continuent.tungsten.replicator.conf.ReplicatorRuntime;
import com.continuent.tungsten.replicator.management.MockOpenReplicatorContext;
import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class Runtimemgr
{
    public ReplicatorRuntime getRuntime() throws ApplierException
    {
        RuntimeProperties rp = new RuntimeProperties();
        ReplicatorRuntime runtime = new ReplicatorRuntime(rp.getConfig(),
                new MockOpenReplicatorContext(),
                ReplicatorMonitor.getInstance());
        try
        {
            runtime.configure();

            runtime.prepare();

        }
        catch (ReplicatorException e)
        {
            e.printStackTrace();
            throw new ApplierException("1006","监听日志运行环境配置失败",e);
        }
     
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ApplierException("1007","监听日志prepare失败",e);
        }
        return runtime;
    }
}
