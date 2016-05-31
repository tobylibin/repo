
package com.continuent.tungsten.replicator.mqtask;

import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqconf.Enviroment;
import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class NodeTask
{
    private static Logger logger = Logger.getLogger(NodeTask.class);

    public static void main(String args[])
    {
        Node node;
        try
        {
            node = Enviroment.initNode();
            if (node instanceof PNode)
            {
                logger.info("初始化主节点环境配置信息");
            }
            else
            {
                logger.info("初始化子节点环境配置信息");
            }
            logger.info("节点服务已启动");
            node.start();
            if (node instanceof PNode)
            {
                logger.info("主节点服务启动成功");
            }
            else
            {
                logger.info("子节点服务启动成功");
            }
           
        }
        catch (ApplierException e)
        {
            logger.info("节点服务执行失败并退出");
            // 记录日志
            logger.error(e.getKey());
            logger.error(e.getMessage());
            System.exit(1);
        }

    }
}
