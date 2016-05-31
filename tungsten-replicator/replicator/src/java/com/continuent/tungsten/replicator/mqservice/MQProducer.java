
package com.continuent.tungsten.replicator.mqservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.continuent.tungsten.replicator.mqconf.PropertiesAttribute;
import com.continuent.tungsten.replicator.mqconf.Propertiesmgr;
import com.continuent.tungsten.replicator.mqexception.ApplierException;

public class MQProducer
{
    private static MQProducer            instance;
    private static String                key    = "key";
    private ActiveMQConnectionFactory    connectionFactory;
    private Connection                   connection;
    private Session                      session;
    //
    private Destination                  destination;
    private MessageProducer              producer;
    //
    private Map<String, MessageProducer> proMap = new HashMap<String, MessageProducer>();

    public MQProducer() throws ApplierException
    {
        createJmsConn();
    }

    private void createJmsConn() throws ApplierException
    {
        // Create a ConnectionFactory
        connectionFactory = new ActiveMQConnectionFactory(
                "failover:("
                        + Propertiesmgr
                                .getValue(PropertiesAttribute.middle_mq_address)
                        + ")");

        try
        {
            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();
            // Create a Session
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        }
        catch (Exception e)
        {
            closeConn();
            throw new ApplierException("1010", "连接MQ服务失败，制造消息失败", e);
        }
    }

    private void closeConn()
    {
        if (connection != null)
        {
            try
            {
                proMap.clear();
                connection.close();
                session.close();
                connection = null;
                session = null;
            }
            catch (JMSException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public static MQProducer getInstance() throws ApplierException
    {
        if (instance == null)
        {
            synchronized (key)
            {
                if (instance == null)
                {
                    instance = new MQProducer();
                }
                return instance;
            }
        }
        // 检查connect是否为空
        if (instance.connection == null)
        {
            instance.createJmsConn();
        }
        return instance;
    }

    public void send(String queueName, String msg) throws ApplierException

    {
        try
        {
            TextMessage message = session.createTextMessage(msg);
            // Create the destination (Topic or Queue)
            destination = session.createQueue(queueName);
            // Create a MessageProducer from the Session to the Topic or
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(message);
            session.commit();
        }
        catch (Exception e)
        {
            closeConn();
            throw new ApplierException("1011", "Jms发送消息失败", e);
        }
    }

    public synchronized void send(String queueName, List<String> eventLogs)
            throws ApplierException
    {

        try
        {
            if (eventLogs.size() > 0)
            {
                destination = session.createQueue(queueName);
                // Create a MessageProducer from the Session to the Topic or
                producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                for (String el : eventLogs)
                {
                    TextMessage message = session.createTextMessage(el);
                    producer.send(message);

                }
                session.commit();
                eventLogs.clear();
            }
        }
        catch (Exception e)
        {
            closeConn();
            throw new ApplierException("1011", "Jms发送消息失败", e);
        }
    }
    
    public void send(Map<String, List<String>>  logMap) throws ApplierException
    {
        try
        {
            Iterator<Entry<String, List<String>>> it = logMap.entrySet().iterator();
            while (it.hasNext()) {
              Entry<String, List<String>> entry = it.next();
              //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
              createProducer(entry.getKey());
              List<String> eventLogs = entry.getValue();
              produce(entry.getKey(),eventLogs);
            }
            //
            session.commit();
        }
        catch (Exception e)
        {
            closeConn();
            throw new ApplierException("1011", "Jms发送消息失败", e);
        }
    }
    
    private void produce(String key,List<String> eventLogs) throws JMSException{
        for (String el : eventLogs)
        {
            TextMessage message = session.createTextMessage(el);
            proMap.get(key).send(message);
        }
    }
    
    private void createProducer( String key ) throws ApplierException{
        try
        {
            if (proMap.get(key) == null)
            {
                Destination des = session.createQueue(key);
                MessageProducer pro  = session.createProducer(des);
                pro.setDeliveryMode(DeliveryMode.PERSISTENT);
                proMap.put(key, pro);
            }
            
        }
        catch (Exception e)
        {
            throw new ApplierException("1011", "Jms发送消息失败", e);
        }
    }

    public static void main(String args[]) throws JMSException, IOException
    {
        
    }
}
