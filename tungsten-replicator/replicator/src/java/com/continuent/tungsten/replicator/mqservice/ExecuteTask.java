
package com.continuent.tungsten.replicator.mqservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqfilter.RuleObject;
import com.continuent.tungsten.replicator.mqutil.LogerEvent;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;

public class ExecuteTask extends Thread
{

    private static Logger            logger      = Logger.getLogger(ExecuteTask.class);
    // public List<String> eventLogs = new ArrayList<String>();
    public static long               extractTime = 0;
    // 表名+内容
    public Map<String, List<String>> eventLogMap = new HashMap<String, List<String>>();
    public String                    tempEventID = null;
    public int                       counter     = 0;

    // public static List<String> eventIdList = new ArrayList<String>();

    @Override
    public void run()
    {
        while (true)
        {
            //判断集合和更新时间
            if (eventLogMap.size() > 0 && extractTime != 0
                    && new Date().getTime() - extractTime > 1000 * 6)
            {
                doEvent(eventLogMap);
            }
            try
            {
                Thread.sleep(1000 * 3);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void clear()
    {
        eventLogMap.clear();
        tempEventID = null;
        counter = 0;
        extractTime = 0;
    }

    public synchronized void doEvent(Object obj)
    {
        try
        {

            if (obj instanceof RuleObject)
            {
                // 记录事件ID
                tempEventID = ((RuleObject) obj).getEventId();
                // 记录数据
                if (eventLogMap.get(((RuleObject) obj).getTableName()) == null)
                {
                    List<String> content = new ArrayList<String>();
                    content.add(XMLUtils.toXml((RuleObject) obj));
                    eventLogMap.put(((RuleObject) obj).getTableName(), content);
                }
                else
                {
                    eventLogMap.get(((RuleObject) obj).getTableName()).add(
                            XMLUtils.toXml((RuleObject) obj));
                }
                counter++;
            }
            else if (eventLogMap instanceof Map)
            {
                if (eventLogMap.size() > 0)
                {
                    System.out.println("BATCH NUM:"+counter);
                    // 根据表名批量发送数据
                    MQProducer.getInstance().send(eventLogMap);
                    LogerEvent.logEventID(tempEventID, true);
                    clear();
                }

            }
        }
        catch (ApplierException e)
        {
            e.printStackTrace();
            try
            {
                logger.error("30秒后系统尝试连接MQ服务,继续发送当前未发送成功的消息:");
                logger.error(e.getErrorMsg());
                Thread.sleep(1000 * 30);
                doEvent(eventLogMap);
            }
            catch (InterruptedException e1)
            {
                logger.error(e1.getMessage());
            }
        }
    }
}
