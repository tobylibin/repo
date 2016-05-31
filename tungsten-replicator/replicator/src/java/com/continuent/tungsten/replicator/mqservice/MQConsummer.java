
package com.continuent.tungsten.replicator.mqservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Message;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;

import com.continuent.tungsten.replicator.mqconf.PropertiesAttribute;
import com.continuent.tungsten.replicator.mqconf.Propertiesmgr;
import com.continuent.tungsten.replicator.mqexception.ApplierException;
import com.continuent.tungsten.replicator.mqfilter.RuleObject;
import com.continuent.tungsten.replicator.mqtransform.ActinionType;
import com.continuent.tungsten.replicator.mqtransform.TransformMgr;
import com.continuent.tungsten.replicator.mqutil.XMLUtils;

public class MQConsummer extends Thread
{
    private static Logger                   logger    = Logger.getLogger(MQConsummer.class);
    private Connection                      connection;
    private String                          queueName;
    // private List<TransformMgr> transList = new ArrayList<TransformMgr>();
    private Integer                         batchNum  = null;
    private Map<String, List<TransformMgr>> groupData = new HashMap<String, List<TransformMgr>>();
    private Integer                         counter   = 0;
    private Session                         session   = null;
    
    private   Map<String, Long> counterMap = new HashMap<String, Long>() ;
    //
    

    public MQConsummer(String queueName) throws NumberFormatException, ApplierException
    {
        this.queueName = queueName;
        batchNum = Integer.valueOf(Propertiesmgr
                .getValue(PropertiesAttribute.snode_db_batchnum));
        counterMap.put(queueName, 0L);
    }

    private void connection() throws ApplierException
    {
        //failover:(tcp://localhost:61616,tcp://localhost:61617) 
        //Create a ConnectionFactory 
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "failover:("
                        + Propertiesmgr
                                .getValue(PropertiesAttribute.middle_mq_address)
                        + ")");

        try
        {
            RedeliveryPolicyMap map = connectionFactory
                    .getRedeliveryPolicyMap();
            RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
            queuePolicy.setMaximumRedeliveries(10000000);
            map.put(new ActiveMQQueue(">"), queuePolicy);
            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();
            // Create a Session
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queueName);
            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);
            // consumer.setMessageListener(this);

            while (true)
            {
                Message message = (Message) consumer.receive(6 * 1000);
                if (message != null)
                {
                    TextMessage textMessage = (TextMessage) message;
                    //
                    counter++;
                    //s test
                    if(counter < 2000){
                       
                         counterMap.put(queueName, counterMap.get(queueName).longValue()+1);
                         continue;                      
                    }
                    if(true){
                        counterMap.put(queueName, counterMap.get(queueName).longValue()+1);
                        if (counter > 0){
                            logger.info("1table("+queueName+") counter : "+counter+";total :"+counterMap.get(queueName));
                        }
                       session.commit();
                       counter = 0;
                       continue;
                    }
                    //e test
                    //
                    RuleObject rule = (RuleObject) XMLUtils
                            .toObject(textMessage.getText());
                    //
                    if (groupData.get(rule.getCode()) == null)
                    {
                        List<TransformMgr> dataList = new ArrayList<TransformMgr>();
                        dataList.add(new TransformMgr(rule));
                        groupData.put(rule.getCode(), dataList);
                    }
                    else
                    {
                        groupData.get(rule.getCode()).add(
                                new TransformMgr(rule));
                    }
                    //判断不是删除操作则继续添加到缓存，如果是删除操作则执行数据加载
                    if (!ActinionType.DELETE.name().equals(rule.getAction()))
                    {
                        if (counter < batchNum)
                        {
                            continue;
                        }
                    }

                }
                
                //s test 默认不走
                if(true){
                    if (counter > 0){
                        logger.info("2table("+queueName+") counter : "+counter+" ; total :"+counterMap.get(queueName));
                    }
                   session.commit();
                   counter = 0 ;
                   continue;
               }
               //e test
                if (counter > 0)
                {
                    loadData();
                    counter=0;
                }
                groupData.clear();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (e instanceof ApplierException)
            {
                throw (ApplierException) e;
            }
            else
            {
                throw new ApplierException("1005", "连接MQ服务失败，获取消息失败",
                        e);
            }
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (JMSException e)
                {
                    e.printStackTrace();
                }
            }
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (JMSException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    private void loadData()
    {
        Collection<List<TransformMgr>> c = groupData.values();
        Iterator it = c.iterator();
        List<TransformMgr> transList = null;
        try
        {
            while (it.hasNext())
            {
                transList = (List<TransformMgr>) it.next();
                MQOracleApplier applier = new MQOracleApplier(transList);
                // MQOracleApplier applier = new MQOracleApplier(transList);
                applier.applier();
                session.commit();

            }
        }
        catch (Exception e)
        {
            try
            {
                session.rollback();
            }
            catch (JMSException e1)
            {
                e1.printStackTrace();
            }
            logger.error("Applier consummer 数据库更新失败:" + e.getMessage());
            // 保存异常处理消息
            // new TungstenPersistence(transList).save();
            //
        }
    }

    @Override
    public void run()
    {
        try
        {
            connection();
        }
        catch (ApplierException e)
        {
            logger.info(e.getKey());
            logger.info(e.getMessage());
            try
            {
                logger.info("监听mq消息失败，1分钟后重试连接.....");
                this.sleep(1000 * 60);
                logger.info("开始连接MQ服务器");
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }

}
