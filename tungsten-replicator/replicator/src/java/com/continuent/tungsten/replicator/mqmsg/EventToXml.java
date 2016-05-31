package com.continuent.tungsten.replicator.mqmsg;

import com.continuent.tungsten.replicator.event.DBMSEvent;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamer;

public class EventToXml
{
   public static String convert(DBMSEvent event){
       XStream xstream = new XStream();
       return xstream.toXML(event);
   }
   
   public void writeFile(DBMSEvent event){
       
   }
   
}
