
package com.continuent.tungsten.replicator.mqservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqconf.PropertiesAttribute;
import com.continuent.tungsten.replicator.mqconf.Propertiesmgr;
import com.continuent.tungsten.replicator.mqtransform.TransformMgr;
import com.continuent.tungsten.replicator.mqutil.StringUtil;

public class TungstenService extends Thread
{
    private static Logger logger = Logger.getLogger(TungstenService.class);

    @Override
    public void run()
    {
        while (true)
        {
            try
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
                            TungstenPersistence tungsten = new TungstenPersistence(
                                    name);
                            List<TransformMgr> transList = tungsten
                                    .getUpdateTrans();
                            List<TransformMgr> deTransList = tungsten
                                    .getDeleteTrans();
                            try
                            {
                                /////
                                if (transList != null && transList.size() > 0)
                                {
                                    MQOracleApplier applier = new MQOracleApplier(
                                            transList);
                                    applier.applier();
                                    //
                                    List<Long> ids = new ArrayList<Long>();
                                    for(TransformMgr tem:transList){
                                        ids.add(tem.getErrorId());
                                    }
                                    new TungstenPersistence().delete(ids);
                                    //
                                }
                                ////////
                                if(deTransList != null && deTransList.size()>0){
                                    List<TransformMgr> temtransList = new ArrayList<TransformMgr>();
                                    List<Long> ids = new ArrayList<Long>();
                                    for(TransformMgr tem:temtransList ){
                                        temtransList.add(tem);
                                        MQOracleApplier applier = new MQOracleApplier(
                                                temtransList);
                                        applier.applier();
                                        temtransList.clear();
                                        //
                                        ids.add(tem.getErrorId());
                                        //
                                    }
                                    new TungstenPersistence().delete(ids);
                                }
                            }
                            catch (Exception e)
                            {
                                logger.error("恢复异常信息再次失败:" + e.getMessage());

                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("定时检查异常日志失败:" + e.getMessage());
            }
            try
            {
                this.sleep(1000 * 60 * 2);
            }
            catch (InterruptedException e)
            {
                logger.error("定时器错误:" + e.getMessage());
            }
        }
    }

}
