
package com.continuent.tungsten.replicator.mqtask;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqservice.MQMySQLExtractor;

public class PNode implements Node
{
    private static Logger logger = Logger.getLogger(PNode.class);
    @Override
    public void start() throws ApplierException
    {
        logger.info("启动抽取模块功能");
        MQMySQLExtractor extractor = new MQMySQLExtractor();
        extractor.start();
    }
}
