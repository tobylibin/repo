
package com.continuent.tungsten.replicator.mqservice;

import java.util.Date;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.conf.ReplicatorRuntime;
import com.continuent.tungsten.replicator.event.DBMSEvent;
import com.continuent.tungsten.replicator.extractor.ExtractorWrapper;
import com.continuent.tungsten.replicator.extractor.mysql.MySQLExtractor;
import com.continuent.tungsten.replicator.mqconf.Runtimemgr;
import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqfilter.FilterManager;
import com.continuent.tungsten.replicator.mqfilter.RuleObject;
import com.continuent.tungsten.replicator.mqutil.EventIdLoad;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;
import com.continuent.tungsten.replicator.pipeline.Pipeline;

public class MQMySQLExtractor
{
    private static String  eventId;
    private static Logger  logger = Logger.getLogger(MQMySQLExtractor.class);
    private MySQLExtractor extractor;
    public static long                   t1     = 0;

    public MQMySQLExtractor() throws ApplierException

    {
        Runtimemgr runtimeMgr = new Runtimemgr();
        ReplicatorRuntime runtime = runtimeMgr.getRuntime();
        extractor = getMySQLExtractor(runtime);
        extractor.setStrictVersionChecking(false);
        try
        {
            extractor.prepare(runtime);
        }
        catch (ReplicatorException e)
        {
            throw new ApplierException("1008", "抽取模块prepare失败", e);
        }
    }

    public void start() throws ApplierException
    {
        // 启动检查
        ExecuteTask task = new ExecuteTask();
        task.start();
        //
        while (true)
        {
            DBMSEvent event = null;
            try
            {
                if (eventId == null)
                {
                    String logEventId = EventIdLoad.getLogLastEventId();
                    if (logEventId != null)
                    {
                        extractor.setLastEventId(logEventId);
                    }
                }
                event = extractor.extract();
                //获取最新的事件
                eventId = event.getEventId();
                // test1
                if (t1 == 0)
                {
                    t1 = new Date().getTime();
                    logger.info("BATCH TIME START : " + t1);
                }
                // test1
                //更新抽取时间
                ExecuteTask.extractTime = new Date().getTime();
            }
            catch (Exception e)
            {
                // 记录日志的地方
                // "1009","抽取模块抽取日志失败"
                logger.error("抽取Mysqlbinlog日志失败");
                logger.error(e.getMessage());
                throw new ApplierException("1009", "抽取模块抽取日志失败", e);
            }
            //
            RuleObject rule = new RuleObject(event);
            // 添加过滤器
            if (!FilterManager.instace.invoke(rule))
            {
                continue;
            }
            //添加xml数据
            task.doEvent(rule);
            // stest
            if (task.counter >= 4000)
            {
                task.doEvent(task.eventLogMap);
                // 抽取数据每批次响应时间
                logger.info("BATCH TIME IS : " + (new Date().getTime() - t1));
                t1 = 0;
                //
            }
            else
            {
                continue;
            }
        }
    }

    /*
     * private boolean convertAndSend(RuleObject rule) { try { if
     * ("instance".equals(Propertiesmgr
     * .getValue(PropertiesAttribute.middle_mq_queue_create_type))) {
     * MQProducer.getInstance().send(rule.getSchemaName(),
     * XMLUtils.toXml(rule)); } else if ("schema".equals(Propertiesmgr
     * .getValue(PropertiesAttribute.middle_mq_queue_create_type))) {
     * MQProducer.getInstance().send(rule.getSchemaName(),
     * XMLUtils.toXml(rule)); } else if ("table".equals(Propertiesmgr
     * .getValue(PropertiesAttribute.middle_mq_queue_create_type))) {
     * MQProducer.getInstance().send( rule.getSchemaName() + "." +
     * rule.getTableName(), XMLUtils.toXml(rule)); } return true; } catch
     * (Exception e) { e.printStackTrace(); logger.error(e.getMessage()); }
     * return false; }
     */

    private MySQLExtractor getMySQLExtractor(ReplicatorRuntime runtime)
    {
        Pipeline p = runtime.getPipeline();

        ExtractorWrapper wrapper = (ExtractorWrapper) p.getStages().get(0)
                .getExtractor0();
        return (MySQLExtractor) wrapper.getExtractor();
    }

}

